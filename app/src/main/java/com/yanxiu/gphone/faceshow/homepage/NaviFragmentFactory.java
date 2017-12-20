package com.yanxiu.gphone.faceshow.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
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

    public HomeFragment getHomeFragment() {
        return mHomeFragment;
    }
    public NoticeFragment getNoticeFragment() {
        return  mNoticeFragment;
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




    public FaceShowBaseFragment hideAndShowFragment(FragmentManager fragmentManager, int index) {
        FaceShowBaseFragment fragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
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
        mCurrItem = index;
        switch (mCurrItem) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    transaction.add(R.id.content_main, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                fragment = mHomeFragment;
                break;
            case 1:
                if (mNoticeFragment == null) {
                    mNoticeFragment = new NoticeFragment();
                    transaction.add(R.id.content_main, mNoticeFragment);
                } else {
                    transaction.show(mNoticeFragment);
                }
                fragment = mNoticeFragment;
                break;
            case 2:
                if (mClassCircleFragment == null) {
                    mClassCircleFragment = new ClassCircleFragment();
                    transaction.add(R.id.content_main, mClassCircleFragment);
                } else {
                    transaction.show(mClassCircleFragment);
                }
                fragment = mClassCircleFragment;
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    transaction.add(R.id.content_main, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                }
                fragment = mMyFragment;
                break;
        }
        transaction.commit();
        return fragment;
    }
}
