package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.request.UpdateProfileRequest;
import com.yanxiu.gphone.faceshow.qrsignup.response.UpdateProfileResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 * 完善用户信息界面
 * 只有在验证的手机号为非注册用户的时候才会进入这个界面
 */
public class SetProfileFragment extends FaceShowBaseFragment implements View.OnClickListener {

    private View fragmentRootView;
    private PublicLoadLayout mRootView;
    /**
     * 用户信息  新注册用户信息内容 大部分为空  已有信息用户 有信息内容
     */
    private SysUserBean sysUserBean;
    /**
     * 目标班级名称
     * */
    public String clazsName;



    /**
     * toolbar 控件
     */
    private ImageView titleLeftImage;
    private TextView titleRightText;
    private TextView titleTextView;
    /**
     * 自定义的 toolbar 点击监听 包含 左侧 控件 点击与右侧控件点击 事件回调
     */
    private ToolbarActionCallback toolbarActionCallback;
    /**界面内的主要控件*/
    /**
     * 用户信息 头像等
     */
    private View personalView;

    private View nameItem;
    private View phoneItem;
    private View sexItem;
    private View stageItem;

    /**
     * 控制notice显示
     */
    private boolean showNotice = false;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetProfileFragment() {
        // Required empty public constructor
    }

    /**
     * 设置显示信息
     */
    public void setSysUserBean(SysUserBean userBean) {
        this.sysUserBean = userBean;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = new PublicLoadLayout(getActivity());
            fragmentRootView = inflater.inflate(R.layout.fragment_setprofile_layout, null);
            mRootView.setContentView(fragmentRootView);
        }
        toolbarInit(fragmentRootView.findViewById(R.id.setprofile_titelbar));
        viewInit(fragmentRootView);
        setUserData(sysUserBean);
        return mRootView;
    }

    /**
     * 显示 用户中心账号 提示
     */
    public void showNotice() {
        showNotice = true;
    }

    /**
     * 隐藏 用户中心账号提示
     */
    public void hideNotice() {
        showNotice = false;
    }


    /**
     * profile 设置相关的各个控件初始化
     */
    private void viewInit(View root) {
        View profileView = root.findViewById(R.id.profile_layout);

        personalView = profileView.findViewById(R.id.person_info);

        nameItem = profileView.findViewById(R.id.rl_name);
        sexItem = profileView.findViewById(R.id.rl_sex);
        stageItem = profileView.findViewById(R.id.rl_stage_subject);
        phoneItem = profileView.findViewById(R.id.rl_phone);

        /*这里要隐藏 personal 部分*/
        personalView.setVisibility(View.GONE);

        nameItem.setOnClickListener(this);
        sexItem.setOnClickListener(this);
        stageItem.setOnClickListener(this);
        phoneItem.setOnClickListener(this);
        phoneItem.setOnClickListener(this);

        root.findViewById(R.id.notice_tv).setVisibility(showNotice ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置 用户信息到UI
     */
    private void setUserData(SysUserBean userData) {

        TextView nameTv = mRootView.findViewById(R.id.profile_layout)
                .findViewById(R.id.tv_name);
        nameTv.setText(String.format("%s", TextUtils.isEmpty(userData.getRealName()) ? "" : userData.getRealName()));

        TextView phoneTv = mRootView.findViewById(R.id.profile_layout)
                .findViewById(R.id.tv_phone);
        phoneTv.setText(String.format("%s", userData.getMobilePhone()));

        TextView sexTv = mRootView.findViewById(R.id.profile_layout)
                .findViewById(R.id.tv_gender);
        sexTv.setText(String.format("%s", TextUtils.isEmpty(userData.getSexName()) ? "" : userData.getSexName()));

        TextView stagetSunjectTv = mRootView.findViewById(R.id.profile_layout)
                .findViewById(R.id.tv_stage_subject);

        StringBuilder stageSubject = new StringBuilder();
        /*拼接 学段*/
        stageSubject.append(!TextUtils.isEmpty(userData.getStageName()) ? userData.getStageName() : "");
        stageSubject.append(TextUtils.isEmpty(userData.getSubjectName()) ? "" : "、" + userData.getSubjectName());
        stagetSunjectTv.setText(String.format("%s", stageSubject.toString()));
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
        titleTextView.setText("完善资料");
        titleRightText.setText("保存");
        titleLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toolbarActionCallback != null) {
                    toolbarActionCallback.onLeftComponentClick();
                }
            }
        });
        /*点击保存*/
        titleRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fadeUpdataProfileRequest();
                updateSysUserInfoRequest(sysUserBean);
            }
        });
    }

 /*   *//**
     * 模拟上传用户信息 网络请求
     * *//*
//    private void fadeUpdataProfileRequest() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ToastUtil.showToast(getActivity(), "用户信息已经上传！");
//            }
//        }, 400);
//    }*/

    /**
     * 上传 用户信息到服务器
     */
    private void updateSysUserInfoRequest(final SysUserBean userBean) {
        final UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();

//        Log.i(TAG, "updateSysUserInfoRequest:  " +new Gson().toJson(userBean));
        updateProfileRequest.userId = userBean.getUserId() + "";
        updateProfileRequest.realName = userBean.getRealName();
        updateProfileRequest.sex = userBean.getSex() + "";
        updateProfileRequest.stage = userBean.getStage() + "";
        updateProfileRequest.subject = userBean.getSubject() + "";

        updateProfileRequest.startRequest(UpdateProfileResponse.class, new HttpCallback<UpdateProfileResponse>() {
            @Override
            public void onSuccess(RequestBase request, UpdateProfileResponse ret) {
                mRootView.hiddenLoadingView();
//                Log.i(TAG, "onSuccess: "+new Gson().toJson(ret));
                if (ret.getCode() == 0) {
                    /*这里对 服务器没有返回班级名的情况尽心给一个处理*/
                    StringBuilder message=new StringBuilder();
                    message.append("成功加入");
                    if (!TextUtils.isEmpty(clazsName)) {
                        message.append("【"+clazsName+"】!");
                    }else {
                        message.append("班级!");
                    }
                    message.append("\n登录后即可查看到该班级。");

                    createNomalDialog(message.toString(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            getActivity().setResult(Activity.RESULT_OK);
                            getActivity().finish();
                        }
                    });
                } else {
                    ToastUtil.showToast(getActivity(), getErrorMsg(ret));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateSysUserInfoRequest(userBean);
                    }
                });
            }
        });
    }

    /**
     * 根据返回值 获取错误信息
     */
    private void setErrorMsg(UpdateProfileResponse ret) {
        if (ret.getError() != null) {
            /*首先检查 是否携带错误信息*/
            ToastUtil.showToast(getActivity(), ret.getError().getMessage());
//            alertDialog.setMessage();
        } else {
            /*没有包含错误信息*/
            if (!TextUtils.isEmpty(ret.getMessage())) {
                ToastUtil.showToast(getActivity(), ret.getMessage());
            } else {
                ToastUtil.showToast(getActivity(), "请求失败");
            }
        }
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

    @Override
    public void onClick(View v) {
        if (itemClickListener != null) {
            switch (v.getId()) {
                case R.id.rl_name:
                    itemClickListener.onNameItemClicked();
                    break;
                case R.id.rl_sex:
                    itemClickListener.onSexItemClicked();
                    break;
                case R.id.rl_stage_subject:
                    itemClickListener.onStageItemClicked();
                    break;
                case R.id.rl_phone:
                    itemClickListener.onPhoneItemClicked();
                    break;
                default:
                    break;
            }
        }
    }


    private AlertDialog alertDialog;


    private void createNomalDialog(String msg, DialogInterface.OnClickListener btnListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(msg).setPositiveButton("确定", btnListener);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private ProfileItemClickListener itemClickListener;

    public void setItemClickListener(ProfileItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ProfileItemClickListener {
        void onNameItemClicked();

        void onPhoneItemClicked();

        void onSexItemClicked();

        void onStageItemClicked();
    }
}
