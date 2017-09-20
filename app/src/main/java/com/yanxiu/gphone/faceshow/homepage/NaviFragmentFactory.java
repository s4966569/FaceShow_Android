package com.yanxiu.gphone.faceshow.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.HomeFragment;
import com.yanxiu.gphone.faceshow.user.MyFragment;
import com.yanxiu.gphone.faceshow.notification.fragment.NoticeFragment;

/**
 * HomeActivity的fragment控制类
 */
public class NaviFragmentFactory {
    private int mCurrItem = 0;
    private HomeFragment mHomeFragment;//首页
    private NoticeFragment mNoticeFragment;//通知
    private ClassCircleFragment mClassCircleFragment;//班级圈
    private MyFragment mMyFragment;//我

    public NaviFragmentFactory() {
    }

    public int getCurrentItem() {
        return mCurrItem;
    }

    public int getCount() {
        return 4;
    }

    public Fragment getItem(int item) {
        if (item == 0) {
            return mHomeFragment;
        } else if (item == 1) {
            return mNoticeFragment;
        } else if (item == 2) {
            return mClassCircleFragment;
        } else {
            return mMyFragment;
        }
    }

    private void hideFragment(FragmentTransaction transaction) {

        if (mCurrItem == 0 && mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mCurrItem == 1 && mNoticeFragment != null) {
            transaction.hide(mNoticeFragment);
        }
        if (mCurrItem == 2 && mClassCircleFragment != null) {
            transaction.hide(mClassCircleFragment);
        }
        if (mCurrItem == 3 && mMyFragment != null) {
            transaction.hide(mMyFragment);
        }
    }

    public void hideAndShowFragment(FragmentManager fragmentManager, int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mCurrItem == 0 && mHomeFragment != null) {
            transaction.hide(mHomeFragment);
            mHomeFragment.setUserVisibleHint(false);
        }
        if (mCurrItem == 1 && mNoticeFragment != null) {
            transaction.hide(mNoticeFragment);
            mNoticeFragment.setUserVisibleHint(false);
        }
        if (mCurrItem == 2 && mClassCircleFragment != null) {
            transaction.hide(mClassCircleFragment);
            mClassCircleFragment.setUserVisibleHint(false);
        }
        if (mCurrItem == 3 && mMyFragment != null) {
            transaction.hide(mMyFragment);
            mMyFragment.setUserVisibleHint(false);
        }
        mCurrItem = index;
        switch (mCurrItem) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.content_main, mHomeFragment);
                    mHomeFragment.setUserVisibleHint(true);
                } else {
                    transaction.show(mHomeFragment);
                    mHomeFragment.setUserVisibleHint(true);
                }
                break;
            case 1:
                if (mNoticeFragment == null) {
                    mNoticeFragment = new NoticeFragment();
                    transaction.add(R.id.content_main, mNoticeFragment);
                    mNoticeFragment.setUserVisibleHint(true);
                } else {
                    transaction.show(mNoticeFragment);
                    mNoticeFragment.setUserVisibleHint(true);
                }
                break;
            case 2:
                if (mClassCircleFragment == null) {
                    mClassCircleFragment = new ClassCircleFragment();
                    transaction.add(R.id.content_main, mClassCircleFragment);
                    mClassCircleFragment.setUserVisibleHint(true);
                } else {
                    transaction.show(mClassCircleFragment);
                    mClassCircleFragment.setUserVisibleHint(true);
                }
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    transaction.add(R.id.content_main, mMyFragment);
                    mMyFragment.setUserVisibleHint(true);
                } else {
                    transaction.show(mMyFragment);
                    mMyFragment.setUserVisibleHint(true);
                }
                break;
        }
        transaction.commit();
    }
}
