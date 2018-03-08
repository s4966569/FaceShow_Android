package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.ClearEditText;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.dialog.SignUpDialogFragment;
import com.yanxiu.gphone.faceshow.qrsignup.request.PhoneNumCheckRequest;
import com.yanxiu.gphone.faceshow.qrsignup.request.VerifyCodeConfirmRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.CheckPhoneNumResponse;
import com.yanxiu.gphone.faceshow.qrsignup.response.VerifyCodeConfirmResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * 手机号码 验证界面
 * 功能：
 * 1、检查手机号格式
 * 2、发起网络请求验证 手机号
 * 3、验证码倒计时
 * 4、根据手机验证结果 设置UI状态并回调给 SignUpActivity
 */
public class CheckPhoneFragment extends FaceShowBaseFragment {
    private final String TAG = getClass().getSimpleName();

    private SysUserBean sysUserBean;

    public SysUserBean getSysUserBean() {
        return sysUserBean;
    }

    /*dialog*/
    private SignUpDialogFragment dialogFragment;

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

    /**
     * classId
     * */
    private int scannedClassId=0;

    public void setScannedClassId(int scannedClassId) {
        this.scannedClassId = scannedClassId;
    }

    /*计时器*/
    private Timer verifyTimer;
    private TimerTask timerTask;
    /*读秒*/
    private short remainSec = 60;
    /*用户类型 0 非注册用户> 1 研修网用户> 2 研修宝用户*/
    public static final int UNRIGISTED_USER = 0;
    public static final int SERVER_USER = 1;
    public static final int APP_USER = 2;
    private int userType = UNRIGISTED_USER;

    public int getUserType() {
        return userType;
    }


