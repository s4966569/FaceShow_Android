package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.qrsignup.PhoneNumCheckResponse;
import com.yanxiu.gphone.faceshow.http.qrsignup.PhoneNumberCheckRequest;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.dialog.SignUpDialogFragment;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import java.util.Random;
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


    /*计时器*/
    private Timer verifyTimer;
    private TimerTask timerTask;
    /*读秒*/
    private short remainSec = 60;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = new PublicLoadLayout(getActivity());
//            mRootView.setContentView(R.layout.fragment_checkphone_layout);
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

        getVerifyCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRootView.showLoadingView();
                /*检查手机号格式*/
                if (isPhoneNumber(phoneEditText.getText().toString())) {
                    /*启动计时器*/
                    startTimer();
                    /*发起网络请求*/
                    fadeRequest();
//                    phoneCheckRequest(phoneEditText.getText().toString());
                } else {
                    // TODO: 2018/3/1 提示手机号格式不正确
                    mRootView.hiddenLoadingView();
                }
            }
        });

        dialogInit();
    }
    /**
     * 取消计时器  退出界面 以及超时时使用
     * */
    private void cancelTimer(){
        timerTask.cancel();
        verifyTimer.cancel();
        verifyTimer.purge();
        getVerifyCodeTextView.setEnabled(true);

        getVerifyCodeTextView.setText("获取验证码");
    }
    /**
     * 开始计时
     * */
    private void startTimer() {
        getVerifyCodeTextView.setEnabled(false);
        remainSec=60;
        verifyTimer = new Timer("verifyTimer");
        timerTask = new TimerTask() {
            @Override
            public void run() {
                getVerifyCodeTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (0 <= remainSec--) {
                            /*60秒倒计时过程中 不允许再次点击*/
                            getVerifyCodeTextView.setText(remainSec + "s");
                        }else {
                            /*获取验证码超时*/
                            cancelTimer();
                        }
                    }
                });
            }
        };
        verifyTimer.schedule(timerTask, 1000, 1000);
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
                 checkVerifyCode(verifyCodeEditText.getText().toString());
//                 在网络 结果中调用
//                if (toolbarActionCallback != null) {
//                    toolbarActionCallback.onRightComponentClick();
//                }
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
     * fade request
     */
    private void fadeRequest() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int n = random.nextInt(3);
                if (n == 0) {
                    /*账号没有注册过 开始请求验证码 */
                    enableNextStepBtn();

                } else if (n == 1) {
                    /*重复添加注册 提示重复 跳转登录*/
                    disableNextStepBtn();
                    alertDialog.setMessage("已经添加了该课程 请登录查看");
                    alertDialog.show();
                } else if (n == 2) {
                    /*账号存在未添加课程 后台执行添加课程操作 成功后提示跳转登录*/
                    disableNextStepBtn();
                    alertDialog.setMessage("正在添加课程 添加课程成功 返回登录");
                    alertDialog.show();
                }
                /*隐藏 loading*/
                mRootView.hiddenLoadingView();
                /*取消计时*/
                cancelTimer();
            }
        }, 2800);
    }

    /**
     * 网络请求 检查手机号
     */
    private void phoneCheckRequest(String number) {
        /*创建 请求对象*/
        PhoneNumberCheckRequest request = new PhoneNumberCheckRequest();
        checkPhoneNumRequestUUID = request.startRequest(PhoneNumCheckResponse.class, new HttpCallback<PhoneNumCheckResponse>() {
            @Override
            public void onSuccess(RequestBase request, PhoneNumCheckResponse ret) {
                Log.i(TAG, "onSuccess: ");
                mRootView.hiddenLoadingView();
                enableNextStepBtn();
                ToastUtil.showToast(getActivity(), "手机号验证成功");
                // TODO: 2018/3/1  验证成功 还要判断 是那种类型
                alertDialog.setMessage("您已经在此班级中，请直接登录");
//                 TODO: 2018/3/5 如果为非注册用户 验证成功后 需要禁止编辑电话号
//                phoneEditText.setEnabled(false);

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                Log.i(TAG, "onFail: ");
                mRootView.hiddenLoadingView();
                // TODO: 2018/3/1 验证失败  没有进行注册的情况吧
            }
        });
    }


    /**
     * 通知 用户 验证码已经下发 等待验证 后进行下一步
     * */

    /**
     * 验证用户填写的二维码 有效性 有效 进行下一步的回调 通知activity进行界面跳转
     * */
    private boolean checkVerifyCode(String verifyCode){
        // TODO: 2018/3/5  判断 验证码 格式 并执行网络请求
        /*首先验证基本格式 空 4位数字等等*/
        if (toolbarActionCallback != null) {
            toolbarActionCallback.onRightComponentClick();
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
