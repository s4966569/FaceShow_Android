package com.yanxiu.gphone.faceshow.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.edt_phone_number)
    ClearEditText edtPhoneNumber;
    @BindView(R.id.tv_get_verification_code)
    TextView tvGetVerificationCode;
    @BindView(R.id.edt_verification_code)
    ClearEditText edtVerificationCode;
    @BindView(R.id.edt_input_new_password)
    ClearEditText edtInputNewPassword;
    @BindView(R.id.forget_password_sure)
    TextView forgetPasswordSure;
    PublicLoadLayout publicLoadLayout;

    private boolean isPhoneNumber = false;
    private boolean isVerificationCodeNull = true;
    private boolean isNewPasswordNull = true;

    private TextWatcher mPhoneNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (Utils.isMobileNO(s)) {
                isPhoneNumber = true;
                tvGetVerificationCode.setTextColor(R.color.color_1da1f2);
                tvGetVerificationCode.setBackgroundResource(R.drawable.shape_verification_code_selected);
            } else {
                isPhoneNumber = false;
                tvGetVerificationCode.setTextColor(R.color.color_999999);
                tvGetVerificationCode.setBackgroundResource(R.drawable.shape_verification_code_normal);
            }
            changSureBtnColor();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mVerificationCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            isVerificationCodeNull = TextUtils.isEmpty(s);
            changSureBtnColor();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher mNewPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isNewPasswordNull = TextUtils.isEmpty(s);
            changSureBtnColor();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 是否可以点击确定按钮
     */
    private boolean isCanSure() {
        return isPhoneNumber && !isNewPasswordNull && isVerificationCodeNull;
    }

    private void changSureBtnColor() {
        if (isCanSure()) {
            forgetPasswordSure.setBackgroundResource(R.drawable.shape_verification_code_sure_pressed_bg);
        } else {
            forgetPasswordSure.setBackgroundResource(R.drawable.shape_verification_code_sure_normal_bg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_forget_password);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.forget_password);
        edtPhoneNumber.addTextChangedListener(mPhoneNumberTextWatcher);
        edtVerificationCode.addTextChangedListener(mVerificationCodeTextWatcher);
        edtInputNewPassword.addTextChangedListener(mNewPasswordTextWatcher);


    }


    @OnClick({R.id.title_layout_left_img, R.id.tv_get_verification_code, R.id.forget_password_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.tv_get_verification_code:
                if (isPhoneNumber) {
                    getVerificationCode();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "请填入正确的手机号");
                }
                break;
            case R.id.forget_password_sure:
                if (isCanSure()) {
                    resetNewPassword();
                } else {
                    if (!isPhoneNumber) {
                        ToastUtil.showToast(getApplicationContext(), "请填入正确的手机号");
                    } else if (isVerificationCodeNull) {
                        ToastUtil.showToast(getApplicationContext(), "验证码不能为空");
                    } else if (isNewPasswordNull) {
                        ToastUtil.showToast(getApplicationContext(), "新密码不能为空");
                    }
                }
                break;
        }
    }

    private void resetNewPassword() {

    }

    private void getVerificationCode() {

    }
}
