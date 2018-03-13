package com.yanxiu.gphone.faceshow.qrsignup.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInCaptureManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.qrsignup.QRCodeChecker;
import com.yanxiu.gphone.faceshow.qrsignup.base.PublicQRScanActivity;
import com.yanxiu.gphone.faceshow.qrsignup.request.QrClazsInfoRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.QrClazsInfoResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 扫描注册的二维码扫描界面 、
 * 继承PublicQRScanActivity
 */
public class QRCodeSignUpActivity extends PublicQRScanActivity {

    private final String TAG=getClass().getSimpleName();

    private final int REQUEST_CODE_TO_CHECK_PHONE = 0X01;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /* 条形码格式检查*/
    QRCodeChecker qrCodeChecker;

    /**
     * 异常code
     * */


    /**
     *
     * */
    private PublicLoadLayout publicLoadLayout;
    private CheckInCaptureManager mCaptureManager;
    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
//            fadeScanResult(); 本地模拟请求
            processScanResult(result);
        }
    };

    /**
     * 对扫描二维码的结果进行处理
     * 分析是 哪一种二维码
     * 然后执行哪种后续操作
     */
    private void processScanResult(String result) {
        if (result != null) {
            /*检查是否符合 班级二维码格式 */
            if (qrCodeChecker.isClazzCode(result)) {
                /*符合班级二维码格式 进行跳转，进入 号码检查界面 进行一个网络请求 获取班级详细信息*/
                // TODO: 2018/3/7 拆分 二维码内容 获取classId
                clazsInfoRequest(qrCodeChecker.getClazsIdFromQR(result)+"");
//                toSignUpActivity(qrCodeChecker.getClazsIdFromQR(result));
            } else {
                /*判断是否是 签到二维码 提示请先登录后再签到*/
                if (qrCodeChecker.isCheckInCode(result)) {
                    ToastUtil.showToast(QRCodeSignUpActivity.this,"请先登录后再签到");
                    restartScan();
                } else {
                    QRCodeSignUpActivity.this.finish();
//                    ToastUtil.showToast(QRCodeSignUpActivity.this, "无效二维码！");
//                    restartScan();
                }
            }
        } else {
            QRCodeSignUpActivity.this.finish();
        }
    }

    private void toSignUpActivity(int clazsId) {
        Intent intent = new Intent(QRCodeSignUpActivity.this, SignUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("clazsId", clazsId);
        intent.putExtra("data", bundle);
        startActivityForResult(intent, REQUEST_CODE_TO_CHECK_PHONE);
    }

    /**
     * 模拟扫描二维码处理结果
     */
//    private void fadeScanResult() {
//        Random random = new Random();
//        int resultType = random.nextInt(3);
//        resultType=2;
//        switch (resultType) {
//            case 0:
//                /*无效二维码 弹出提示*/
//                ToastUtil.showToast(QRCodeSignUpActivity.this, "无效二维码！");
//                restartScan();
//                break;
//            case 1:
//                /*非班级二维码 返回登录页*/
//                ToastUtil.showToast(QRCodeSignUpActivity.this,"签到二维码 提示 请先登录后再签到");
//                QRCodeSignUpActivity.this.finish();
//                break;
//            case 2:
//                /*班级二维码 请求班级信息*/
////                ToastUtil.showToast(QRCodeSignUpActivity.this,"是班级二维码 获取班级ID 并请求网络");
////                publicLoadLayout.showLoadingView();
//                mLoadingDialogView.show();
//                fadeClazsInfoRequest();
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 模拟 班级信息网络请求
     */
//    private void fadeClazsInfoRequest() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                /*请求失败*/
////                mLoadingDialogView.dismiss();
//////                publicLoadLayout.showNetErrorView();
////                ToastUtil.showToast(QRCodeSignUpActivity.this,"网络问题 获取 扫码请求 返回结果错误 重新开启 扫码过程");
////
////                publicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////
////                    }
////                });
//////                publicLoadLayout.hiddenNetErrorView();
////                restartScan();
////                publicLoadLayout.hiddenLoadingView();
//                mLoadingDialogView.dismiss();
//                Intent intent = new Intent(QRCodeSignUpActivity.this, SignUpActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("clazsId", 1);
//                intent.putExtra("data", bundle);
//                startActivity(intent);
//            }
//        }, 200);
//    }
    /**
     * 扫码后获取的 url 进行请求
     *
     * */
    private void clazsInfoRequest(final String clazsId) {
        QrClazsInfoRequest clazsInfoRequest = new QrClazsInfoRequest();
        clazsInfoRequest.clazsId = clazsId;
        clazsInfoRequest.startRequest(QrClazsInfoResponse.class, new HttpCallback<QrClazsInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, QrClazsInfoResponse ret) {
                /*网络请求成功*/
//                Log.i(TAG, "onSuccess: "+new Gson().toJson(ret));
                mLoadingDialogView.dismiss();
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    /*服务器请求成功*/
                    if (ret.getData() != null) {
                        /*可以获取到 classId*/
                        Intent intent = new Intent(QRCodeSignUpActivity.this, SignUpActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("info", ret.getData());
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                        QRCodeSignUpActivity.this.finish();
                    }else {
                        /*没有获取到有效的classId*/
                        ToastUtil.showToast(QRCodeSignUpActivity.this,getErrorMsg(ret));
                        restartScan();
                    }
                } else {
                    /*code!=0 是 服务器返回了 异常情况 可以判断是否是 班级二维码 或者 是其他二维码 */
                    ToastUtil.showToast(QRCodeSignUpActivity.this,getErrorMsg(ret));
                    restartScan();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                Log.i(TAG, "onFail: ");
                mLoadingDialogView.dismiss();
                publicLoadLayout.showNetErrorView();
                publicLoadLayout.setRetryButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clazsInfoRequest(clazsId);
                    }
                });
            }
        });
    }
    /**
     * 根据返回值 获取错误信息
     * */
    private void setErrorMsg(QrClazsInfoResponse ret) {
        if (ret.getError() != null) {
            /*首先检查 是否携带错误信息*/
            ToastUtil.showToast(QRCodeSignUpActivity.this,ret.getError().getMessage());
//            alertDialog.setMessage();
        }else {
            /*没有包含错误信息*/
            if (!TextUtils.isEmpty(ret.getMessage())) {
                ToastUtil.showToast(QRCodeSignUpActivity.this,ret.getMessage());
            }else {
                ToastUtil.showToast(QRCodeSignUpActivity.this,"请求失败");
            }
        }
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

    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;


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
//        setContentView(R.layout.activity_qrcode_sign_up);
        initWindow();
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_zxing_layout);
//        setContentView(R.layout.activity_zxing_layout);
        ButterKnife.bind(this);
        initToolbar();
        mBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        mCaptureManager = new CheckInCaptureManager(this, mBarcodeView);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mBarcodeView.setStatusText("请扫描二维码完成注册");
        /*二维码格式检查工具*/
        qrCodeChecker = new QRCodeChecker();
        mLoadingDialogView=new LoadingDialogView(this);
    }

    private void restartScan() {
        mCaptureManager.decode();
        mCaptureManager.setCodeCallBack(codeCallBack);
        mCaptureManager.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        restartScan();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

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
        tvTitle.setText("扫码注册");
        View titleView = findViewById(R.id.checkup_titlebar);
        ImageView backImg = titleView.findViewById(R.id.img_left);
        backImg.setVisibility(View.VISIBLE);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRCodeSignUpActivity.this.finish();
            }
        });

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

    @Override
    protected boolean checkQRCode(String code) {
        /*检查是否是 班级二维码规则*/
        return qrCodeChecker.isClazzCode(code);
    }

    @Override
    protected void scanResult(String code, boolean success) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE_TO_CHECK_PHONE) {
//            if (resultCode == RESULT_OK) {
//                QRCodeSignUpActivity.this.finish();
//            }
//        }
    }
}
