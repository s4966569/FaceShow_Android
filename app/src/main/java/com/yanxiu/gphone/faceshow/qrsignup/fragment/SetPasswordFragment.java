package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.request.SignUpRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.SignUpResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 * 设置用户密码界面
 * 当前 为 扫描注册的流程中一步
 * 后期可以复用
 * <p>
 * 设置完用户密码 后 用户账号已经生成 注册完成，下一步中为 正常的用户信息修改操作
 * 修改信息操作 可 复用已有的功能界面
 */
public class SetPasswordFragment extends FaceShowBaseFragment {
    private View rootView;

    private SysUserBean sysUserBean;
    /*基本的view  包含一些 异常的显示*/
    private PublicLoadLayout mRootView;
    /*toolbar*/
    private ImageView titleLeftImage;
    private TextView titleRightText;
    private TextView titleTextView;
    private ToolbarActionCallback toolbarActionCallback;
    /**
     * classId
     */
    private int scannedClassId = 0;

    public void setScannedClassId(int scannedClassId) {
        this.scannedClassId = scannedClassId;
    }

    private String phoneNumber;

    public void setPhoneNumber(String phone) {
        phoneNumber = phone;
    }

    /*密码编辑框*/
    private ClearEditText passwordEditText;
    /*用户名输入框*/
    private ClearEditText usernameEditText;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetPasswordFragment() {
        // Required empty public constructor
    }

    public SysUserBean getSysUserBean() {
        return sysUserBean;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            mRootView = new PublicLoadLayout(getActivity());
            rootView = inflater.inflate(R.layout.fragment_setpassword_layout, null);
            passwordEditText = rootView.findViewById(R.id.setpassword_edit);
            usernameEditText = rootView.findViewById(R.id.username_edit);
            passwordEditText.addTextChangedListener(editWatcher);
            usernameEditText.addTextChangedListener(editWatcher);

//            usernameEditText.addTextChangedListener(new SketchTextWatcher(usernameEditText));
//            InputFilter[] filters={new SketchLengthFilter()};
//            usernameEditText.setFilters(filters);

            mRootView.setContentView(rootView);
            dialogInit();
        }
        toolbarInit(rootView);
        disableNextStepBtn();
        return mRootView;
    }


    /**
     * 对当前界面进行toolbar 设置
     */
    private void toolbarInit(View root) {
        View toolbar = root.findViewById(R.id.setpassword_titlebar);
        titleLeftImage = (ImageView) toolbar.findViewById(R.id.title_layout_left_img);
        titleRightText = (TextView) toolbar.findViewById(R.id.title_layout_right_txt);
        titleTextView = (TextView) toolbar.findViewById(R.id.title_layout_title);

        titleRightText.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
        titleTextView.setText("填写注册信息");
        titleRightText.setText("下一步");
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*如果 有 提示 界面 需要隐藏*/
                mRootView.hiddenNetErrorView();
                mRootView.hiddenOtherErrorView();
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });
        titleRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*点击下一步 首先要对 输入的密码进行格式验证 并对 用户名进行 格式验证*/
                if (pswFormateCheck(passwordEditText.getText().toString())
                        && userNameFormateCheck(usernameEditText.getText().toString())) {
                    /*这里应该有网络请求 如果有 在请求回调里 调用 点击监听*/
//                    fadeSignUpRequest(phoneNumber,passwordEditText.getText().toString(),scannedClassId);
                    signUpRequest(phoneNumber, usernameEditText.getText().toString(), passwordEditText.getText().toString(), scannedClassId);
                }
            }
        });
    }


    /**
     * 开启下一步
     */
    public void enableNextStepBtn() {
        /*禁止再次编辑电话号*/
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
     * 统一监听
     */
    private TextWatcher editWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkEnable();

        }
    };

    private void checkEnable() {
    /*文字更改后 判断 密码与用户名的长度*/

        int pl = 0;
        int nl = 0;
        if (passwordEditText == null||usernameEditText==null) {
            return;
        }
        if (!TextUtils.isEmpty(passwordEditText.getText())) {
            pl = passwordEditText.getText().toString().length();
        }
        if (!TextUtils.isEmpty(usernameEditText.getText())) {
            nl = usernameEditText.getText().toString().length();
        }
            /*检查 密码长度是否在 6~20 位之间*/
            /*检查用户名长度 是否在1~6 位之间*/
        boolean enable = (pl > 5 && pl < 21) && (nl > 0 && nl < 7);
        if (enable) {
            enableNextStepBtn();
        } else {
            disableNextStepBtn();
        }
    }


    /**
     * 编辑监听 当用户开始编辑 密码输入框时 使能 下一步按钮
     */
//    private TextWatcher pswEditWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            Log.i(TAG, "onTextChanged: ");
//            if (charSequence.length() > 0) {
//                enableNextStepBtn();
//            } else {
//                disableNextStepBtn();
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//            Log.i(TAG, "afterTextChanged: ");
//        }
//    };

//    private TextWatcher usernameWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            /*输入之前 判断当前内容长度*/
//            if (charSequence.length()==6) {
//                ToastUtil.showToast(getActivity(),"用户名最长为6");
//            }
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    };


    /**
     * 模拟请求 成功
     * */
