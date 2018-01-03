package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.OkHttpClientManager;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.checkin.UserSignInRequest;
import com.yanxiu.gphone.faceshow.http.checkin.UserSignInResponse;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.util.LBSManager;
import com.yanxiu.gphone.faceshow.util.NetWorkUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    }

    CheckInCaptureManager.CodeCallBack codeCallBack = new CheckInCaptureManager.CodeCallBack() {
        @Override
        public void callBack(String result) {
            if (result != null) {
                if (TextUtils.isEmpty(result)) {
                    CheckInByQRActivity.this.finish();
                } else {
                    //判断是否为当前app返回的字段  二维码内容应该为:http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&stepId=xxx&timestamp=xxxxxxx
                    if (result.startsWith(UrlRepository.getInstance().getServer()+"?method=interact.userSignIn")) {
                        String[] values = result.split("&");
                        if (values.length > 2) {
                            //包含timestamp的为动态二维码
                            getLocation((values[1].split("="))[1], (values[2].split("="))[1]);
                        } else {
                            getLocation((values[1].split("="))[1], "");
                        }

                    } else {
                        Intent intent = new Intent(CheckInByQRActivity.this, CheckInErrorActivity.class);
                        CheckInResponse.Error error = new CheckInResponse.Error();
                        error.setCode(CheckInErrorActivity.QR_NO_USE);
                        intent.putExtra(CheckInErrorActivity.QR_STATUE, error);
                        startActivity(intent);
                        CheckInByQRActivity.this.finish();
                    }

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
}
