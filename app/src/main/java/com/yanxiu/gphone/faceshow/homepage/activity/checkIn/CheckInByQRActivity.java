package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
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
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.ScanClazsCodeResponse;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.checkin.UserSignInRequest;
import com.yanxiu.gphone.faceshow.http.course.GetStudentClazsesResponse;
import com.yanxiu.gphone.faceshow.http.course.GetSudentClazsesRequest;
import com.yanxiu.gphone.faceshow.http.main.ScanClazsCodeRequest;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.qrsignup.QRCodeChecker;
import com.yanxiu.gphone.faceshow.util.LBSManager;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.UUID;

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

    private final String TAG = getClass().getSimpleName();

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
     */
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
        qrCodeChecker = new QRCodeChecker();
        mLoadingDialogView = new LoadingDialogView(this);
        dialogInit();
    }

    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
            Log.i(TAG, "callBack: result = " + result);
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
                    } else if (qrCodeChecker.isClazzCode(result)) {
                        /*检查结果为 班级二维码 发起 班级 绑定请求*/
                        scanClazsRequest(qrCodeChecker.getClazsIdFromQR(result)+"");
                        restartScan();
                    } else {
                        /*其他 进入签到错误信息页面*/
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
                if (TextUtils.isEmpty(bdLocation.getLocationDescribe())) {
                    userSignIn(stepId, timestamp, "", "");
                } else {
                    userSignIn(stepId, timestamp, longitude + "," + latitude, bdLocation.getLocationDescribe());
                }
            }

        });
        locationClient.start();
        return locationClient;
    }

    /**
     * 签到 请求
     */
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
     */
    private void scanClazsRequest(final String clazsId) {
        ScanClazsCodeRequest clazsInfoRequest = new ScanClazsCodeRequest();
        clazsInfoRequest.clazsId = clazsId;
        clazsInfoRequest.startRequest(ScanClazsCodeResponse.class, new HttpCallback<ScanClazsCodeResponse>() {
            @Override
            public void onSuccess(RequestBase request, final ScanClazsCodeResponse ret) {
//                Log.i(TAG, "onSuccess: " + new Gson().toJson(ret));
                /*网络请求成功*/
                mLoadingDialogView.dismiss();
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    /*服务器请求成功*/
                    if (ret.getData() != null && ret.getData().getClazsInfo() != null) {
                        /*可以获取到 classId*/
                        alertDialog.setMessage("成功加入【" + ret.getData().getClazsInfo().getClazsName() + "】");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 2018/3/8  执行切换班级操作并 回到 首页
                                /*直接设置 userInfo 的班级信息 在用户班级列表里查找 班级信息 */
                                getClassListData(clazsId);
                                /*这里如何控制 首页的刷新？*/
                                // TODO: 2018/3/9 通知首页刷新
                            }
                        });
                        alertDialog.show();
                    } else {
                        /*没有获取到有效的classId*/
                        ToastUtil.showToast(CheckInByQRActivity.this, getErrorMsg(ret));
                    }
                } else {
                    if (ret.getError().getCode() == ResponseConfig.ERROR_QR_HAS_JOINED_CLASS) {
                        /*已经加入了该班级*/
                        alertDialog.setMessage(ret.getError().getMessage());
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 2018/3/8  执行切换班级操作并 回到 首页
                                /*直接设置 userInfo 的班级信息 在用户班级列表里查找 班级信息 */
                                getClassListData(clazsId);
                                /*这里如何控制 首页的刷新？*/
                                // TODO: 2018/3/9 通知首页刷新
                            }
                        });
                        alertDialog.show();
                    } else {
                        /*其他异常情况*/
                        ToastUtil.showToast(CheckInByQRActivity.this, getErrorMsg(ret));

                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingDialogView.dismiss();
                Log.i(TAG, "onFail: " + error.getMessage());
            }
        });
    }

    private UUID mUUID;

    private void getClassListData(final String clazsId) {
        mLoadingDialogView.show();
        GetSudentClazsesRequest getSudentClazsesRequest = new GetSudentClazsesRequest();
        mUUID = getSudentClazsesRequest.startRequest(GetStudentClazsesResponse.class, new HttpCallback<GetStudentClazsesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetStudentClazsesResponse ret) {
//                Log.i(TAG, "onSuccess: "+new Gson().toJson(ret));
                mLoadingDialogView.dismiss();
                mUUID = null;
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().getClazsInfos() != null && ret.getData().getClazsInfos().size() > 0) {
                        for (GetStudentClazsesResponse.ClazsInfosBean clazsInfosBean : ret.getData().getClazsInfos()) {
                            if (clazsId.equals(clazsInfosBean.getId() + "")) {
                                UserInfo.Info info = SpManager.getUserInfo();
                                if (info == null) {
                                    Log.e(TAG, " sp get user info null" );
                                }
                                info.setClassId(String.valueOf(clazsId));
                                info.setClassName(clazsInfosBean.getClazsName());
                                info.setProjectName(clazsInfosBean.getProjectName());
                                SpManager.saveUserInfo(info);
                                MainActivity.invoke(CheckInByQRActivity.this);
                                CheckInByQRActivity.this.finish();
                                break;
                            }else {
                                ToastUtil.showToast(CheckInByQRActivity.this,"没有找到目标班级！");
                            }
                        }
                    }else {
                        if (ret.getError() != null&& !TextUtils.isEmpty(ret.getError().getMessage())) {
                            ToastUtil.showToast(CheckInByQRActivity.this, ret.getError().getMessage());
                        }else {
                            ToastUtil.showToast(CheckInByQRActivity.this,
                                    TextUtils.isEmpty(ret.getMessage())?"获取班级列表失败！":ret.getMessage());
                        }

                    }
                } else {
//                    ToastUtil.showToast(CheckInByQRActivity.this, ret.getError().getMessage());
                    if (ret.getError() != null&& !TextUtils.isEmpty(ret.getError().getMessage())) {
                        ToastUtil.showToast(CheckInByQRActivity.this, ret.getError().getMessage());
                    }else {
                        ToastUtil.showToast(CheckInByQRActivity.this,
                                TextUtils.isEmpty(ret.getMessage())?"获取班级列表失败！":ret.getMessage());
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingDialogView.dismiss();
                mUUID = null;
                ToastUtil.showToast(CheckInByQRActivity.this, error.getMessage());

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
                CheckInByQRActivity.this.finish();
            }
        });

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private String getErrorMsg(FaceShowBaseResponse ret) {
        if (ret.getError() != null) {
            return TextUtils.isEmpty(ret.getError().getMessage()) ?
                    "请求失败" : ret.getError().getMessage();
        } else {
            return TextUtils.isEmpty(ret.getMessage()) ?
                    "请求失败" : ret.getMessage();
        }
    }

    private void restartScan() {
        mCaptureManager.decode();
        mCaptureManager.setCodeCallBack(codeCallBack);
        mCaptureManager.onResume();

    }
}
