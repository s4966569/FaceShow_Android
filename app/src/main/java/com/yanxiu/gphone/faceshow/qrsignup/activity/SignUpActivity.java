package com.yanxiu.gphone.faceshow.qrsignup.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.qrsignup.SignUpManager;
import com.yanxiu.gphone.faceshow.qrsignup.ToolbarActionCallback;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.CheckPhoneFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetPasswordFragment;
import com.yanxiu.gphone.faceshow.qrsignup.fragment.SetProfileFragment;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/*用户进行注册时 的activity 主要包含 验证手机号 设置密码 完善资料 三个主要功能 功能界面由不同的fragment实现
* 1、由扫码界面获取结果
* 2、进入当前activity 并加载验证手机号fragment
* 3、点击获取验证码后 发送验证请求 得到下一步需要显示的结果
* */
public class SignUpActivity extends FaceShowBaseActivity{
    private PublicLoadLayout mRootView;
    
//    各种操作的网络请求集中在这个类中
    private SignUpManager mSignUpManager;
  
//    各个步骤的fragment
    private CheckPhoneFragment checkPhoneFragment;
    private SetPasswordFragment setPasswordFragment;
    private SetProfileFragment setProfileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView=new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_sign_up);
        setContentView(mRootView);
        mRootView.showLoadingView();
        mSignUpManager=new SignUpManager();
        viewInit();
    }

    private void fragmentInit(){
        checkPhoneFragment=new CheckPhoneFragment();
        setPasswordFragment=new SetPasswordFragment();
        setProfileFragment=new SetProfileFragment();
        /*第一步 验证手机号的注册情况*/
        checkPhoneFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                // TODO: 2018/3/1  在验证手机号部分点击返回  取消所有请求 并返回到登录界面？还是 扫码界面
                /*此时点击 返回按钮 */
                SignUpActivity.this.finish();
            }

            @Override
            public void onRightComponentClick() {
                // TODO: 2018/3/1 点击下一步 需要验证通过 并且为非注册用户 才有下一步 验证成功以后 才是可点击的
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container,setPasswordFragment).commit();
            }
        });
        
        setPasswordFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                // TODO: 2018/3/1  进入下一步的界面 setProfileFragment
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container,checkPhoneFragment).commit();
            }

            @Override
            public void onRightComponentClick() {
                // TODO: 2018/3/1 返回到上一步？返回到上一界面
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container,setProfileFragment).commit();
            }
        });

        setProfileFragment.setToolbarActionCallback(new ToolbarActionCallback() {
            @Override
            public void onLeftComponentClick() {
                // TODO: 2018/3/1 返回上一步？上一界面
                getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container,setPasswordFragment).commit();
            }

            @Override
            public void onRightComponentClick() {
                // TODO: 2018/3/1 执行保存 网络操作
                ToastUtil.showToast(SignUpActivity.this,"保存");
                           }

        });
    }



    private void viewInit(){
        fragmentInit();
        getSupportFragmentManager().beginTransaction().replace(R.id.signupfragment_container,checkPhoneFragment).commit();
        mRootView.hiddenLoadingView();
    }

   
}
