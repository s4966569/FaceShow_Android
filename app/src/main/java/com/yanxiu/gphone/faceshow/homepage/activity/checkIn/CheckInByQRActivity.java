package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.checkin.UserSignInRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.qrsignup.QRCodeChecker;
import com.yanxiu.gphone.faceshow.qrsignup.request.QrClazsInfoRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.QrClazsInfoResponse;
import com.yanxiu.gphone.faceshow.util.LBSManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Class: CheckInByQRActivity
 * @Description: 自定义条形码/二维码扫描
 * @Author: frc
 * @Date: 2017/5/19
 */

public class CheckInByQRActivity extends FaceShowBaseActivity {

    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 条形码扫描管理器
     */
    private CheckInCaptureManager mCaptureManager;

    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;

    /**
     * 条码处理器
     * */
    private QRCodeChecker qrCodeChecker;

    private LoadingDialogView mLoadingDialogView;

    public static void toThisAct(Activity activity) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        // 设置自定义扫描Activity
        intentIntegrator.setCaptureActivity(CheckInByQRActivity.class);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_zxing_layout);
        ButterKnife.bind(this);
        initToolbar();
        mBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        mCaptureManager = new CheckInCaptureManager(this, mBarcodeView);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mCaptureManager.decode();
        mCaptureManager.setCodeCallBack(codeCallBack);
        qrCodeChecker=new QRCodeChecker();
        dialogInit();
    }

    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
            if (result != null) {
                if (TextUtils.isEmpty(result)) {
                    CheckInByQRActivity.this.finish();
                } else {
                    if (qrCodeChecker.isCheckInCode(result)) {
                        /*检查结果为 签到二维码*/
                        String[] values = result.split("&");
                        if (values.length > 2) {
                            //包含timestamp的为动态二维码
                            getLocation((values[1].split("="))[1], (values[2].split("="))[1]);
                        } else {
                            getLocation((values[1].split("="))[1], "");
                        }
                    }else if (qrCodeChecker.isClazzCode(result)){
                        /*检查结果为 班级二维码 发起 班级 绑定请求*/
                        scanClazsRequest(result);
                    }else {
                        /*其他*/
                        Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                        CheckInResponse.Error error = new CheckInResponse.Error();
                        error.setCode(CheckInErrorActivity.QR_NO_USE);
                        intent.putExtra(CheckInErrorActivity.QR_STATUE, error);
                        startActivity(intent);
                        CheckInByQRActivity.this.finish();
                    }
//                    //判断是否为当前app返回的字段  二维码内容应该为:http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&stepId=xxx&timestamp=xxxxxxx
//                    if (result.startsWith(UrlRepository.getInstance().getServer()+"?method=interact.userSignIn")) {
//                        String[] values = result.split("&");
//                        if (values.length > 2) {
//                            //包含timestamp的为动态二维码
//                            getLocation((values[1].split("="))[1], (values[2].split("="))[1]);
//                        } else {
//                            getLocation((values[1].split("="))[1], "");
//                        }
//
//                    } else {
//                        Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
//                        CheckInResponse.Error error = new CheckInResponse.Error();
//                        error.setCode(CheckInErrorActivity.QR_NO_USE);
//                        intent.putExtra(CheckInErrorActivity.QR_STATUE, error);
//                        startActivity(intent);
//                        CheckInByQRActivity.this.finish();
//                    }
                }
            } else {
                CheckInByQRActivity.this.finish();
            }
        }
    };

    private LocationClient getLocation(final String stepId, final String timestamp) {
        if (mLoadingDialogView == null) {
            mLoadingDialogView = new LoadingDialogView(this);
        }
        mLoadingDialogView.show();
        final LocationClient locationClient = LBSManager.getLocationClient();
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //不解除监听下次再进来会注册多重监听
                locationClient.unRegisterLocationListener(this);
                locationClient.stop();
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();
                if (TextUtils.isEmpty(bdLocation.getLocationDescribe())){
                    userSignIn(stepId, timestamp, "", "");
                }else {
                    userSignIn(stepId, timestamp, longitude + "," + latitude, bdLocation.getLocationDescribe());
                }
            }

        });
        locationClient.start();
        return locationClient;
    }