    /*手机号*/
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = new PublicLoadLayout(getActivity());
            fragmentRootView = inflater.inflate(R.layout.fragment_checkphone_layout, null);
            mRootView.setContentView(fragmentRootView);
            dialogFragment = new SignUpDialogFragment();
        }
        /*初始化toolbar*/
        toolbarInit(fragmentRootView);
        /*初始化 view*/
        viewInit(fragmentRootView);
        return mRootView;
    }

    private void viewInit(View root) {
        checkPhoneView = root.findViewById(R.id.checkphone_verification_layout);
        phoneEditText = (ClearEditText) checkPhoneView.findViewById(R.id.edt_phone_number);
        verifyCodeEditText = (ClearEditText) checkPhoneView.findViewById(R.id.edt_verification_code);
        getVerifyCodeTextView = (TextView) checkPhoneView.findViewById(R.id.tv_get_verification_code);
        phoneEditText.addTextChangedListener(phoneInputWatcher);
        getVerifyCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRootView.showLoadingView();
                /*检查手机号格式*/
                if (isPhoneNumber(phoneEditText.getText().toString())) {
                    /*启动计时器*/
                    startTimer();
                    /*发起网络请求*/
//                    fadeVerifyCodeRequest(); 本地模拟请求
                    verfifyCodeRequest(phoneEditText.getText().toString(), scannedClassId);
                } else {
                    // TODO: 2018/3/1 提示手机号格式不正确
                    mRootView.hiddenLoadingView();
                }
            }
        });
        dialogInit();
    }
    /**
     * 手机号输入 检查 控制 获取验证码 按钮
     * */
    private TextWatcher phoneInputWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length()>10) {
                enableVerifyCodeBtn();
            }else {
                disableVerifyCodeBtn();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    /**
     * 开启 获取验证的点击功能 以及UI 变化
     * */
    private void enableVerifyCodeBtn(){
        getVerifyCodeTextView.setEnabled(true);
        getVerifyCodeTextView.setTextColor(getActivity().getResources().getColor(R.color.color_1da1f2));
    }

    private void disableVerifyCodeBtn(){
        getVerifyCodeTextView.setEnabled(false);
        getVerifyCodeTextView.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
    }


    /**
     * 取消计时器  退出界面 以及超时时使用
     */
    private void cancelTimer() {
        timerTask.cancel();
        verifyTimer.cancel();
        verifyTimer.purge();
        getVerifyCodeTextView.setEnabled(true);
         /*更改 倒计时颜色*/
        getVerifyCodeTextView.setTextColor(getActivity().getResources().getColor(R.color.color_999999));
        getVerifyCodeTextView.setText("获取验证码");
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        getVerifyCodeTextView.setEnabled(false);
        remainSec = 60;

        verifyTimer = new Timer("verifyTimer");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getVerifyCodeTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (0 <= remainSec) {
                            /*60秒倒计时过程中 不允许再次点击*/
                            getVerifyCodeTextView.setText(remainSec-- + "s");
                        } else {
                            /*获取验证码超时*/
                            cancelTimer();
                        }
                    }
                });
            }
        };
        verifyTimer.schedule(timerTask, 0, 1000);
        /*更改 倒计时颜色*/
        getVerifyCodeTextView.setTextColor(getActivity().getResources().getColor(R.color.color_1da1f2));
    }

    /**
     * 对当前界面进行toolbar 设置
     */
    private void toolbarInit(View root) {
        titleLeftImage = (ImageView) root.findViewById(R.id.title_layout_left_img);
        titleRightText = (TextView) root.findViewById(R.id.title_layout_right_txt);
        titleTextView = (TextView) root.findViewById(R.id.title_layout_title);

        titleRightText.setVisibility(View.VISIBLE);
        titleLeftImage.setVisibility(View.VISIBLE);
//        验证成功之前 不能进入下一步
        disableNextStepBtn();
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
                 /*首先验证 验证码的有效性  因为有网络请求 需要延迟处理回调*/
                if (checkVerifyCode(verifyCodeEditText.getText().toString())) {
                    mRootView.showLoadingView();

                    /*模拟网络请求*/
//                    fadeUserTypeCheckRequest(phoneEditText.getText().toString(),
//                            verifyCodeEditText.getText().toString(), scannedClassId); 本地模拟请求

                    userTypeCheckRequest(phoneEditText.getText().toString(),
                            verifyCodeEditText.getText().toString(), scannedClassId);
                }
            }
        });
    }

    /**
     * 手机号格式检查
     * 若格式不正确要给出提示
     */
    private boolean isPhoneNumber(String number) {
        if (number == null || number.isEmpty()) {
            ToastUtil.showToast(getActivity(), "手机号不能为空！");
            return false;
        }
        if (Utils.isMobileNO(number)) {
            return true;
        } else {
            ToastUtil.showToast(getActivity(), "手机号格式不正确！");
            return false;
        }
    }

    /**
     * 模拟的 验证码请求
     * */
//    private void fadeVerifyCodeRequest(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                /*请求成功的情况*/
//                alertDialog.setMessage("验证码已经发送");
//                alertDialog.show();
//                enableNextStepBtn();
//                mRootView.hiddenLoadingView();
//
////                if (new Random().nextBoolean()) {
////                /*请求失败的情况 */
////                    mRootView.showOtherErrorView("请求失败 提示retry");
////                }else {
////                /*网络失败的额情况*/
////                    mRootView.showNetErrorView();
////                }
////                mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        fadeVerifyCodeRequest();
////                    }
////                });
//
//            }
//        },300);
//    }


    /**
     * 模拟的 账号验证请求
     */
