package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.request.SetPasswordRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.SetpasswordResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * 设置用户密码界面
 * 当前 为 扫描注册的流程中一步
 * 后期可以复用
 */
public class SetPasswordFragment extends FaceShowBaseFragment {
    private View rootView;
    /*toolbar*/
    private ImageView titleLeftImage;
    private TextView titleRightText;
    private TextView titleTextView;

    private ToolbarActionCallback toolbarActionCallback;


    /*密码编辑框*/
    private ClearEditText passwordEditText;
    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView=inflater.inflate(R.layout.fragment_setpassword_layout,null);
            passwordEditText=rootView.findViewById(R.id.setpassword_edit);
            passwordEditText.addTextChangedListener(pswEditWatcher);

        }
        toolbarInit(rootView);
        disableNextStepBtn();
        return rootView;
    }



    /**
     * 对当前界面进行toolbar 设置
     * */
    private void toolbarInit(View root){
        View toolbar=root.findViewById(R.id.setpassword_titlebar);
        titleLeftImage= (ImageView) toolbar.findViewById(R.id.title_layout_left_img);
        titleRightText= (TextView) toolbar.findViewById(R.id.title_layout_right_txt);
        titleTextView= (TextView) toolbar.findViewById(R.id.title_layout_title);

        titleRightText.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleTextView.setText("设置密码");
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
                /*点击下一步 首先要对 输入的密码进行格式验证*/
                if (pswFormateCheck(passwordEditText.getText().toString())) {
                    /*这里应该有网络请求 如果有 在请求回调里 调用 点击监听*/
                    if (toolbarActionCallback != null) {
                        toolbarActionCallback.onRightComponentClick();
                    }
                }

            }
        });
    }


    /**
     * 开启下一步
     */
    public void enableNextStepBtn() {
        /*禁止再次编辑电话号*/
//        phoneEditText.setEnabled(false);
        titleRightText.setEnabled(true);
        titleRightText.setTextColor(getActivity().getResources().getColor(R.color.color_1da1f2));
    }

    /**
     * 关闭下一步
     */
    public void disableNextStepBtn() {
        titleRightText.setEnabled(false);
        titleRightText.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
    }

    /**
     * 编辑监听 当用户开始编辑 密码输入框时 使能 下一步按钮
     * */
    private TextWatcher pswEditWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length()>0) {
                enableNextStepBtn();
            }else {
                disableNextStepBtn();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    /**
     * 网络请求 设置密码
     * */
    private void setPasswordRequest(){
        SetPasswordRequest setPasswordRequest=new SetPasswordRequest();
        setPasswordRequest.startRequest(SetpasswordResponse.class, new HttpCallback<SetpasswordResponse>() {
            @Override
            public void onSuccess(RequestBase request, SetpasswordResponse ret) {

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
    }


    /**
     * 对 用户输入的密码进行格式验证
     * */
    private boolean pswFormateCheck(String psw){
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.showToast(getActivity(),"密码不能为空");
            return false;
        }
        if (psw.length()<6||psw.length()>12) {
            ToastUtil.showToast(getActivity(),"密码长度在6到12位");
            return false;
        }

        /*还要判断 不包含特殊字符 保证 密码由数字和字母组成 以及 字母大小写敏感*/

        return true;
    }

}
