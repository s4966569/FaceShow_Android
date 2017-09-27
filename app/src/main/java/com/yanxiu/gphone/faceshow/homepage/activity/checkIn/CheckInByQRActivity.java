package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.test.yanxiu.network.OkHttpClientManager;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Class: CheckInByQRActivity
 * @Description: 自定义条形码/二维码扫描
 * @Author: frc
 * @Date: 2017/5/19
 */

public class CheckInByQRActivity extends AppCompatActivity {

    /**
     * 条形码扫描管理器
     */
    private CheckInCaptureManager mCaptureManager;

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
        initWindow();
        setContentView(R.layout.activity_zxing_layout);
        initToolbar();
        mBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        mCaptureManager = new CheckInCaptureManager(this, mBarcodeView);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mCaptureManager.decode();
        mCaptureManager.setCodeCallBack(new CheckInCaptureManager.CodeCallBack() {
            @Override
            public void callBack(String result) {
                if (result != null) {
                    if (TextUtils.isEmpty(result)) {
                        CheckInByQRActivity.this.finish();
                    } else {
                        Log.e("frc", "http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&" + result + "&token=" + SpManager.getToken() + "&device=android");
                        goCheckIn("http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&" + result + "&token=" + SpManager.getToken() + "&device=android");
                    }

                } else {
//            super.onActivityResult(requestCode, resultCode, data);
                    CheckInByQRActivity.this.finish();
                }
            }
        });
    }

    private void goCheckIn(String resultString) {
        if (mLoadingDialogView == null)
            mLoadingDialogView = new LoadingDialogView(this);
        mLoadingDialogView.show();
        Request request = new Request.Builder().url(resultString).build();
        OkHttpClient client = OkHttpClientManager.getInstance();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoadingDialogView.dismiss();
                ToastUtil.showToast(FaceShowApplication.getContext(), R.string.net_error);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        return;
                    }
                } catch (Exception e) {
                }

                String bodyString = response.body().string();

                if (!response.isSuccessful()) {
                    ToastUtil.showToast(FaceShowApplication.getContext(), "服务器数据异常");
                    return;
                }
                try {
                    CheckInResponse userSignInResponse = RequestBase.getGson().fromJson(bodyString, CheckInResponse.class);
                    if (userSignInResponse.getCode() == 0) {
                        CheckInSuccessActivity.toThiAct(CheckInByQRActivity.this, 0, userSignInResponse.getData().getSigninTime());
                        CheckInByQRActivity.this.finish();
                    } else {
                        if (userSignInResponse.getError().getCode() == 210414) {//用户已签到
                            CheckInSuccessActivity.toThiAct(CheckInByQRActivity.this, 210414, userSignInResponse.getError().getData().getStartTime() + "-" + userSignInResponse.getError().getData().getEndTime());
                        } else {
                            Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                            intent.putExtra(CheckInErrorActivity.QR_STATUE, userSignInResponse.getError());
                            startActivity(intent);
                        }
                        CheckInByQRActivity.this.finish();
                    }
                } catch (Exception e) {
                    Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                    startActivity(intent);
                    CheckInByQRActivity.this.finish();
                }
                mLoadingDialogView.dismiss();
            }

        });

    }

    /**
     * 初始化窗口
     */
    private void initWindow() {
        // API_19及其以上透明掉状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutParams.flags;
        }
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
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

}