/**
 * 签到 请求
 * */
    private void userSignIn(String stepId, String timestamps, @NonNull String position, @NonNull String site) {
        UserSignInRequest userSignInRequest = new UserSignInRequest();
        userSignInRequest.position = position;
        userSignInRequest.site = site;
        userSignInRequest.stepId = stepId;
        userSignInRequest.timestamp = timestamps;
        userSignInRequest.startRequest(CheckInResponse.class, new HttpCallback<CheckInResponse>() {
            @Override
            public void onSuccess(RequestBase request, CheckInResponse userSignInResponse) {
                mLoadingDialogView.dismiss();
                if (userSignInResponse.getCode() == 0) {
                    CheckInSuccessActivity.toThiAct(CheckInByQRActivity.this, userSignInResponse);
                    CheckInByQRActivity.this.finish();
                } else {
                    if (userSignInResponse.getError().getCode() == 210414) {
                        //用户已签到
                        CheckInSuccessActivity.toThiAct(CheckInByQRActivity.this, userSignInResponse);
                    } else {
                        Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                        intent.putExtra(CheckInErrorActivity.QR_STATUE, userSignInResponse.getError());
                        startActivity(intent);
                    }
                    CheckInByQRActivity.this.finish();
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingDialogView.dismiss();
                Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                startActivity(intent);
                CheckInByQRActivity.this.finish();
            }
        });

    }

/**
 * 加入班级请求
 * */
    private void scanClazsRequest(String result){
        QrClazsInfoRequest clazsInfoRequest = new QrClazsInfoRequest();

        clazsInfoRequest.clazsId = qrCodeChecker.getClazsIdFromQR(result)+"";
        clazsInfoRequest.startRequest(QrClazsInfoResponse.class, new HttpCallback<QrClazsInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, final QrClazsInfoResponse ret) {
                /*网络请求成功*/
                mLoadingDialogView.dismiss();
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    /*服务器请求成功*/
                    if (ret.getClazsInfo() != null||ret.getClazsId()!=0) {
                        /*可以获取到 classId*/
                        alertDialog.setMessage("成功加入【"+ret.getClazsInfo().getClazsName()+"】");
                        alertDialog.show();
                    }else {
                        /*没有获取到有效的classId*/
//                        ToastUtil.showToast(QRCodeSignUpActivity.this,"班级信息获取失败！");
                    }
                } else {
                    /*code!=0 是 服务器返回了 异常情况 可以判断是否是 班级二维码 或者 是其他二维码 */
//                    setErrorMsg(ret);
                    if (ret.getCode()== ResponseConfig.ERROR_QR_HAS_JOINED_CLASS) {
                        /*已经加入了该班级*/
                        alertDialog.setMessage("已经加入此班级，点【确定】打开班级首页");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 2018/3/8  执行切换班级操作并 回到 首页
                                /*直接设置 userInfo 的班级信息 */
                                UserInfo.Info info = SpManager.getUserInfo();
                                info.setClassId(String.valueOf(ret.getClazsInfo().getId()));
                                info.setClassName(ret.getClazsInfo().getClazsName());
                                info.setProjectName(ret.getClazsInfo().getProjectName());
                                SpManager.saveUserInfo(info);

                            }
                        });
                        alertDialog.show();
                    }else {

                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingDialogView.dismiss();
//                publicLoadLayout.showNetErrorView();
//                publicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clazsInfoRequest(clazsId);
//                    }
//                });
            }
        });
    }


    /**
     * 初始化窗口
     */
    private void initWindow() {
        // API_19及其以上透明掉状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
//            layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags;
//        }
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        tvTitle.setText("签到");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureManager.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mCaptureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCaptureManager.onSaveInstanceState(outState);
    }

    /**
     * 权限处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mCaptureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 按键处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.img_left)
    public void onViewClicked() {
        this.finish();
    }


    private AlertDialog alertDialog;

    private void dialogInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckInByQRActivity.this);
        builder.setMessage("dialog").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
    }
}
