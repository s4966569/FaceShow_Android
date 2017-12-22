package com.yanxiu.gphone.faceshow.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改用户性别页面
 * Created by frc on 2017/10/19.
 */

public class ModifyUserSexActivity extends FaceShowBaseActivity {
    @BindView(R.id.title_layout_left_img)
    ImageView titleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView titleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView titleLayoutRightTxt;
    @BindView(R.id.boy_click)
    ImageView boyClick;
    @BindView(R.id.rl_boy)
    RelativeLayout rlBoy;
    @BindView(R.id.girl_click)
    ImageView girlClick;
    @BindView(R.id.rl_girl)
    RelativeLayout rlGirl;
    private PopupWindow mCancelPopupWindow;
    private InputMethodManager imm;
    private PublicLoadLayout publicLoadLayout;
    private String sexId;
    private String sexName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_modify_user_sex);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        titleLayoutTitle.setText(R.string.modify_sex);
        titleLayoutTitle.setVisibility(View.VISIBLE);
        titleLayoutLeftImg.setVisibility(View.VISIBLE);
        titleLayoutRightTxt.setText(R.string.save);
        titleLayoutRightTxt.setVisibility(View.VISIBLE);
        if (SpManager.getUserInfo().getSexName().endsWith("男")) {
            sexId = "1";
            sexName = "男";
            boyClick.setVisibility(View.VISIBLE);
            girlClick.setVisibility(View.INVISIBLE);
        } else {
            sexId = "0";
            sexName = "女";
            boyClick.setVisibility(View.INVISIBLE);
            girlClick.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.title_layout_left_img, R.id.rl_boy, R.id.rl_girl, R.id.title_layout_right_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.rl_boy:
                sexId = "1";
                sexName = "男";
                boyClick.setVisibility(View.VISIBLE);
                girlClick.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_girl:
                sexId = "0";
                sexName = "女";
                girlClick.setVisibility(View.VISIBLE);
                boyClick.setVisibility(View.INVISIBLE);
                break;
            case R.id.title_layout_right_txt:
                modifyUserInfo();
                break;
            default:
                break;
        }
    }

    private void modifyUserInfo() {
        publicLoadLayout.showLoadingView();
        ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
        modifyUserInfoRequest.sex = sexId;
        modifyUserInfoRequest.startRequest(ModifyUserInfoResponse.class, new FaceShowBaseCallback<ModifyUserInfoResponse>() {
            @Override
            protected void onResponse(RequestBase request, ModifyUserInfoResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    UserInfo.Info userInfo = UserInfo.getInstance().getInfo();
                    userInfo.setSexName(sexName);
                    userInfo.setSex(Integer.valueOf(sexId));
                    SpManager.saveUserInfo(userInfo);
                    Toast.makeText(getApplicationContext(), "性别修改成功", Toast.LENGTH_SHORT).show();
                    ModifyUserSexActivity.this.setResult(RESULT_OK);
                    ModifyUserSexActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), "性别修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.hiddenLoadingView();
                Toast.makeText(getApplicationContext(), "性别修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    @Override
//    public void onBackPressed() {
//        exitDialog();
//    }


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
                    ModifyUserSexActivity.this.finish();
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
