package com.yanxiu.gphone.faceshow.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.CourseArrangeFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.HomeFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.MyFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.NoticeFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.ProjectTaskFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.ResourcesFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.ScheduleFragment;

/**
 * HomeFragment的控制类
 */
public class HomeFragmentFactory {
    private int mCurrItem = 0;
    private CourseArrangeFragment mCourseArrangeFragment;//课程安排
    private ResourcesFragment mResourcesFragment;//资源
    private ProjectTaskFragment mProjectTaskFragment;//项目任务
    private ScheduleFragment mScheduleFragment;//日程计划

    public HomeFragmentFactory() {
    }

    public int getCurrentItem() {
        return mCurrItem;
    }

    public int getCount() {
        return 4;
    }

    public Fragment getItem(int item) {
        if (item == 0) {
            return mCourseArrangeFragment;
        } else if (item == 1) {
            return mResourcesFragment;
        } else if (item == 2) {
            return mProjectTaskFragment;
        } else {
            return mScheduleFragment;
        }
    }

    private void hideFragment(FragmentTransaction transaction) {

        if (mCurrItem == 0 && mCourseArrangeFragment != null) {
            transaction.hide(mCourseArrangeFragment);
        }
        if (mCurrItem == 1 && mResourcesFragment != null) {
            transaction.hide(mResourcesFragment);
        }
        if (mCurrItem == 2 && mProjectTaskFragment != null) {
            transaction.hide(mProjectTaskFragment);
        }
        if (mCurrItem == 3 && mScheduleFragment != null) {
            transaction.hide(mScheduleFragment);
        }
    }

    public void hideAndShowFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mCurrItem == 0 && mCourseArrangeFragment != null) {
            transaction.hide(mCourseArrangeFragment);
        }
        if (mCurrItem == 1 && mResourcesFragment != null) {
            transaction.hide(mResourcesFragment);
        }
        if (mCurrItem == 2 && mProjectTaskFragment != null) {
            transaction.hide(mProjectTaskFragment);
        }
        if (mCurrItem == 3 && mScheduleFragment != null) {
            transaction.hide(mScheduleFragment);
        }
        mCurrItem = index;
        switch (mCurrItem) {
            case 0:
                if (mCourseArrangeFragment == null) {
                    mCourseArrangeFragment = new CourseArrangeFragment();
                    transaction.add(R.id.homefragment_container, mCourseArrangeFragment);
                } else {
                    transaction.show(mCourseArrangeFragment);
                }
                break;
            case 1:
                if (mResourcesFragment == null) {
                    mResourcesFragment = new ResourcesFragment();
                    transaction.add(R.id.homefragment_container, mResourcesFragment);
                } else {
                    transaction.show(mResourcesFragment);
                }
                break;
            case 2:
                if (mProjectTaskFragment == null) {
                    mProjectTaskFragment = new ProjectTaskFragment();
                    transaction.add(R.id.homefragment_container, mProjectTaskFragment);
                } else {
                    transaction.show(mProjectTaskFragment);
                }
                break;
            case 3:
                if (mScheduleFragment == null) {
                    mScheduleFragment = new ScheduleFragment();
                    transaction.add(R.id.homefragment_container, mScheduleFragment);
                } else {
                    transaction.show(mScheduleFragment);
                }
                break;
        }
        transaction.commit();
    }
}
