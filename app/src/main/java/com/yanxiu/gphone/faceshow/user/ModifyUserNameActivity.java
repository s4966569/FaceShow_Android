package com.yanxiu.gphone.faceshow.user;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.LoadingView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseCallback;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.user.request.ModifyUserInfoRequest;
import com.yanxiu.gphone.faceshow.user.response.ModifyUserInfoResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改用户名界面
 * Created by frc on 2017/10/19.
 */

public class ModifyUserNameActivity extends FaceShowBaseActivity {
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView titleLayoutRightTxt;
    @BindView(R.id.edt_name)
    EditText edtName;
    private PopupWindow mCancelPopupWindow;
    private InputMethodManager imm;
    private PublicLoadLayout publicLoadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(ModifyUserNameActivity.this);
        publicLoadLayout.setContentView(R.layout.activity_modify_user_name);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutTitle.setText(R.string.modify_user_name);
        titleLayoutRightTxt.setText(R.string.save);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        edtName.setText(SpManager.getUserInfo().getRealName());
    }


    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                exitDialog();
                break;
            case R.id.title_layout_right_txt:
                saveName(edtName.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 保存用户名
     *
     * @param userName 用户名
     */
    private void saveName(final String userName) {
        publicLoadLayout.showLoadingView();
        ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
        modifyUserInfoRequest.realName = userName;
        modifyUserInfoRequest.startRequest(ModifyUserInfoResponse.class, new FaceShowBaseCallback<ModifyUserInfoResponse>() {
            @Override
            protected void onResponse(RequestBase request, ModifyUserInfoResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    UserInfo.Info userInfo = UserInfo.getInstance().getInfo();
                    userInfo.setRealName(userName);
                    SpManager.saveUserInfo(userInfo);
                    ToastUtil.showToast(getApplicationContext(), "姓名保存成功");
                    ModifyUserNameActivity.this.setResult(RESULT_OK);
                    ModifyUserNameActivity.this.finish();
                } else {
                    ToastUtil.showToast(getApplicationContext(), "姓名保存失败");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(getApplicationContext(), "姓名保存失败");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (!edtName.getText().toString().equals(SpManager.getUserInfo().getRealName())) {
            exitDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void exitDialog() {
        if (imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(titleLayoutLeftImg.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        showCancelPopupWindow(this);
    }

    private void showCancelPopupWindow(Activity context) {
        if (mCancelPopupWindow == null) {
            View pop = LayoutInflater.from(context).inflate(R.layout.pop_ask_cancel_layout, null);
            (pop.findViewById(R.id.tv_pop_sure)).setOnClickListener(popupWindowClickListener);
            (pop.findViewById(R.id.tv_cancel)).setOnClickListener(popupWindowClickListener);
            mCancelPopupWindow = new PopupWindow(pop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mCancelPopupWindow.setAnimationStyle(R.style.pop_anim);
            mCancelPopupWindow.setFocusable(true);
            mCancelPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        mCancelPopupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void dismissPopupWindow() {
        if (mCancelPopupWindow != null) {
            mCancelPopupWindow.dismiss();
        }
    }

    View.OnClickListener popupWindowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_pop_sure:
                    dismissPopupWindow();
                    ModifyUserNameActivity.this.finish();
                    break;
                case R.id.tv_cancel:
                    dismissPopupWindow();
                    break;
                default:
                    break;

            }

        }
    };
}
