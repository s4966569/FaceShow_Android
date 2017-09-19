package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.HomeFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.NaviFragmentFactory;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.QRCodeCheckInActivity;


/**
 * 首页 “首页”Fragment
 */
public class HomeFragment extends FaceShowBaseFragment implements View.OnClickListener {
    private final static String TAG = HomeFragment.class.getSimpleName();

    private PublicLoadLayout mRootView;

    public HomeFragmentFactory mFragmentFactory;
    public FragmentManager mFragmentManager;

    private final int INDEX_HOME_TAB = 0;//首页tab
    private final int INDEX_NOTICE_TAB = 1;//通知tab
    private final int INDEX_CLASSCIRCLE_TAB = 2;//班级圈tab
    private final int INDEX_MY = 3;//我的tab
    private int mLastSelectIndex = -1;

    private TextView mCourseArrange_tab;//课程安排
    private TextView mResources_tab;//资源
    private TextView mProjectTask_tab;//项目任务
    private TextView mSchedule_tab;//日程计划

    private ImageView mCheckInEnter;//签到入口l

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getContext());
        mRootView.setContentView(R.layout.fragment_home);
        initView();
        intListener();
        return mRootView;
    }

    private void initView() {
        mCourseArrange_tab = (TextView) mRootView.findViewById(R.id.courseArrange_tab);
        mResources_tab = (TextView) mRootView.findViewById(R.id.resources_tab);
        mProjectTask_tab = (TextView) mRootView.findViewById(R.id.projectTask_tab);
        mSchedule_tab = (TextView) mRootView.findViewById(R.id.schedule_tab);
        mCheckInEnter = (ImageView) mRootView.findViewById(R.id.title_layout_right_img);

        mFragmentManager = getChildFragmentManager();
        mFragmentFactory = new HomeFragmentFactory();
        showCurrentFragment(0);
    }

    private void intListener() {
//        mCourseArrange_tab.setOnClickListener(this);
//        mResources_tab.setOnClickListener(this);
//        mProjectTask_tab.setOnClickListener(this);
//        mSchedule_tab.setOnClickListener(this);
//        mCheckInEnter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int curItem = INDEX_HOME_TAB;
        switch (view.getId()) {
            case R.id.courseArrange_tab:
                curItem = INDEX_HOME_TAB;
//                mNavIconViews[0].setEnabled(false);
//                mNavIconViews[1].setEnabled(true);
//                mNavIconViews[2].setEnabled(true);
//                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.resources_tab:
                curItem = INDEX_NOTICE_TAB;
//                mNavIconViews[0].setEnabled(true);
//                mNavIconViews[1].setEnabled(false);
//                mNavIconViews[2].setEnabled(true);
//                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.projectTask_tab:
                curItem = INDEX_CLASSCIRCLE_TAB;
//                mNavIconViews[0].setEnabled(true);
//                mNavIconViews[1].setEnabled(true);
//                mNavIconViews[2].setEnabled(false);
//                mNavIconViews[3].setEnabled(true);
                break;
            case R.id.schedule_tab:
                curItem = INDEX_MY;
//                mNavIconViews[0].setEnabled(true);
//                mNavIconViews[1].setEnabled(true);
//                mNavIconViews[2].setEnabled(true);
//                mNavIconViews[3].setEnabled(false);
                break;
            case R.id.title_layout_right_img:
                QRCodeCheckInActivity.toThisAct(getActivity());
                break;
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
