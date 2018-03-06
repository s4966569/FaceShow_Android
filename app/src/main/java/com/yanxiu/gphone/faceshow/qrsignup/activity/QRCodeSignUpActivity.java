package com.yanxiu.gphone.faceshow.qrsignup.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInCaptureManager;
import com.yanxiu.gphone.faceshow.qrsignup.QRCodeChecker;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

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
    /* 条形码格式检查*/
    QRCodeChecker qrCodeChecker;

    private CheckInCaptureManager mCaptureManager;
    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
            if (result != null) {
                /*检查是否符合 班级二维码格式 */
                if (qrCodeChecker.isClazzCode(result)) {
                    /*符合班级二维码格式 进行跳转，进入 号码检查界面*/
                    Intent currectIntent=new Intent(QRCodeSignUpActivity.this,SignUpActivity.class);
                    startActivityForResult(currectIntent,REQUEST_CODE_TO_CHECK_PHONE);
                }else {
                    /*不符合 班级二维码格式 显示提示*/
                    ToastUtil.showToast(QRCodeSignUpActivity.this,"无效二维码！");
                    /*重新开启 扫描*/
                    restartScan();
                }
            } else {
                QRCodeSignUpActivity.this.finish();
            }
        }
    };

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
        /*二维码格式检查工具*/
        qrCodeChecker=new QRCodeChecker();
    }
    private void restartScan(){
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
