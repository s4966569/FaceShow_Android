package com.yanxiu.gphone.faceshow.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshow.http.login.SignInRequest;
import com.yanxiu.gphone.faceshow.http.login.SignInResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 登录页面
 * created by frc
 */
public class LoginActivity extends FaceShowBaseActivity {

    private Unbinder unbinder;
    private Context mContext;
    private PublicLoadLayout rootView;
    @BindView(R.id.edt_account_number)
    EditText edt_account_number;
    @BindView(R.id.edt_account_password)
    EditText edt_account_password;
    @BindView(R.id.img_show_password)
    ImageView img_show_password;
    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;
    private boolean isPasswordShow = false;
    private UUID mSignInRequestUUID;

    public static void toThisAct(Activity activity) {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rootView = new PublicLoadLayout(mContext);
        rootView.setContentView(R.layout.activity_login);
        setContentView(rootView);
//    如果修改状态栏则ｓｃｒｏｌｌＶｉｅｗ失效    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarUtils.setStatusBarFullScreen(LoginActivity.this);
//        }
        unbinder = ButterKnife.bind(this);
        if (SpManager.isFristStartUp())
            SpManager.setFristStartUp(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mSignInRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mSignInRequestUUID);
        }
    }

    @OnClick({R.id.img_show_password, R.id.tv_sign_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_show_password:
                if (isPasswordShow) {
                    edt_account_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordShow = false;
                } else {
                    edt_account_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordShow = true;
                }
                break;
            case R.id.tv_sign_in:
                if (edt_account_number.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_name_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else if (edt_account_password.getText().length() <= 0) {
                    Toast.makeText(mContext, R.string.account_password_can_not_be_null, Toast.LENGTH_SHORT).show();
                } else {
                    signInRequest();
                }

                break;
        }
    }

    private void signInRequest() {
        rootView.showLoadingView();
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.loginName = edt_account_number.getText().toString();
        signInRequest.password = Utils.MD5Helper(edt_account_password.getText().toString());
        mSignInRequestUUID = signInRequest.startRequest(SignInResponse.class, new HttpCallback<SignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignInResponse ret) {

                if (ret.getCode() == 0) {
                    SpManager.saveToken(ret.getToken());
                    getUserInfo(LoginActivity.this);
                } else {
                    Toast.makeText(mContext, ret.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    rootView.hiddenLoadingView();
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserInfo(final Activity activity) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                if (ret.getCode() == 0) {
                    UserInfo.getInstance().setInfo(ret.getData());
                    MainActivity.invoke(activity);
                } else {
                    ToastUtil.showToast(activity, ret.getError().getMessage());
                }
                rootView.hiddenLoadingView();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                ToastUtil.showToast(FaceShowApplication.getContext(), error.getMessage());
            }
        });
    }

    private void toMainActivity() {
        SpManager.haveSignIn();
        MainActivity.invoke(LoginActivity.this);
    }
}