//    private void fadeUserTypeCheckRequest(final String phone, final String verifycode, final int clazsID) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Random random = new Random();
//                int n = random.nextInt(2);
//                if (n == 0) {
//                    /*账号没有注册过 */
//                    phoneNumber=phone;
//                    userType=UNRIGISTED_USER;
//                } else if (n == 1) {
//                    /*重复添加注册 提示重复 跳转登录*/
//                    sysUserBean=new SysUserBean();
//                    sysUserBean.setRealName("中心用户");
//                    sysUserBean.setMobilePhone(phone);
//                    userType=SERVER_USER;
//                }
//                /*隐藏 loading*/
//                mRootView.hiddenLoadingView();
//                if (toolbarActionCallback != null) {
//                    toolbarActionCallback.onRightComponentClick();
//                }
//            }
//        }, 500);
//    }


    /**
     * 网络请求 验证 手机号与验证码
     * 验证成功 则可以进入下一步
     * 需要对手机号进行保存
     */
    private void userTypeCheckRequest(final String phone, final String verifycode, final int clazsID) {

        VerifyCodeConfirmRequest confirmRequest = new VerifyCodeConfirmRequest();
        confirmRequest.clazsId = clazsID+"";
        confirmRequest.mobile = phone;
        confirmRequest.code = verifycode;
        /*验证验证码与手机号  结果只有两种情况 通过和未通过，不需要对UI进行使能更改
        * 只进行必要的信息提示即可*/
        confirmRequest.startRequest(VerifyCodeConfirmResponse.class, new HttpCallback<VerifyCodeConfirmResponse>() {
            @Override
            public void onSuccess(RequestBase request, VerifyCodeConfirmResponse ret) {
                Log.i(TAG, "onSuccess: " + new Gson().toJson(ret));
                mRootView.hiddenLoadingView();
                if (ret.getCode() == 0) {
                    userType = ret.getHasRegistUser();
                    switch (userType) {
                        case UNRIGISTED_USER:
                            /*未注册用户 保存电话号码 准备进行注册操作*/
                            phoneNumber = phone;
                            break;
                        case SERVER_USER:
                            /*用户中心用户 保存 sysuserbean 等待 用户信息设置后进行 保存*/
                            sysUserBean=ret.getSysUser();
                        case APP_USER:
                            /*注册用户 保存 后台会自动对未添加班级的用户进行添加操作  不需要跳转*/
                            alertDialog.setMessage(ret.getError().getMessage());
                            alertDialog.show();
                            break;
                        default:
                            break;
                    }
                    /*回调 给signupactivity 验证成功 执行UI 回调*/
                    if (toolbarActionCallback != null) {
                        toolbarActionCallback.onRightComponentClick();
                    }
                } else {
                    /*验证失败 提示失败信息*/
                    alertDialog.setMessage(ret.getError().getTitle());
                    alertDialog.show();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                // TODO: 2018/3/6 验证失败 弹出提示
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userTypeCheckRequest(phone, verifycode, clazsID);
                    }
                });

            }
        });
    }

    /**
     * 请求获取验证码
     */
    private void verfifyCodeRequest(final String number, final int clazsId) {
        PhoneNumCheckRequest request = new PhoneNumCheckRequest();
        request.clazsId = clazsId+"";
        request.mobile = number;
        request.startRequest(CheckPhoneNumResponse.class, new HttpCallback<CheckPhoneNumResponse>() {
            @Override
            public void onSuccess(RequestBase request, CheckPhoneNumResponse ret) {
                if (ret.getCode() == 0) {
                    /*成功下发了 验证码 开启下一步使能*/
                    enableNextStepBtn();
                    ToastUtil.showToast(getActivity(), "验证码已经发送到您的手机！");
                } else {
                    /*发送验证码失败 关闭 下一步使能*/
                    disableNextStepBtn();
                    mRootView.showOtherErrorView(ret.getError().getMessage());
//                    alertDialog.setMessage(ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                /*网路请求失败 提示 */
                disableNextStepBtn();
                mRootView.showNetErrorView();
                /*设置重试按钮监听*/
                mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        verfifyCodeRequest(number, clazsId);
                    }
                });
            }
        });
    }

    /**
     * 验证用户填写的二维码 有效性 有效 进行下一步的回调 通知activity进行界面跳转
     */
    private boolean checkVerifyCode(String verifyCode) {

        /*首先验证基本格式 空 4位数字等等*/
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtil.showToast(getActivity(), "验证码不能为空");
            return false;
        }
        /*验证码在4~6位*/
        if (verifyCode.length()<4||verifyCode.length()>7){
            ToastUtil.showToast(getActivity(),"验证码格式不正确");
            return false;
        }
        return true;
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

    private CheckVerificationCallback checkVerificationCallback;

    public void setCheckVerificationCallback(CheckVerificationCallback checkVerificationCallback) {
        this.checkVerificationCallback = checkVerificationCallback;
    }

    public interface CheckVerificationCallback {
        void onRequestGetVerifyCode(String phone);
    }

    private AlertDialog alertDialog;

    private void dialogInit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("dialog").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelTimer();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
    }
}
