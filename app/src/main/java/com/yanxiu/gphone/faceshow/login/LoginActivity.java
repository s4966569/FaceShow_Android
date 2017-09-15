package com.yanxiu.gphone.faceshow.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.login.SignInRequest;
import com.yanxiu.gphone.faceshow.http.login.SignInResponse;
import com.yanxiu.gphone.faceshow.util.statueBar.StatusBarUtils;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginActivity extends FaceShowBaseActivity {

    private Unbinder unbinder;
    private Context mContext;
    private PublicLoadLayout rootView;
    @BindView(R.id.edt_account_number)
    ClearEditText edt_account_number;
    @BindView(R.id.edt_account_password)
    ClearEditText edt_account_password;
    @BindView(R.id.img_show_password)
    ImageView img_show_password;
    @BindView(R.id.tv_sign_in)
    TextView tv_sign_in;
    private boolean isPasswordShow = false;
    private boolean isCanSignIn = false;
    private UUID mSignInRequestUUID;
    private TextWatcher accountNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (edt_account_password.getText().length() > 0 && charSequence.length() > 0) {
                isCanSignIn = true;
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_bg);
                tv_sign_in.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                isCanSignIn = false;
                tv_sign_in.setBackgroundResource(R.drawable.shape_password_bg);
                tv_sign_in.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private TextWatcher accountPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (edt_account_number.getText().length() > 0 && charSequence.length() > 0) {
                isCanSignIn = true;
                tv_sign_in.setBackgroundResource(R.drawable.shape_sign_in_bg);
                tv_sign_in.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                isCanSignIn = false;
                tv_sign_in.setBackgroundResource(R.drawable.shape_password_bg);
                tv_sign_in.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            StatusBarUtils.setStatusBarFullScreen(LoginActivity.this);
        }
        unbinder = ButterKnife.bind(this);
        edt_account_number.addTextChangedListener(accountNumberTextWatcher);
        edt_account_password.addTextChangedListener(accountPasswordTextWatcher);
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
                if (isCanSignIn) {
                    signInRequest();
                } else if (edt_account_number.getText().length() <= 0) {
                    Toast.makeText(mContext, "账户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (edt_account_password.getText().length() <= 0) {
                    Toast.makeText(mContext, "账户密码不能为空", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void signInRequest() {
        rootView.showLoadingView();
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.accountNumber = edt_account_number.getText().toString();
        signInRequest.accountPassword = edt_account_password.getText().toString();
        mSignInRequestUUID = signInRequest.startRequest(SignInResponse.class, new HttpCallback<SignInResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignInResponse ret) {
                rootView.hiddenLoadingView();
                if (ret.getStatus().getCode() == 0) {
                    // TODO: 17-9-14
                }
                toMainActivity();
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                rootView.hiddenLoadingView();
                toMainActivity();
//                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toMainActivity() {
        SpManager.haveSignIn();
        MainActivity.invoke(LoginActivity.this);
    }
}
