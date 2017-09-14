//package com.yanxiu.gphone.faceshow.homepage;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.yanxiu.gphone.faceshow.R;
//import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by 戴延枫 on 2017/5/9.
// */
//
//public class WelcomeActivity extends FaceShowBaseActivity implements View.OnClickListener {
//
//    /**
//     * add LOAD_TIME and change time
//     * cwq
//     * */
//    private static final int LOAD_TIME=400;
//
//    private RelativeLayout mRootView;
//
//    //viewpager相关
//    private ViewPager mViewpager;
//    private MyViewPagerAdapter mAdapter;
//    private int mLastX = 0;
//    boolean isLoading = false;//在引导页最后一页防止重复调用检查登录状态
//    private List<View> mViews;//引导页list
//    private Handler mHander;
//    private final static int GO_LOGIN = 0x0001;
//    private final static int GO_MAIN = 0x0002;
//    private final static int GO_GUIDE_PAGE = 0x0003;
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_welcome);
//        initView();
//    }
//
//    private void initView() {
//        mRootView = (RelativeLayout) findViewById(R.id.root_view);
//        mHander = new WelcomeHandler(this);
//        initViewPager();
//        checkUserStatus();
//    }
//
//    /**
//     * 初始化viewpager
//     */
//    private void initViewPager() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        mViews = new ArrayList<>();
//        View view=inflater.inflate(R.layout.guide_view_one, null);
//        WavesLayout enterView= (WavesLayout) view.findViewById(R.id.wl_enter);
//        enterView.setOnClickListener(this);
//        mViews.add(view);
////        mViews.add(inflater.inflate(R.layout.guide_view_two, null));
////        mViews.add(inflater.inflate(R.layout.guide_view_three, null));
//
//        mViewpager = (ViewPager) findViewById(R.id.wel_vp);
//        mAdapter = new MyViewPagerAdapter();
//        mViewpager.setAdapter(mAdapter);
//        mViewpager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mLastX = (int) event.getX();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if ((mLastX - event.getX()) > 100 && (mViewpager.getCurrentItem() == mViews.size() - 1) && !isLoading) {
//                            isLoading = true;
////                            checkUserStatus();
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//
//    }
//
//    /**
//     * 检查用户状态
//     */
//    private void checkUserStatus() {
//        int lastVersion = SpManager.getAppVersionCode();
//        int version = SystemUtil.getVersionCode();
//        if (SpManager.isFristStartUp()) {
//            //第一次启动，展示引导页
//            mHander.sendEmptyMessageDelayed(GO_GUIDE_PAGE, LOAD_TIME);
//            SpManager.setFristStartUp(false);
//            SpManager.setAppVersionCode(version);
//        } else if (version > lastVersion) {
//            //当前版本大于上次记录的版本，认为是升级后首次启动，展示引导页
//            mHander.sendEmptyMessageDelayed(GO_GUIDE_PAGE, LOAD_TIME);
//            SpManager.setAppVersionCode(version);
//        } else {
//            //
//            if (!LoginInfo.isLogIn()) {
//                //TODO 用户信息不完整,跳转登录页
//                mHander.sendEmptyMessageDelayed(GO_LOGIN, LOAD_TIME);
//            } else {
//                //TODO 用户信息完整，跳转首页
//                mHander.sendEmptyMessageDelayed(GO_MAIN, LOAD_TIME);
//            }
//        }
//    }
//
//    class MyViewPagerAdapter extends PagerAdapter {
//        ImageView btn;
//
//        @Override
//        public int getCount() {
//            return mViews.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object obj) {
//            return view == obj;
//        }
//
//        @Override
//        public Object instantiateItem(View container, int position) {
//            View view = mViews.get(position);
//            ((ViewPager) container).addView(view, 0);
//            return mViews.get(position);
//        }
//
//        @Override
//        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager) container).removeView(mViews.get(position));
//        }
//    }
//
//    private static class WelcomeHandler extends Handler {
//
//        private WeakReference<WelcomeActivity> mActivity;
//
//        public WelcomeHandler(WelcomeActivity activity) {
//            mActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            WelcomeActivity activity = mActivity.get();
//
//            switch (msg.what) {
//                case GO_LOGIN:
//                    //TODO 登录页
//                    LoginActivity.LaunchActivity(activity);
//                    activity.finish();
//                    break;
//                case GO_MAIN:
//                    //TODO 进入首页
//                    MainActivity.invoke(activity);
//                    activity.finish();
//                    break;
//                case GO_GUIDE_PAGE:
//                    activity.mRootView.setVisibility(View.GONE);
//                    activity.mViewpager.setVisibility(View.VISIBLE);
//                    break;
//            }
//        }
//    }
//
//    ;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            //Todo 退出程序
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.wl_enter:
//                checkUserStatus();
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        mHander.removeCallbacksAndMessages(null);
//        super.onDestroy();
//        //TODO 相应操作
//    }
//}