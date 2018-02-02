package com.yanxiu.gphone.faceshow.homepage.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.orhanobut.logger.Logger;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.course.activity.ClassManagerActivity;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInErrorActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInSuccessActivity;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.http.checkin.UserSignInRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.util.LBSManager;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;

/**
 * 欢迎页面
 * Created by 戴延枫 on 2017/9/14.
 */

public class WelcomeActivity extends FaceShowBaseActivity {

    /**
     * add LOAD_TIME and change time
     * cwq
     */
    private static final int LOAD_TIME = 400;
    private Handler mHandler;
    private final static int GO_LOGIN = 0x0001;
    private final static int GO_MAIN = 0x0002;

    private ImageView mImgLogo;
    private static boolean isAnimationEnd = false;
    private static boolean isCanLogin = false;
    private static boolean isGetUserInfoEnd = false;
    private Animator.AnimatorListener logoAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
            isAnimationEnd = false;
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            isAnimationEnd = true;
            if (isGetUserInfoEnd) {
                whereToGoWithGetUserInfoEnd(WelcomeActivity.this);
            } else if (isCanLogin) {
                LoginActivity.toThisAct(WelcomeActivity.this);
                WelcomeActivity.this.finish();
            }

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
        initView();
        String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};
        requestPermissions(perms, new OnPermissionCallback() {
            @Override
            public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                /*欢迎页logo的动画效果*/
                int ANIMATION_DURATION = 1000;//动画时长
                mImgLogo.animate().translationY(-Utils.dip2px(FaceShowApplication.getContext(), 375)).setDuration(ANIMATION_DURATION).setListener(logoAnimatorListener);
                checkUserStatus();
            }

            @Override
            public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                WelcomeActivity.this.finish();
            }
        });

    }

    private void initMajicWindow() {
        MWConfiguration config = new MWConfiguration(this);
        config.setLogEnable(false);//打开魔窗Log信息
        MagicWindowSDK.initSDK(config);

    }

    /**
     * 当用户有token 并请求UseInfo接口后该往哪跳转
     * 正常是调往首页
     * 当时如果用户是通过scheme进入  就可能会调完签到页
     *
     * @param activity this
     */
    private void whereToGoWithGetUserInfoEnd(WelcomeActivity activity) {
//        if (isAppOpenByScheme()){
//            //目前通过只有签到是通过scheme打开app的
//            toCheckInActOrCheckInResultAct(activity);
//        }else {
        MainActivity.invoke(activity);
        activity.finish();
//        }
    }

    /**
     * 判断app是否为用户通过微信或者浏览器打开 其实就是通过scheme
     *
     * @return boolean
     */
    public boolean isAppOpenByScheme() {
        return Intent.ACTION_VIEW.equals(getIntent().getAction());

    }

    private void initView() {
        mImgLogo = (ImageView) findViewById(R.id.img_logo);
        mHandler = new WelcomeHandler(this);

    }

    /**
     * 检查用户
     */
    private void checkUserStatus() {
        if (TextUtils.isEmpty(SpManager.getToken())) {
            //用户信息不完整,跳转登录页
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, LOAD_TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_MAIN, LOAD_TIME);
        }
    }

    private void toCheckInActOrCheckInResultAct(WelcomeActivity activity) {
        String stepId = "";
        String timestamp = "";
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            Uri uri = getIntent().getData();
            if (uri != null) {
                stepId = uri.getQueryParameter("stepId");
                timestamp = uri.getQueryParameter("timestamp");
            }
        }
        if (!TextUtils.isEmpty(stepId)) {
            toCheckInResultAct(activity, stepId, timestamp);
        } else {
            MainActivity.invoke(activity);
            activity.finish();
        }
    }

    private void toCheckInAct(WelcomeActivity activity) {
        CheckInByQRActivity.toThisAct(activity);
        activity.finish();
    }

    private void toCheckInResultAct(WelcomeActivity activity, String stepId, String timestamp) {
        getLocation(stepId, timestamp, activity);

    }

    private LoadingDialogView mLoadingDialogView;

    private LocationClient getLocation(final String stepId, final String timestamp, final WelcomeActivity activity) {
        if (mLoadingDialogView == null) {
            mLoadingDialogView = new LoadingDialogView(this);
        }
        mLoadingDialogView.show();
        final LocationClient locationClient = LBSManager.getLocationClient();
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                locationClient.unRegisterLocationListener(this);
                locationClient.stop();
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();
                userSignIn(stepId, timestamp, latitude + "," + longitude, bdLocation.getLocationDescribe(), activity);
            }
        });
        locationClient.start();
        return locationClient;
    }

    private void userSignIn(String stepId, String timestamps, @NonNull String position, @NonNull String site, final WelcomeActivity activity) {
        final UserSignInRequest userSignInRequest = new UserSignInRequest();
        userSignInRequest.position = position;
        userSignInRequest.site = site;
        userSignInRequest.stepId = stepId;
        userSignInRequest.timestamp = timestamps;
        userSignInRequest.startRequest(CheckInResponse.class, new HttpCallback<CheckInResponse>() {
            @Override
            public void onSuccess(RequestBase request, CheckInResponse userSignInResponse) {
                mLoadingDialogView.dismiss();
                if (userSignInResponse.getCode() == 0) {
                    CheckInSuccessActivity.toThiAct(activity, userSignInResponse);
                    activity.finish();
                } else {
                    if (userSignInResponse.getError().getCode() == 210414) {
                        //用户已签到
                        CheckInSuccessActivity.toThiAct(activity, userSignInResponse);
                    } else {
                        Intent intent = new Intent(activity, CheckInErrorActivity.class);
                        intent.putExtra(CheckInErrorActivity.QR_STATUE, userSignInResponse.getError());
                        startActivity(intent);
                    }
                    activity.finish();
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mLoadingDialogView.dismiss();
                Intent intent = new Intent(activity, CheckInErrorActivity.class);
                startActivity(intent);
                activity.finish();
            }
        });

    }

    private static class WelcomeHandler extends Handler {

        private WeakReference<WelcomeActivity> mActivity;

        WelcomeHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final WelcomeActivity activity = mActivity.get();

            switch (msg.what) {
                case GO_LOGIN:
                    isCanLogin = true;
                    if (isAnimationEnd) {
                        LoginActivity.toThisAct(activity);
                        activity.finish();
                    }
                    break;
                case GO_MAIN:
                    //获取用户基本信息
                    getCurrentClassId(activity);
//                    ®
                    break;
            }
        }
    }

    private static void getCurrentClassId(final WelcomeActivity activity) {
        if (SpManager.getUserInfo() != null && !TextUtils.isEmpty(SpManager.getUserInfo().getClassId())) {
            getUserInfo(activity);
        } else {
            MainRequest getCurrentClassId = new MainRequest();
            getCurrentClassId.startRequest(MainResponse.class, new HttpCallback<MainResponse>() {
                @Override
                public void onSuccess(RequestBase request, MainResponse ret) {
                    Logger.json(RequestBase.getGson().toJson(ret));
                    if (ret != null && ret.getCode() == 0) {
                        getUserInfo(activity);
                    } else {
                        toNoClassPage(activity);
                    }
                }

                @Override
                public void onFail(RequestBase request, Error error) {
                    //todo
                    isCanLogin = true;
                    if (isAnimationEnd) {
                        LoginActivity.toThisAct(activity);
                        activity.finish();
                    }
                }
            });
        }
    }

    private static void toNoClassPage(WelcomeActivity activity) {
        activity.startActivity(new Intent(activity, ClassManagerActivity.class));
        activity.finish();
    }

    private static void getUserInfo(final WelcomeActivity activity) {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                Logger.json(RequestBase.getGson().toJson(ret));
                if (ret.getCode() == 0) {
                    UserInfo.Info info = ret.getData();
                    info.setClassId(SpManager.getUserInfo().getClassId());
                    info.setClassName(SpManager.getUserInfo().getClassName());
                    info.setProjectName(SpManager.getUserInfo().getProjectName());
                    SpManager.saveUserInfo(info);
                } else {

                }
                if (isAnimationEnd) {
                    activity.whereToGoWithGetUserInfoEnd(activity);
                }
                isGetUserInfoEnd = true;
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                UserInfo.getInstance().setInfo(SpManager.getUserInfo());
                if (isAnimationEnd) {
                    MainActivity.invoke(activity);
                    activity.finish();
                }
                isGetUserInfoEnd = true;

            }
        });
    }


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
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}