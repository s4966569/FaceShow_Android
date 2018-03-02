package com.yanxiu.gphone.faceshow.qrsignup.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInCaptureManager;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInErrorActivity;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.util.LBSManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 扫描注册的二维码扫描界面 、
 * 继承PublicQRScanActivity
 */
public class QRCodeSignUpActivity extends PublicQRScanActivity {
    private final int REQUEST_CODE_TO_CHECK_PHONE=0X01;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 条形码扫描管理器
     */

    private CheckInCaptureManager mCaptureManager;
    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
            if (result != null) {
                if (TextUtils.isEmpty(result)) {
                    QRCodeSignUpActivity.this.finish();
                } else {
                    //判断是否为当前app返回的字段  二维码内容应该为:http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&stepId=xxx&timestamp=xxxxxxx
//                    if (result.startsWith(UrlRepository.getInstance().getServer() + "?method=interact.userSignIn")) {
                    if (checkQRCode(result)) {
                        /*检查得到的是正确的班级二维码*/
                        Intent currectIntent=new Intent(QRCodeSignUpActivity.this,SignUpActivity.class);
                        startActivityForResult(currectIntent,REQUEST_CODE_TO_CHECK_PHONE);

                        String[] values = result.split("&");
                        if (values.length > 2) {
                            //包含timestamp的为动态二维码
//                            getLocation((values[1].split("="))[1], (values[2].split("="))[1]);
                        } else {
//                            getLocation((values[1].split("="))[1], "");
                        }

                    } else {
                        // TODO: 2018/3/1 弹出对话框提示 二维码错误  必须是班级码才能正确通过并跳转

                        Intent intent = new Intent(QRCodeSignUpActivity.this, CheckInErrorActivity.class);
                        CheckInResponse.Error error = new CheckInResponse.Error();
                        error.setCode(CheckInErrorActivity.QR_NO_USE);
                        intent.putExtra(CheckInErrorActivity.QR_STATUE, error);
                        startActivity(intent);
                        QRCodeSignUpActivity.this.finish();
                    }

                }
            } else {
                QRCodeSignUpActivity.this.finish();
            }
        }
    };
//    private LocationClient getLocation(final String stepId, final String timestamp) {
//        if (mLoadingDialogView == null) {
//            mLoadingDialogView = new LoadingDialogView(this);
//        }
//        mLoadingDialogView.show();
//        final LocationClient locationClient = LBSManager.getLocationClient();
//        locationClient.registerLocationListener(new BDAbstractLocationListener() {
//            @Override
//            public void onReceiveLocation(BDLocation bdLocation) {
//                //不解除监听下次再进来会注册多重监听
//                locationClient.unRegisterLocationListener(this);
//                locationClient.stop();
//                double latitude = bdLocation.getLatitude();
//                double longitude = bdLocation.getLongitude();
//                if (TextUtils.isEmpty(bdLocation.getLocationDescribe())){
//                    userSignIn(stepId, timestamp, "", "");
//                }else {
//                    userSignIn(stepId, timestamp, longitude + "," + latitude, bdLocation.getLocationDescribe());
//                }
//            }
//
//        });
//        locationClient.start();
//        return locationClient;
//    }
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
        setContentView(R.layout.activity_zxing_layout);
        ButterKnife.bind(this);
        initToolbar();
        mBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        mCaptureManager = new CheckInCaptureManager(this, mBarcodeView);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mBarcodeView.setStatusText("请扫描二维码完成注册");


    }


    @Override
    protected void onStart() {
        super.onStart();
        mCaptureManager.decode();
        mCaptureManager.setCodeCallBack(codeCallBack);
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
        View titleView=findViewById(R.id.checkup_titlebar);
        ImageView backImg=titleView.findViewById(R.id.img_left);
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
        return true;
    }

    @Override
    protected void scanResult(String code, boolean success) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE_TO_CHECK_PHONE){
            if (resultCode==RESULT_OK) {
                QRCodeSignUpActivity.this.finish();
            }
        }
    }
}
