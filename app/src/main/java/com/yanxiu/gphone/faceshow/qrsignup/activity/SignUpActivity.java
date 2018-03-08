package com.yanxiu.gphone.faceshow.qrsignup.activity;

import android.content.Intent;
import android.os.Bundle;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.CheckPhoneFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetPasswordFragment;

/**
 * 注册Activity 负责 扫码过程中的新用户注册
 * 主要 包含两个Fragment {@link CheckPhoneFragment} 与 {@link SetPasswordFragment}
 * 分别对 手机号码进行检查 用户类型进行区分 与 设置用户密码并进行用户注册
 * 对应两种个情况  新用户 会要求设置密码
 * 用户中心已有用户 会直接进入信息设置{@link ModifySysUserActivity}
 */
public class SignUpActivity extends FaceShowBaseActivity {
    private PublicLoadLayout mRootView;

    private SysUserBean sysUserBean;

    /**
     * classId
     */
    private int scannedClassId = 0;

    public void setScannedClassId(int scannedClassId) {
        this.scannedClassId = scannedClassId;
    }

    /*各个步骤的fragment*/
    private CheckPhoneFragment checkPhoneFragment;
    private SetPasswordFragment setPasswordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_sign_up);
        setContentView(mRootView);
        mRootView.showLoadingView();
        viewInit();
    }

    private void fragmentInit() {
        checkPhoneFragment = new CheckPhoneFragment();
        setPasswordFragment = new SetPasswordFragment();
        /*记录 扫描的clazsId*/
        scannedClassId = getIntent().getBundleExtra("data").getInt("clazsId");
        checkPhoneFragment.setScannedClassId(scannedClassId);
        setPasswordFragment.setScannedClassId(scannedClassId);
        /*第一步 在验证手机号码以及验证码页面 点击返回与下一步 */
        checkPhoneFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                /*此时点击 返回按钮 退回到扫码页面 */
                SignUpActivity.this.finish();
            }

            @Override
            public void onRightComponentClick() {
                /*点击下一步 */
                switch (checkPhoneFragment.getUserType()) {
                    case CheckPhoneFragment.UNRIGISTED_USER:
                         /*非注册用户 进入密码设置 进行注册*/
                        setFragment(setPasswordFragment);
                     /*传递电话号码 到 setpasswordFragment 生成sysuser*/
                        setPasswordFragment.setPhoneNumber(checkPhoneFragment.getPhoneNumber());
                        break;
                    case CheckPhoneFragment.SERVER_USER:
                          /*研修网用户 进入用户信息设置 直接获取了 sysuser */
                        sysUserBean = checkPhoneFragment.getSysUserBean();
                        toProfileActivity(CheckPhoneFragment.SERVER_USER);
                        break;
                    case CheckPhoneFragment.APP_USER:
                        /*不需要操作等待用户返回*/
                        break;
                    default:
                        break;
                }
            }
        });
        /*第二步 在设置密码页面 点击 返回与下一步*/
        setPasswordFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                /*进入密码设置页面 只有一种情况 不需要进行判断*/
                setFragment(checkPhoneFragment);
            }

            @Override
            public void onRightComponentClick() {
                /*正常注册新用户 不显示研修网账号提示 获取注册后返回的sysuser*/
                sysUserBean = setPasswordFragment.getSysUserBean();

                toProfileActivity(CheckPhoneFragment.UNRIGISTED_USER);

            }
        });
    }

    /*将已经通过注册 或 用户中心获取的 信息传递到 信息设置界面*/
    private void toProfileActivity(int userType) {
        Intent intent = new Intent(SignUpActivity.this, ModifySysUserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", sysUserBean);
        bundle.putInt("type", userType);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }


    private void setFragment(FaceShowBaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container, fragment).commit();
    }


    private void viewInit() {
        fragmentInit();
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container, checkPhoneFragment).commit();
        mRootView.hiddenLoadingView();
    }


}
