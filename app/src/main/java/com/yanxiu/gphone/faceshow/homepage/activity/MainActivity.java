package com.yanxiu.gphone.faceshow.homepage.activity;

import android.content.Context;
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
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.classcircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.getui.ToMainActivityBroadcastReceiver;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.homepage.fragment.HomeFragment;
import com.yanxiu.gphone.faceshow.http.base.ResponseConfig;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.http.main.RedDotRequest;
import com.yanxiu.gphone.faceshow.http.main.RedDotResponse;
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
    //推送过来的消息需要定位到第一页
    private boolean isToFirstFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_main);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        initView();
        initListener();
        requestData();
        UpdateUtil.Initialize(this, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isToFirstFragment) {
            mNavIconViews[0].setEnabled(false);
            mNavIconViews[1].setEnabled(true);
            mNavIconViews[2].setEnabled(true);
            mNavIconViews[3].setEnabled(true);
            showCurrentFragment(0);
            isToFirstFragment = false;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isToFirstFragment = intent.getBooleanExtra(ToMainActivityBroadcastReceiver.IS_TO_FIRST_FRAGMENT, false);
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

    private ImageView mImgClassCircleRedCircle;

    private void initBottomBar() {
        mSelNavTxtColor = getResources().getColor(R.color.color_1da1f2);
        mNormalNavTxtColor = getResources().getColor(R.color.color_a1a8b2);
        mImgClassCircleRedCircle = (ImageView) findViewById(R.id.img_class_circle_red_circle);
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

    }

    private void requestData() {
        mRootView.showLoadingView();
        MainRequest mainRequest = new MainRequest();
        mainRequest.startRequest(MainResponse.class, new HttpCallback<MainResponse>() {
            @Override
            public void onSuccess(RequestBase request, MainResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0 && ret.getData() != null && ret.getData().getClazsInfo() != null
                        && ret.getData().getProjectInfo() != null) {
                    mMainData = ret.getData();
                    UserInfo.Info info = UserInfo.getInstance().getInfo();
                    info.setClassId(mMainData.getClazsInfo().getId());
                    SpManager.saveUserInfo(info);
                    initFragment();
                    pollingRedPointer();
                } else {
                    mRootView.showOtherErrorView(ret.getError().getMessage());
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
        getRedDotRequest();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                getRedPointersRequest();
            } else if (msg.what == 2) {
                getRedDotRequest();
            }
        }
    };

    private void getRedPointersRequest() {
        GetHasNotificationsNeedReadRequest getHasNotificationsNeedReadRequest = new GetHasNotificationsNeedReadRequest();
        getHasNotificationsNeedReadRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mGetHasNotificationsNeedReadRequestUUID = getHasNotificationsNeedReadRequest.startRequest(GetHasNotificationsNeedReadResponse.class, new HttpCallback<GetHasNotificationsNeedReadResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetHasNotificationsNeedReadResponse ret) {
                if (ret.getCode() == 0) {
                    if (ret.getData().isHasUnView()) {
                        if (mRedCircle.getVisibility() == View.INVISIBLE) {
                            mRedCircle.setVisibility(View.VISIBLE);
                        }
                    } else {
                        hideNoticeRedDot();
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

    public void hideNoticeRedDot() {
        if (mRedCircle.getVisibility() == View.VISIBLE) {
            mRedCircle.setVisibility(View.INVISIBLE);
        }
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
                if (mNaviFragmentFactory.getNoticeFragment() != null) {
                    mNaviFragmentFactory.getNoticeFragment().toRefresh();
                }
                break;
            case R.id.navi_3:
                curItem = INDEX_CLASSCIRCLE_TAB;
                if (mImgClassCircleRedCircle.getVisibility() == View.VISIBLE) {
                    mImgClassCircleRedCircle.setVisibility(View.GONE);
                }
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                ClassCircleFragment classCircleFragment = mNaviFragmentFactory.getClassCircleFragment();
                if (classCircleFragment != null && !classCircleFragment.firstEnter && showCommentRedDot) {
                    showCommentRedDot = false;
                    mNaviFragmentFactory.getClassCircleFragment().toRefresh();
                }
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

    private FaceShowBaseFragment mCurrentFragment;

    private FaceShowBaseFragment showCurrentFragment(int index) {
        if (index == mLastSelectIndex && !isToFirstFragment) {
            return mCurrentFragment;
        }
        mLastSelectIndex = index;
        checkBottomBar(index);
        if (mNaviFragmentFactory == null) {
            mNaviFragmentFactory = new NaviFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        mCurrentFragment = mNaviFragmentFactory.hideAndShowFragment(mFragmentManager, index);
        return mCurrentFragment;
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
    public static void invoke(Context activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    public void setBottomVisibility(int visibility) {
        if (mBottomView != null) {
            mBottomView.setVisibility(visibility);
            findViewById(R.id.line).setVisibility(visibility);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private UUID mGetRedDotRequestUUID;
    private boolean showResourceRedDot = false;
    private boolean showTaskRedDot = false;
    private boolean showCommentRedDot = false;

    private void getRedDotRequest() {
        RedDotRequest redDotRequest = new RedDotRequest();
        redDotRequest.clazsId = SpManager.getUserInfo().getClassId();
        redDotRequest.bizIds = "taskNew,momentNew,resourceNew";
        mGetRedDotRequestUUID = redDotRequest.startRequest(RedDotResponse.class, new HttpCallback<RedDotResponse>() {
            @Override
            public void onSuccess(RequestBase request, RedDotResponse ret) {
                if (0 == ret.getCode()) {
                    //有新的班级圈消息
                    if (ret.getData().getMomentNew() != null) {
                        if (ret.getData().getMomentNew().getPromptNum() >= 0) {
                            mImgClassCircleRedCircle.setVisibility(View.VISIBLE);
                            showCommentRedDot = true;
                        } else {
                            showCommentRedDot = false;
                            mImgClassCircleRedCircle.setVisibility(View.GONE);
                        }
                    }

                    //有新的资源消息
                    if (ret.getData().getResourceNew() != null) {
                        if (ret.getData().getResourceNew().getPromptNum() >= 0) {
                            if (mNaviFragmentFactory != null) {
                                HomeFragment homeFragment = mNaviFragmentFactory.getHomeFragment();
                                if (homeFragment != null) {
                                    homeFragment.showResourceRedDot();
                                }
                            }
                        }
                    }
                    //有新的任务信息
                    if (ret.getData().getTaskNew() != null) {
                        if (ret.getData().getTaskNew().getPromptNum() >= 0) {
                            HomeFragment homeFragment = mNaviFragmentFactory.getHomeFragment();
                            if (homeFragment != null) {
                                homeFragment.showTaskRedDot();
                            }
                        }
                    }
                }
                handler.sendEmptyMessageDelayed(2, 30000);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                handler.sendEmptyMessageDelayed(2, 30000);

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetHasNotificationsNeedReadRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetHasNotificationsNeedReadRequestUUID);
        }
        if (mGetRedDotRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetRedDotRequestUUID);
        }
        // TODO: 17-9-21 移除轮讯
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

    }


}
