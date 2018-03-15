package com.yanxiu.gphone.faceshow.homepage.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.recyclerview.LeftDrawerListAdapter;
import com.yanxiu.gphone.faceshow.customview.recyclerview.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.getui.ToMainActivityBroadcastReceiver;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInNotesActivity;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.homepage.fragment.HomeFragment;
import com.yanxiu.gphone.faceshow.http.course.GetStudentClazsesResponse;
import com.yanxiu.gphone.faceshow.http.course.GetSudentClazsesRequest;
import com.yanxiu.gphone.faceshow.http.main.MainRequest;
import com.yanxiu.gphone.faceshow.http.main.MainResponse;
import com.yanxiu.gphone.faceshow.http.main.RedDotRequest;
import com.yanxiu.gphone.faceshow.http.main.RedDotResponse;
import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadResponse;
import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.user.FeedBackActivity;
import com.yanxiu.gphone.faceshow.user.ProfileActivity;
import com.yanxiu.gphone.faceshow.util.ActivityManger;
import com.yanxiu.gphone.faceshow.util.SystemUtil;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.util.UpdateUtil;

import java.util.UUID;

public class MainActivity extends FaceShowBaseActivity implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();
    private PublicLoadLayout mRootView;

    /*跳转到 切换班级界面的requestcode*/
    private final int CHOOSE_CLASS = 0X01;
    /*跳转到 用户信息设置界面*/
    private final int REQUEST_CODE_SET_PROFILE = 0X02;


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

    //by zhuxiaolong
    /*学员端增加 侧滑菜单  左侧*/
    private DrawerLayout mDrawerLayout;//  google 抽屉布局
    private RecyclerView mLeftDrawerList;// 左侧抽屉View内的RecyclerView 菜单列表部分
    private View mLeftDrawerView;//左侧抽屉View 包含 顶部的头布局  还有菜单列表布局（由recyclerview 实现）
    private LeftDrawerListAdapter mLeftDrawerListAdapter;//左侧抽屉内 RecyclerView 的adapter 用于填充生成 菜单内容
    private DrawerLayout.DrawerListener mDrawerToggleListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_main);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        mContext = this;
        initView();
        initListener();
        getData();
        UpdateUtil.Initialize(this, false);

        /*判断是否进入班级选择界面*/
        if (SpManager.isFristStartUp()) {
            SpManager.setFristStartUp(false);
            /*请求班级列表 并判断是否跳转*/
            getClassListData();
        }
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
        isToFirstFragment = intent.getBooleanExtra(ToMainActivityBroadcastReceiver.IS_TO_FIRST_FRAGMENT,
                false);
        setIntent(intent);
        setHomeFragment();
        /*切换tab 到课程 */
    }

    private void drawerLayoutInit() {
        mDrawerLayout = (DrawerLayout) mRootView.findViewById(R.id.drawer_layout);
        mLeftDrawerList = (RecyclerView) mRootView.findViewById(R.id.left_drawer_list);
        // TODO: 2018/3/2  将recyclerview 的overscroll 动画去除
        mDrawerLayout.addDrawerListener(mDrawerToggleListener);
        mLeftDrawerView = mRootView.findViewById(R.id.left_drawer);


//        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.selector_drawerview_toggle,)
        setLeftDrawerContent();
        /*退出按钮*/
        TextView exitTextView = (TextView) mRootView.findViewById(R.id.exit);
        exitTextView.setOnClickListener(this);
    }

    /***
     * 对左侧抽屉菜单进行布局初始化以及内容填充
     *
     */
    public void setLeftDrawerContent() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLeftDrawerList.setLayoutManager(linearLayoutManager);
        /*设置*/
        mLeftDrawerListAdapter = new LeftDrawerListAdapter(mContext);
        mLeftDrawerList.setAdapter(mLeftDrawerListAdapter);
        /*左侧抽屉 recyclerview item点击监听*/
        mLeftDrawerListAdapter.addItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.i(getClass().getSimpleName(), "onItemClick: " + position);
                LeftDrawerListItemToOtherAct(position);
            }
        });
        /*抽屉顶部信息部分的点击监听 用户头像 和 切换班级按钮*/
        mLeftDrawerListAdapter.setHeaderViewClickListener(new LeftDrawerListAdapter.HeaderViewClickListener() {
            @Override
            public void onHeaderImgClicked() {
                // TODO: 2018/3/2  点击用户头像
            }

            @Override
            public void onHeaderButtonClicked() {
                /*点击 切换班级按钮 在onactivityresult 回调中重置 抽屉的数据*/
                mDrawerLayout.closeDrawer(mLeftDrawerView);
                toChooseClassActivity(new Intent(MainActivity.this,
                        ChooseClassActivity.class), CHOOSE_CLASS);
            }
        });
        /*设置版本号显示*/
        TextView appVersionTv = mRootView.findViewById(R.id.app_version_textview);
        appVersionTv.setText("版本号：v" + SystemUtil.getVersionName());
    }

    private void toChooseClassActivity(Intent intent, int choose_class) {
        startActivityForResult(intent, choose_class);
    }

    /**
     * 抽屉菜单 跳转逻辑
     * 菜单由一个 recyclerview 生成 顶部信息部分 为head layout
     * 由于内容较多 所以不在 listitem 中设置点击监听 使用
     * {@link com.yanxiu.gphone.faceshow.customview.recyclerview.LeftDrawerListAdapter.HeaderViewClickListener}
     */
    private void LeftDrawerListItemToOtherAct(int position) {
        switch (position) {
            case 0:
                /*跳转班级首页*/
                toClassHomePage();
                mDrawerLayout.closeDrawer(mLeftDrawerView);
                break;
            case 1:
                /*跳转我的资料*/
                mDrawerLayout.closeDrawer(mLeftDrawerView,true);
                toMyProfile();
                break;
            case 2:
                /*跳转签到记录*/
                mDrawerLayout.closeDrawer(mLeftDrawerView,true);
                CheckInNotesActivity.toThisAct(MainActivity.this);
                break;
            case 3:
                /*跳转意见反馈*/
                mDrawerLayout.closeDrawer(mLeftDrawerView,true);
                startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
                break;
            default:
        }
//        mDrawerLayout.closeDrawer(mLeftDrawerView);

    }

    private void toMyProfile() {
        Intent i = new Intent(MainActivity.this, ProfileActivity.class);
        toChooseClassActivity(i, REQUEST_CODE_SET_PROFILE);
    }

    private void toClassHomePage() {
        showCurrentFragment(setHomeFragment());
        /* 点击 课程详情 需要刷新 tab 位置*/
        mNaviFragmentFactory.getHomeFragment().resetTab();
    }

    public void openLeftDrawer() {
        mDrawerLayout.openDrawer(mLeftDrawerView);
    }

    private void initView() {
        mBottomView = findViewById(R.id.navi_switcher);
        initBottomBar();
        /*初始化左侧抽屉菜单*/
        drawerLayoutInit();
    }

    private void initFragment() {
        mFragmentManager = getSupportFragmentManager();
        mNaviFragmentFactory = new NaviFragmentFactory(mFragmentManager);
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


    private void getData() {
        if (TextUtils.isEmpty(SpManager.getUserInfo().getClassId())) {
            requestData();
        } else {
            initFragment();
            pollingRedPointer();
        }
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
                    UserInfo.Info info = SpManager.getUserInfo();
                    info.setClassId(mMainData.getClazsInfo().getId());
                    info.setClassName(mMainData.getClazsInfo().getClazsName());
                    info.setProjectName(mMainData.getProjectInfo().getProjectName());
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
                mRootView.showOtherErrorView(error.getMessage());

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
            } else {

            }
        }
    };

    private void getRedPointersRequest() {
        GetHasNotificationsNeedReadRequest getHasNotificationsNeedReadRequest
                = new GetHasNotificationsNeedReadRequest();
        getHasNotificationsNeedReadRequest.clazsId = SpManager.getUserInfo().getClassId();
        mGetHasNotificationsNeedReadRequestUUID = getHasNotificationsNeedReadRequest
                .startRequest(GetHasNotificationsNeedReadResponse.class,
                        new HttpCallback<GetHasNotificationsNeedReadResponse>() {
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

    public void hideClassCircleRedDot() {
        if (mImgClassCircleRedCircle.getVisibility() == View.VISIBLE) {
            mImgClassCircleRedCircle.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.navi_1:
                curItem = setHomeFragment();
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
                mNavIconViews[0].setEnabled(true);
                mNavIconViews[1].setEnabled(true);
                mNavIconViews[2].setEnabled(false);
                mNavIconViews[3].setEnabled(true);
                if (mImgClassCircleRedCircle.getVisibility() == View.VISIBLE) {
                    mImgClassCircleRedCircle.setVisibility(View.GONE);
                }
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
                getData();
                break;

            case R.id.exit:
            /*左侧抽屉 退出登录按钮  从MyFragment 的退出登录复制*/
                logout();
                return;

            default:
                break;
        }
        showTargetFragment(curItem);
    }

    private void showTargetFragment(int curItem) {

        if (mNaviFragmentFactory != null && mNaviFragmentFactory.getCurrentItemIndex() != curItem) {

            showCurrentFragment(curItem);
        }
    }

    private int setHomeFragment() {
        int curItem;
        curItem = INDEX_HOME_TAB;
        mNavIconViews[0].setEnabled(false);
        mNavIconViews[1].setEnabled(true);
        mNavIconViews[2].setEnabled(true);
        mNavIconViews[3].setEnabled(true);
        if (mNaviFragmentFactory.getHomeFragment() != null) {
            mNaviFragmentFactory.getHomeFragment().toRefresh();
        }
        return curItem;
    }

    private void logout() {
        LoginActivity.toThisAct(MainActivity.this);
        UserInfo.getInstance().setInfo(null);
        SpManager.saveToken("");
        clearGTPushSettings();
        SpManager.loginOut();//设置为登出状态
        MainActivity.this.finish();
    }

    /**
     * 去除 通知以及推送  从MyFragment 复制过来的
     */
    private void clearGTPushSettings() {
        NotificationManager notificationManager = (NotificationManager) MainActivity.this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        PushManager.getInstance()
                .unBindAlias(MainActivity.this,
                        String.valueOf(SpManager.getUserInfo().getUserId()),
                        true, "2000");//只对当前cid做解绑
        PushManager.getInstance().turnOffPush(MainActivity.this);
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
            mNaviFragmentFactory = new NaviFragmentFactory(mFragmentManager);
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
        if (requestCode == CHOOSE_CLASS) {
            /*由切换班级界面返回 */
            if (resultCode == RESULT_OK) {
//                getData();
//                reFreshFragment();
                setHomeFragment();
                showCurrentFragment(INDEX_HOME_TAB);
                pollingRedPointer();
                /*对 抽屉内的headerview展示信息进行更新   */
                mLeftDrawerListAdapter.notifyItemChanged(0);
            }
        } else if (requestCode == REQUEST_CODE_SET_PROFILE) {
            /*由用户信息界面返回 用户可能对信息进行了设置
              更新抽屉信息*/
            mLeftDrawerListAdapter.notifyItemChanged(0);
//            if (resultCode == RESULT_OK) {
//                mLeftDrawerListAdapter.notifyItemChanged(0);
//            }

        }
    }

    private void reFreshFragment() {
        if (mNaviFragmentFactory.getHomeFragment() != null) {
            mNaviFragmentFactory.getHomeFragment().toRefresh();
        }
        if (mNaviFragmentFactory.getNoticeFragment() != null) {
            mNaviFragmentFactory.getNoticeFragment().toRefresh();
        }
        if (mNaviFragmentFactory.getClassCircleFragment() != null) {
            mNaviFragmentFactory.getClassCircleFragment().toRefresh();
        }
    }

    private UUID mGetRedDotRequestUUID;
    private boolean showResourceRedDot = false;
    private boolean showTaskRedDot = false;
    private boolean showCommentRedDot = false;

    private void getRedDotRequest() {
        RedDotRequest redDotRequest = new RedDotRequest();
        redDotRequest.clazsId = SpManager.getUserInfo().getClassId();
        redDotRequest.bizIds = "taskNew,momentNew,resourceNew,momentMsgNew";
        mGetRedDotRequestUUID = redDotRequest.startRequest(RedDotResponse.class, new HttpCallback<RedDotResponse>() {
            @Override
            public void onSuccess(RequestBase request, RedDotResponse ret) {
                if (0 == ret.getCode()) {
                    //有新的班级圈消息
                    if (ret.getData().getMomentNew() != null) {
                        if (ret.getData().getMomentNew().getPromptNum() >= 0) {
//                            if (mNaviFragmentFactory.getCurrentItemIndex() == 2) {
                            //当前在班级圈页面
//                                ClassCircleFragment classCircleFragment =
// mNaviFragmentFactory.getClassCircleFragment();
//                                if (classCircleFragment != null && !classCircleFragment.firstEnter) {
                            //mNaviFragmentFactory.getClassCircleFragment().toRefresh();
//                                }
//                            } else {
                            //当前不在班级圈页面
                            mImgClassCircleRedCircle.setVisibility(View.VISIBLE);
                            showCommentRedDot = true;

                        } else {
                            showCommentRedDot = false;
                            mImgClassCircleRedCircle.setVisibility(View.GONE);
                        }
                    }

                    //新的班级圈回复消息
                    if (ret.getData().getMomentMsgNew() != null) {
                        if (ret.getData().getMomentMsgNew().getPromptNum() > 0) {
                            if (mNaviFragmentFactory.getCurrentItemIndex() == 2) {
                                ClassCircleFragment classCircleFragment =
                                        mNaviFragmentFactory.getClassCircleFragment();
                                if (classCircleFragment != null && !classCircleFragment.firstEnter) {
                                    mNaviFragmentFactory
                                            .getClassCircleFragment()
                                            .showMomentMsg(ret.getData()
                                                    .getMomentMsgNew()
                                                    .getPromptNum());
                                }
                            }
                        } else {
                            if (mNaviFragmentFactory.getCurrentItemIndex() == 2) {
                                ClassCircleFragment classCircleFragment =
                                        mNaviFragmentFactory.getClassCircleFragment();
                                if (classCircleFragment != null && !classCircleFragment.firstEnter) {
                                    mNaviFragmentFactory.
                                            getClassCircleFragment()
                                            .hideMomentMsg();
                                }
                            }
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
                    } else {
                        HomeFragment homeFragment = mNaviFragmentFactory.getHomeFragment();
                        if (homeFragment != null) {
                            homeFragment.hideTaskRedDot();
                        }
                    }


                }
                handler.sendEmptyMessageDelayed(2, 10000);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                handler.sendEmptyMessageDelayed(2, 10000);

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
    private void getClassListData() {
        mRootView.showLoadingView();
        GetSudentClazsesRequest getSudentClazsesRequest = new GetSudentClazsesRequest();
        getSudentClazsesRequest.startRequest(GetStudentClazsesResponse.class, new HttpCallback<GetStudentClazsesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetStudentClazsesResponse ret) {
                mRootView.finish();
//                mUUID = null;
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().getClazsInfos() != null && ret.getData().getClazsInfos().size() > 0) {
                        if (ret.getData().getClazsInfos().size()!=1) {
                            /*当只有一个班级的时候 什么都不做*/
                            toChooseClassActivity(new Intent(MainActivity.this,
                                    ChooseClassActivity.class), CHOOSE_CLASS);
                        }
                    } else {
                        mRootView.showOtherErrorView("暂无班级");
                    }
                } else {
                    mRootView.showOtherErrorView(ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
                mRootView.showOtherErrorView(error.getMessage());
            }
        });
    }


}
