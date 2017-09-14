package com.yanxiu.gphone.faceshow.homepage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        mRootView = (RelativeLayout) findViewById(R.id.root_view);
        mHander = new WelcomeHandler(this);
        checkUserStatus();
    }

    /**
     * 检查用户状态
     */
    private void checkUserStatus() {
        //TODO @荣成 判断用户信息是否登录
        if (false) {
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
            WelcomeActivity activity = mActivity.get();

            switch (msg.what) {
                case GO_LOGIN:
                    //TODO  @荣成 登录页
//                    LoginActivity.LaunchActivity(activity);
//                    activity.finish();
                    break;
                case GO_MAIN:
                    //进入首页
                    MainActivity.invoke(activity);
                    activity.finish();
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