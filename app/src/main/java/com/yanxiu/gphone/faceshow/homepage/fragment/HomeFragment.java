package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.HomeFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;


/**
 * 首页 “首页”Fragment
 */
public class HomeFragment extends FaceShowBaseFragment implements View.OnClickListener {
    private final static String TAG = HomeFragment.class.getSimpleName();

    private MainActivity mActivity;
    public MainBean mMainBean;

    private PublicLoadLayout mRootView;

    public HomeFragmentFactory mFragmentFactory;
    public FragmentManager mFragmentManager;

    private final int INDEX_HOME_TAB = 0;//首页tab
    private final int INDEX_NOTICE_TAB = 1;//通知tab
    private final int INDEX_CLASSCIRCLE_TAB = 2;//班级圈tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

    private final int mNavBarViewsCount = 4;
    private View[] mNavBarViews = new View[mNavBarViewsCount];

    private View mCourseArrange_tab;//课程安排tab
    private View mResources_tab;//资源tab
    private View mProjectTask_tab;//项目任务taba
    private View mSchedule_tab;//日程计划tab

    private TextView mTitle;
    private TextView mCheckInEnterTV;//签到入口文字描述
    private TextView mProject_tv;//项目名称
    private TextView mClass_tv;//班级
    private ImageView mCheckInEnterIMG;

    private ImageView mImgResourceRedDot, mImgProjectTaskRedDot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getContext());
        mRootView.setContentView(R.layout.fragment_home);
        mActivity = (MainActivity) getActivity();
        initData();
        initView();
        intListener();
        return mRootView;
    }

    private void initData() {
        mMainBean = mActivity.mMainData;
    }

    private void initView() {
        mTitle = (TextView) mRootView.findViewById(R.id.title_layout_title);
        mTitle.setText(R.string.homepage);
        mCheckInEnterTV = (TextView) mRootView.findViewById(R.id.title_layout_signIn);
        mCheckInEnterTV.setVisibility(View.VISIBLE);
        mCheckInEnterIMG = (ImageView) mRootView.findViewById(R.id.title_layout_right_img);
        mCheckInEnterIMG.setImageResource(R.drawable.scan_selector);
        mCheckInEnterIMG.setVisibility(View.VISIBLE);
        mProject_tv = (TextView) mRootView.findViewById(R.id.project_tv);
        mClass_tv = (TextView) mRootView.findViewById(R.id.class_tv);
        mImgProjectTaskRedDot = (ImageView) mRootView.findViewById(R.id.img_project_task_red_dot);
        mImgResourceRedDot = (ImageView) mRootView.findViewById(R.id.img_resource_red_dot);

        if (mMainBean != null && mMainBean.getClazsInfo() != null && mMainBean.getProjectInfo() != null) {
            mProject_tv.setText(mMainBean.getProjectInfo().getProjectName());
            mClass_tv.setText(mMainBean.getClazsInfo().getClazsName());
        }
        initTabBar();
        mFragmentManager = getChildFragmentManager();
        mFragmentFactory = new HomeFragmentFactory();
        showCurrentFragment(0);
    }

    private void initTabBar() {
        mCourseArrange_tab = mRootView.findViewById(R.id.courseArrange_tab);
        mResources_tab = mRootView.findViewById(R.id.resources_tab);
        mProjectTask_tab = mRootView.findViewById(R.id.projectTask_tab);
        mSchedule_tab = mRootView.findViewById(R.id.schedule_tab);

        mNavBarViews[0] = mCourseArrange_tab;
        mNavBarViews[1] = mResources_tab;
        mNavBarViews[2] = mProjectTask_tab;
        mNavBarViews[3] = mSchedule_tab;


    }

    public void showResourceRedDot() {
        mImgResourceRedDot.setVisibility(View.VISIBLE);
    }

    public void showTaskRedDot() {
        mImgProjectTaskRedDot.setVisibility(View.VISIBLE);
    }

    public void hideTaskRedDot() {
        mImgProjectTaskRedDot.setVisibility(View.GONE);
    }

    private void intListener() {
        mCourseArrange_tab.setOnClickListener(this);
        mResources_tab.setOnClickListener(this);
        mProjectTask_tab.setOnClickListener(this);
        mSchedule_tab.setOnClickListener(this);
        mCheckInEnterTV.setOnClickListener(this);
        mCheckInEnterIMG.setOnClickListener(this);
        for (int i = 0; i < mNavBarViews.length; i++) {
            mNavBarViews[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.courseArrange_tab:
                curItem = INDEX_HOME_TAB;
                mNavBarViews[0].setEnabled(false);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(true);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onCourseButton(getContext());
                break;
            case R.id.resources_tab:
                curItem = INDEX_NOTICE_TAB;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(false);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(true);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onResourceButton(getContext());
                mImgResourceRedDot.setVisibility(View.GONE);
                break;
            case R.id.projectTask_tab:
                curItem = INDEX_CLASSCIRCLE_TAB;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(false);
                mNavBarViews[3].setEnabled(true);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(true);
                mNavBarViews[3].setSelected(false);
                EventUpdate.onTaskButton(getContext());
                break;
            case R.id.schedule_tab:
                curItem = INDEX_MY;
                mNavBarViews[0].setEnabled(true);
                mNavBarViews[1].setEnabled(true);
                mNavBarViews[2].setEnabled(true);
                mNavBarViews[3].setEnabled(false);

                mNavBarViews[0].setSelected(false);
                mNavBarViews[1].setSelected(false);
                mNavBarViews[2].setSelected(false);
                mNavBarViews[3].setSelected(true);
                EventUpdate.onScheduleButton(getContext());
                break;
            case R.id.title_layout_signIn:
            case R.id.title_layout_right_img:
                CheckInByQRActivity.toThisAct(getActivity());
                EventUpdate.onHomeSignInButton(getContext());
                return;
            default:
                break;
        }
        if (mFragmentFactory.getCurrentItem() != curItem) {
            showCurrentFragment(curItem);
        }
    }

    private void showCurrentFragment(int index) {
        if (index == mLastSelectIndex) {
            return;
        }
        if (index == 0) { //默认选项
            mNavBarViews[0].setEnabled(false);
            mNavBarViews[0].setSelected(true);
        }
        mLastSelectIndex = index;
        if (mFragmentFactory == null) {
            mFragmentFactory = new HomeFragmentFactory();
        }
        if (mFragmentManager == null) {
            mFragmentManager = getChildFragmentManager();
        }
        mFragmentFactory.hideAndShowFragment(mFragmentManager, index);
    }
}