//    private void fadeSignUpRequest(final String phone, final String psw, final int clazsId){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                /*模拟请求成功*/
//                sysUserBean=new SysUserBean();
//                sysUserBean.setRealName("新用户");
//                sysUserBean.setMobilePhone(phone);
//                if (toolbarActionCallback != null) {
//                    toolbarActionCallback.onRightComponentClick();
//                }
//
//
////                if (new Random().nextBoolean()) {
////                /*模拟请求失败*/
////                    disableNextStepBtn();
////                    mRootView.showOtherErrorView("请求失败");
////
////                }else {
////                /*模拟网络问题*/
////                    disableNextStepBtn();
////                    mRootView.showNetErrorView();
////                    mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            fadeSignUpRequest(phone, psw, clazsId);
////                        }
////                    });
////
////                }
//
//            }
//        },500);
//    }


    /**
     * 网络请求 设置密码
     * 实际执行的是用户的注册操作
     * 传递参数 为 手机号 密码MD5  验证码
     */
    private void signUpRequest(final String phone, final String username, final String md5Psw, final int clazsId) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.mobile = phone;
        signUpRequest.password = Utils.MD5Helper(md5Psw);
        signUpRequest.name = username;
        signUpRequest.clazsId = clazsId + "";
        signUpRequest.startRequest(SignUpResponse.class, new HttpCallback<SignUpResponse>() {
            @Override
            public void onSuccess(RequestBase request, SignUpResponse ret) {
                /*注册请求成功*/
                if (ret.getCode() == ResponseConfig.INT_SUCCESS) {
                    /*注册成功*/
                    if (ret.getData() != null) {
                        if (ret.getData().getSysUser() != null) {
                            sysUserBean = ret.getData().getSysUser();
                            if (toolbarActionCallback != null) {
                                toolbarActionCallback.onRightComponentClick();
                            }
                        } else {
                            /*没有返回 用信息的处理*/
                            ToastUtil.showToast(getActivity(), getErrorMsg(ret));
                        }
                    } else {
                        /*没有返回 data  检查error 字段 以及message 字段*/
                        ToastUtil.showToast(getActivity(), getErrorMsg(ret));
                    }
                } else {
                    /*注册失败 */
                    ToastUtil.showToast(getActivity(), getErrorMsg(ret));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.showNetErrorView();
                hideSoftInput(usernameEditText);
                mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        signUpRequest(phone, username, md5Psw, clazsId);
                        mRootView.hiddenNetErrorView();
                    }
                });
            }
        });
    }


    /**
     * toast message
     */
    private void toastErrorMsg(FaceShowBaseResponse ret) {
        if (ret.getError() != null) {
            if (TextUtils.isEmpty(ret.getError().getMessage())) {

            } else {
                ToastUtil.showToast(getActivity(), ret.getError().getMessage());
            }
        } else {
            if (TextUtils.isEmpty(ret.getMessage())) {

            } else {
                ToastUtil.showToast(getActivity(), ret.getMessage());
            }
        }
    }

    /**
     * 根据返回值 获取错误信息
     */
    private void setErrorMsg(SignUpResponse ret) {
        if (ret.getError() != null) {
            /*首先检查 是否携带错误信息*/
            alertDialog.setMessage(ret.getError().getMessage());
        } else {
            /*没有包含错误信息*/
            if (!TextUtils.isEmpty(ret.getMessage())) {
                alertDialog.setMessage(ret.getMessage());
            } else {
                alertDialog.setMessage("请求失败！");
            }
        }
    }

    private boolean userNameFormateCheck(String un) {
        if (TextUtils.isEmpty(un)) {
            ToastUtil.showToast(getActivity(), "用户名不能为空");
            return false;
        }
        if (un.contains(" ")) {
            ToastUtil.showToast(getActivity(), "用户名不能包含空格");
            return false;
        }
        return true;
    }


    /**
     * 对 用户输入的密码进行格式验证
     */
    private boolean pswFormateCheck(String psw) {
        if (TextUtils.isEmpty(psw)) {
            ToastUtil.showToast(getActivity(), "密码不能为空");
            return false;
        }
        if (psw.length() < 6 || psw.length() > 20) {
            ToastUtil.showToast(getActivity(), "密码长度在6到20位");
            return false;
        }
        /*还要判断 不包含特殊字符 保证 密码由数字和字母组成 以及 字母大小写敏感*/
        /*不能包含空格*/
        if (psw.contains(" ")) {
            ToastUtil.showToast(getActivity(), "密码不能包含空格");
            return false;
        }
//        if (Utils.isContainBlank(psw)) {
//            return false;
//        }
        return true;
    }

    private String getErrorMsg(FaceShowBaseResponse ret) {
        if (ret.getError() != null) {
            return TextUtils.isEmpty(ret.getError().getMessage()) ?
                    "请求失败" : ret.getError().getMessage();
        } else {
            return TextUtils.isEmpty(ret.getMessage()) ?
                    "请求失败" : ret.getMessage();
        }
    }

    private AlertDialog alertDialog;

    private void dialogInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("dialog").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }
    private void hideSoftInput(EditText editText) {
        InputMethodManager inputMethodManager= (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }


}


