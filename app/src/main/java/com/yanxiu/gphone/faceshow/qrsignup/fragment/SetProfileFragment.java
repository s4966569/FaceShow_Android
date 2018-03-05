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
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.user.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * 完善用户信息界面
 * 只有在验证的手机号为非注册用户的时候才会进入这个界面
 */
public class SetProfileFragment extends FaceShowBaseFragment implements View.OnClickListener {

    private View fragmentRootView;
    private PublicLoadLayout mRootView;
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


    public void setToolbarActionCallback(ToolbarActionCallback toolbarActionCallback) {
        this.toolbarActionCallback = toolbarActionCallback;
    }

    public SetProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = new PublicLoadLayout(getActivity());
//            mRootView.setContentView(R.layout.fragment_checkphone_layout);
            fragmentRootView = inflater.inflate(R.layout.fragment_setprofile_layout, null);
            mRootView.setContentView(fragmentRootView);
        }
        toolbarInit(fragmentRootView.findViewById(R.id.setprofile_titelbar));
        viewInit(fragmentRootView);
        return mRootView;
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
                /*进入 姓名设置 activity*/
                intent=new Intent(getActivity(), ProfileActivity.class);
//                getActivity().startActivityForResult(new Intent(getActivity(),));
                break;
            case R.id.rl_phone:
                break;
            case R.id.rl_sex:
                break;
            case R.id.rl_stage_subject:
                break;
            default:
                break;

        }

    }
}
