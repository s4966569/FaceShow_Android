package com.yanxiu.gphone.faceshow.qrsignup.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.user.ModifyUserNameActivity;
import com.yanxiu.gphone.faceshow.user.ModifyUserSexActivity;
import com.yanxiu.gphone.faceshow.user.ModifyUserStageActivity;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * 完善用户信息界面
 * 只有在验证的手机号为非注册用户的时候才会进入这个界面
 */
public class SetProfileFragment extends FaceShowBaseFragment implements View.OnClickListener {

    private View fragmentRootView;
    private PublicLoadLayout mRootView;
    /*用户信息  新注册用户信息内容 大部分为空  已有信息用户 有信息内容*/
    private SysUserBean sysUserBean;

    /*toolbar 控件*/
    private ImageView titleLeftImage;
    private TextView titleRightText;
    private TextView titleTextView;
    /*自定义的 toolbar 点击监听 包含 左侧 控件 点击与右侧控件点击 事件回调*/
    private ToolbarActionCallback toolbarActionCallback;
    /*界面内的主要控件*/
    /*用户信息 头像等*/
    private View personalView;
    /*课程信息*/


    private View nameItem;
    private View phoneItem;
    private View sexItem;
    private View stageItem;

    /*控制notice显示*/
    private boolean showNotice=false;

    /*request code*/
    private final int MODIFY_NAME = 0X01;
    private final int MODIFY_SEX = 0X02;
    private final int MODIFY_STAGE_SUBJECT = 0X03;

    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetProfileFragment() {
        // Required empty public constructor
    }

    public void setSysUserBean(SysUserBean userBean){
        this.sysUserBean=userBean;
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
        return mRootView;
    }


    public void showNotice(){
        showNotice=true;
    }

    public void hideNotice(){
        showNotice=false;
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

        root.findViewById(R.id.notice_tv).setVisibility(!showNotice?View.VISIBLE:View.GONE);
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
        titleTextView.setText("设置密码");
        titleRightText.setText("保存");
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

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()) {
            case R.id.rl_name:
                //修改用户名
                startActivityForResult(new Intent(getActivity(), ModifyUserNameActivity.class), MODIFY_NAME);
                break;
            case R.id.rl_sex:
                //修改用户性别
                startActivityForResult(new Intent(getActivity(), ModifyUserSexActivity.class), MODIFY_SEX);
                break;
            case R.id.rl_stage_subject:
                //修改用户学段学科
                EventUpdate.onChooseStageSubjectButton(getActivity());
                startActivityForResult(new Intent(getActivity(), ModifyUserStageActivity.class), MODIFY_STAGE_SUBJECT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case MODIFY_NAME:
                    break;
                case MODIFY_SEX:
                    break;
                case MODIFY_STAGE_SUBJECT:
                    break;
                default:
                    break;
            }
        }
    }
}
