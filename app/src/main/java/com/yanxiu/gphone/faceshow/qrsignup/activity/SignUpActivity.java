package com.yanxiu.gphone.faceshow.qrsignup.activity;

import android.os.Bundle;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SignUpManager;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.dialog.SignUpDialogFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.CheckPhoneFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetPasswordFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetProfileFragment;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/*用户进行注册时 的activity 主要包含 验证手机号 设置密码 完善资料 三个主要功能 功能界面由不同的fragment实现
* 1、由扫码界面获取结果
* 2、进入当前activity 并加载验证手机号fragment
* 3、点击获取验证码后 发送验证请求 得到下一步需要显示的结果
* */
public class SignUpActivity extends FaceShowBaseActivity {
    private PublicLoadLayout mRootView;

    /*各种操作的网络请求集中在这个类中*/
    private SignUpManager mSignUpManager;

    /*各个步骤的fragment*/
    private CheckPhoneFragment checkPhoneFragment;
    private SetPasswordFragment setPasswordFragment;
    private SetProfileFragment setProfileFragment;
    /*dialog Fragment*/
    private SignUpDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_sign_up);
        setContentView(mRootView);
        mRootView.showLoadingView();
        mSignUpManager = new SignUpManager();
        viewInit();
    }

    private void fragmentInit() {
        checkPhoneFragment = new CheckPhoneFragment();
        setPasswordFragment = new SetPasswordFragment();
        setProfileFragment = new SetProfileFragment();

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
                if (checkPhoneFragment.getUserType()==0) {
                    /*非注册用户 进入密码设置 进行注册*/
                    setFragment(setPasswordFragment);
                /*传递电话号码*/
                    setPasswordFragment.setPhoneNumber(checkPhoneFragment.getPhoneNumber());
                }else if(checkPhoneFragment.getUserType()==1){
                    /*研修网用户 进入用户信息设置*/
                   ;
                    setFragment(setProfileFragment);
                    setProfileFragment.showNotice();
                }else if (checkPhoneFragment.getUserType()==2){
                    /*研修宝APP 用户 提示返回登录*/
                }
            }
        });
        /*第二步 在设置密码页面 点击 返回与下一步*/
        setPasswordFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                setFragment(checkPhoneFragment);
            }

            @Override
            public void onRightComponentClick() {
                /*正常注册新用户 不显示研修网账号提示*/

                setFragment(setProfileFragment);
                setProfileFragment.hideNotice();
            }
        });
        /*第三步 在信息设置界面 点击返回与保存*/
        setProfileFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                setFragment(setPasswordFragment);
            }

            @Override
            public void onRightComponentClick() {
                ToastUtil.showToast(SignUpActivity.this, "保存");
            }

        });
    }

    private void setFragment(FaceShowBaseFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container, fragment).commit();
    }


    private void viewInit() {
        dialogFragment = new SignUpDialogFragment();
        fragmentInit();
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container, checkPhoneFragment).commit();
        mRootView.hiddenLoadingView();
    }


}
