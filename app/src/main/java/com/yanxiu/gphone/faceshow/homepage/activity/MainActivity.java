package com.yanxiu.gphone.faceshow.homepage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoRequest;
import com.yanxiu.gphone.faceshow.http.login.GetUserInfoResponse;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;

import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.ActivityManger;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.UpdateUtil;

import java.util.UUID;

public class MainActivity extends FaceShowBaseActivity implements View.OnClickListener {

    private PublicLoadLayout mRootView;

    private final int mNavBarViewsCount = 4;
    private View[] mNavBarViews = new View[mNavBarViewsCount];
    private ImageView[] mNavIconViews = new ImageView[mNavBarViewsCount];
    private TextView[] mNavTextViews = new TextView[mNavBarViewsCount];
    private int mNormalNavTxtColor, mSelNavTxtColor;

    private final int INDEX_HOME_TAB = 0;//首页tab
    private final int INDEX_NOTICE_TAB = 1;//通知tab
    private final int INDEX_CLASSCIRCLE_TAB = 2;//班级圈tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

    private View mBottomView;
    private ImageView mRedCircle;
    public NaviFragmentFactory mNaviFragmentFactory;
    public FragmentManager mFragmentManager;

    public MainBean mMainData;

    private UUID mGetHasNotificationsNeedReadRequestUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_main);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        initView();
        initListener();
        getUserInfo();
        UpdateUtil.Initialize(this,false);
    }

    private void getUserInfo() {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.startRequest(GetUserInfoResponse.class, new HttpCallback<GetUserInfoResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetUserInfoResponse ret) {
                if (ret.getCode()==0) {
                    UserInfo.getInstance().setInfo(ret.getData());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {

            }
        });
        requestData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initView() {
        mBottomView = findViewById(R.id.navi_switcher);
        initBottomBar();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory();
        showCurrentFragment(0);

    }

    private void initListener() {
    }

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_336600);
        mNormalNavTxtColor = getResources().getColor(R.color.color_999999);
        mNavBarViews[0] = findViewById(R.id.navi_1);
        mNavBarViews[1] = findViewById(R.id.navi_2);
        mNavBarViews[2] = findViewById(R.id.navi_3);
        mNavBarViews[3] = findViewById(R.id.navi_4);
        for (int i = 0; i < mNavBarViews.length; i++) {
            mNavBarViews[i].setOnClickListener(this);
            mNavIconViews[i] = (ImageView) mNavBarViews[i].findViewById(R.id.nav_icon);
            mNavTextViews[i] = (TextView) mNavBarViews[i].findViewById(R.id.nav_txt);
        }
        mNavIconViews[0].setEnabled(false);
        mRedCircle = (ImageView) findViewById(R.id.img_red_circle);
        pollingRedPointer();
    }

    private void requestData() {
        mRootView.showLoadingView();
        MainRequest mainRequest = new MainRequest();
        mainRequest.startRequest(MainResponse.class, new HttpCallback<MainResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getCode() == 0) {
                    if (ret.getData() == null) {
                        mRootView.showOtherErrorView();
                    } else {
                        mMainData = ret.getData();
                        initFragment();
                    }
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();

            }
        });

    }

    /**
     * 轮询小红点
     */
    private void pollingRedPointer() {
        getRedPointersRequest();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                getRedPointersRequest();
            }
        }
    };

    private void getRedPointersRequest() {
        GetHasNotificationsNeedReadRequest getHasNotificationsNeedReadRequest = new GetHasNotificationsNeedReadRequest();
        mGetHasNotificationsNeedReadRequestUUID = getHasNotificationsNeedReadRequest.startRequest(GetHasNotificationsNeedReadResponse.class, new HttpCallback<GetHasNotificationsNeedReadResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetHasNotificationsNeedReadResponse ret) {
                if (ret.getCode() == 0 && ret.getData().isHasUnView()) {
                    if (mRedCircle.getVisibility() == View.INVISIBLE) {
                        mRedCircle.setVisibility(View.VISIBLE);
                    }
                } else {
                }
                handler.sendEmptyMessageDelayed(1, 30000);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                handler.sendEmptyMessageDelayed(1, 30000);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.navi_1:
                curItem = INDEX_HOME_TAB;
                mNavIconViews[0].setEnabled(false);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_2:
                curItem = INDEX_NOTICE_TAB;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(false);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_3:
                curItem = INDEX_CLASSCIRCLE_TAB;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.navi_4:
                curItem = INDEX_MY;
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(true);
                mNavIconViews[3].setEnabled(false);
                break;
            case R.id.title_layout_right_img:
                ToastUtil.showToast(getApplicationContext(), "扫描");
                break;
            case R.id.retry_button:
                requestData();
                break;
            default:
                break;
        }
        if (mNaviFragmentFactory != null && mNaviFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }

    private void checkBottomBar(int index) {
        if (index >= 0 && index < mNavBarViews.length) {
            for (int i = 0; i < mNavBarViews.length; i++) {
                if (i == index) {
                    mNavTextViews[index].setTextColor(mSelNavTxtColor);
                } else {
                    mNavTextViews[i].setTextColor(mNormalNavTxtColor);
                }
            }
        }
    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        mLastSelectIndex = index;
        checkBottomBar(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mNaviFragmentFactory.hideAndShowFragment(mFragmentManager, index);
    }

    /**
     * 退出间隔时间戳
     */
    private long mBackTimestamp = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if (System.currentTimeMillis() - mBackTimestamp <= 2000) {
                //Todo 退出程序
                ActivityManger.destoryAll();
            } else {
                mBackTimestamp = System.currentTimeMillis();
                Toast.makeText(this, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 跳转MainActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public void setBottomVisibility(int visibility) {
        if (mBottomView != null) {
            mBottomView.setVisibility(visibility);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mGetHasNotificationsNeedReadRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetHasNotificationsNeedReadRequestUUID);
        }
        // TODO: 17-9-21 移除轮讯
        handler.removeCallbacksAndMessages(null);
    }

}
