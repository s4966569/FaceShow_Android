package com.yanxiu.gphone.faceshow.homepage.activity;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.login.SignInRequest;
import com.yanxiu.gphone.faceshow.http.login.SignInResponse;
import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;

import java.lang.ref.WeakReference;

/**
 * Created by 戴延枫 on 2017/9/14.
 */

public class WelcomeActivity extends FaceShowBaseActivity {

    /**
     * add LOAD_TIME and change time
     * cwq
     */
    private static final int LOAD_TIME = 400;

    private RelativeLayout mRootView;

    private Handler mHander;
    private final static int GO_LOGIN = 0x0001;
    private final static int GO_MAIN = 0x0002;

    private Context mContext;
    private ImageView mImgLogo;

    private Animator.AnimatorListener logoAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            checkUserStatus();
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;
        initView();
        /*欢迎页logo的动画效果*/
        mImgLogo.animate().translationY(-800).setDuration(1000).setListener(logoAnimatorListener);
    }

    private void initView() {
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mImgLogo = (ImageView) findViewById(R.id.img_logo);
        mHander = new WelcomeHandler(this);

    }

    /**
     * 检查用户
     */
    private void checkUserStatus() {
        //TODO @荣成 判断用户信息是否登录
        if (!SpManager.isLogined()) {
            //用户信息不完整,跳转登录页
            mHander.sendEmptyMessageDelayed(GO_LOGIN, LOAD_TIME);
        } else {
            //用户信息完整，跳转首页
            mHander.sendEmptyMessageDelayed(GO_MAIN, LOAD_TIME);
        }
    }

    private static class WelcomeHandler extends Handler {

        private WeakReference<WelcomeActivity> mActivity;

        public WelcomeHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WelcomeActivity activity = mActivity.get();

            switch (msg.what) {
                case GO_LOGIN:
                    //TODO  @荣成 登录页
                    LoginActivity.toThisAct(activity);
                    activity.finish();
                    break;
                case GO_MAIN:
                    //进入首页
                    // TODO: 17-9-19 此处需要个根据token登录的接口
                    SignInRequest signInRequest = new SignInRequest();
                    signInRequest.startRequest(SignInResponse.class, new HttpCallback<SignInResponse>() {
                        @Override
                        public void onSuccess(RequestBase request, SignInResponse ret) {
                            if (ret.getCode() == 0) {
                                UserInfo.getInstance().setInfo(ret.getData());
                                MainActivity.invoke(activity);
                                activity.finish();
//                                Toast.makeText(activity, ret.getError().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, ret.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                activity.finish();
                            }
                        }

                        @Override
                        public void onFail(RequestBase request, Error error) {
                            Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                            activity.finish();

                        }
                    });

                    break;
            }
        }
    }

    ;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出程序
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        mHander.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}