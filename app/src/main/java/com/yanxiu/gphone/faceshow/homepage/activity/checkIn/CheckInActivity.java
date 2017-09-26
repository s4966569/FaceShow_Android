package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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

public class CheckInActivity extends AppCompatActivity {


    public static void toThisAct(Context activity) {
        Intent intent = new Intent(activity
                , CheckInActivity.class);
        activity.startActivity(intent);
    }

    private LoadingDialogView mLoadingDialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        IntentIntegrator intentIntegrator = new IntentIntegrator(CheckInActivity.this);
        // 设置自定义扫描Activity
//        intentIntegrator.setCaptureActivity(CustomCaptureActivity.class);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 获取解析结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (TextUtils.isEmpty(result.getContents())) {
//                Intent intent = new Intent(CheckInActivity.this, CheckInErrorActivity.class);
//                intent.putExtra(CheckInErrorActivity.QR_STATUE, CheckInErrorActivity.QR_INVALID);
//                startActivity(intent);
                CheckInActivity.this.finish();
            } else {
                Log.e("frc", "http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&" + result.getContents() + "&token=" + SpManager.getToken() + "&device=android");
                goCheckIn("http://orz.yanxiu.com/pxt/platform/data.api?method=interact.userSignIn&" + result.getContents() + "&token=" + SpManager.getToken() + "&device=android");
            }

        } else {
//            super.onActivityResult(requestCode, resultCode, data);
            CheckInActivity.this.finish();
        }
    }

    private void goCheckIn(String resultString) {
        if (mLoadingDialogView == null)
            mLoadingDialogView = new LoadingDialogView(CheckInActivity.this);
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
                        CheckInSuccessActivity.toThiAct(CheckInActivity.this, 0, userSignInResponse.getData().getSigninTime());
                        CheckInActivity.this.finish();
                    } else {
                        if (userSignInResponse.getError().getCode() == 210414) {//用户已签到
                            CheckInSuccessActivity.toThiAct(CheckInActivity.this, 210414, userSignInResponse.getError().getData().getStartTime() + "-" + userSignInResponse.getError().getData().getEndTime());
                        } else {
                            Intent intent = new Intent(CheckInActivity.this, CheckInErrorActivity.class);
                            intent.putExtra(CheckInErrorActivity.QR_STATUE, userSignInResponse.getError());
                            startActivity(intent);
                        }
                        CheckInActivity.this.finish();
                    }
                } catch (Exception e) {
                    Intent intent = new Intent(CheckInActivity.this, CheckInErrorActivity.class);
                    startActivity(intent);
                    CheckInActivity.this.finish();
                }
                mLoadingDialogView.dismiss();
            }

        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();

    }
}
