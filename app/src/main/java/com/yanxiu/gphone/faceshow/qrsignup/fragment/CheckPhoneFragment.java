package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.qrsignup.PhoneNumCheckResponse;
import com.yanxiu.gphone.faceshow.http.qrsignup.PhoneNumberCheckRequest;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;

import java.util.UUID;

/**
 * 手机号码 验证界面
 * */
public class CheckPhoneFragment extends FaceShowBaseFragment {
    private View fragmentRootView;
    private PublicLoadLayout mRootView;
    /*toolbar 控件*/
    private ImageView titleLeftImage;
    private TextView titleTextView;
    private TextView titleRightText;

    /*request  uuid*/
    private UUID checkPhoneNumRequestUUID;

    /**/
    private View checkPhoneView;
    private ClearEditText phoneEditText;
    private ClearEditText verifyCodeEditText;
    private TextView getVerifyCodeTextView;

    private ToolbarActionCallback toolbarActionCallback;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView=new PublicLoadLayout(getActivity());
//            mRootView.setContentView(R.layout.fragment_checkphone_layout);
            fragmentRootView=inflater.inflate(R.layout.fragment_checkphone_layout,null);
            mRootView.setContentView(fragmentRootView);
        }

        /*初始化toolbar*/
        toolbarInit(fragmentRootView);
        /*初始化 view*/
        viewInit(fragmentRootView);
        return mRootView;
    }

    private void viewInit(View root){
        checkPhoneView=root.findViewById(R.id.checkphone_verification_layout);
        phoneEditText= (ClearEditText) checkPhoneView.findViewById(R.id.edt_phone_number);
        verifyCodeEditText= (ClearEditText) checkPhoneView.findViewById(R.id.edt_verification_code);
        getVerifyCodeTextView= (TextView) checkPhoneView.findViewById(R.id.tv_get_verification_code);

        getVerifyCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRootView.showLoadingView();
                /*检查手机号格式*/
                if (isPhoneNumber(phoneEditText.getText().toString())) {
                    /*发起网络请求*/
                    phoneCheckRequest(phoneEditText.getText().toString());
                }else {
                    // TODO: 2018/3/1 提示手机号格式不正确
                    mRootView.hiddenLoadingView();
                }
            }
        });
    }


    /**
     * 对当前界面进行toolbar 设置
     * */
    private void toolbarInit(View root){
        titleLeftImage= (ImageView) root.findViewById(R.id.title_layout_left_img);
        titleRightText= (TextView) root.findViewById(R.id.title_layout_right_txt);
        titleTextView= (TextView) root.findViewById(R.id.title_layout_title);

        titleRightText.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
//        验证成功之前 不能进入下一步
        titleRightText.setEnabled(false);

        titleTextView.setText("验证手机号");
        titleRightText.setText("下一步");
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });
        titleRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onRightComponentClick();
                }
            }
        });
    }

    /**
     * 手机号格式检查
     * */
    private boolean isPhoneNumber(String number){
        return true;
    }

    /**
     * 网络请求 检查手机号
     * */
    private void phoneCheckRequest(String number){
        /*创建 请求对象*/
        PhoneNumberCheckRequest request=new PhoneNumberCheckRequest();

       checkPhoneNumRequestUUID= request.startRequest(PhoneNumCheckResponse.class, new HttpCallback<PhoneNumCheckResponse>() {
            @Override
            public void onSuccess(RequestBase request, PhoneNumCheckResponse ret) {
                mRootView.hiddenLoadingView();
                // TODO: 2018/3/1  验证成功 还要判断 是那种类型
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                // TODO: 2018/3/1 验证失败  没有进行注册的情况吧
            }
        });
    }


    public void enableNextStepBtn(){
        titleRightText.setEnabled(true);
    }
    private CheckVerificationCallback checkVerificationCallback;

    public void setCheckVerificationCallback(CheckVerificationCallback checkVerificationCallback) {
        this.checkVerificationCallback = checkVerificationCallback;
    }

    public interface CheckVerificationCallback{
        void onRequestGetVerifyCode(String phone);
    }


}
