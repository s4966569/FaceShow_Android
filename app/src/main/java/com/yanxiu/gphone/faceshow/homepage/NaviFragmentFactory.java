package com.yanxiu.gphone.faceshow.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.test.yanxiu.im_ui.ImTopicListFragment;
import com.test.yanxiu.im_ui.MyTestFragment;
import com.yanxiu.gphone.faceshow.R;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.classcircle.ClassCircleFragment;
import com.yanxiu.gphone.faceshow.homepage.fragment.HomeFragment;
import com.yanxiu.gphone.faceshow.user.MyFragment;
import com.yanxiu.gphone.faceshow.notification.fragment.NoticeFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * HomeActivity的fragment控制类
 */
public class NaviFragmentFactory {
    private int mCurrItemIndex = 0;
    private HomeFragment mHomeFragment = new HomeFragment();//首页
    private NoticeFragment mNoticeFragment = new NoticeFragment();//通知
    private ClassCircleFragment mClassCircleFragment = new ClassCircleFragment();//班级圈
    private MyFragment mMyFragment = new MyFragment();//我

    private MyTestFragment mMyTestFragment = new MyTestFragment(); //IM相关
    private ImTopicListFragment mTestFragment = new ImTopicListFragment();

    private List<FaceShowBaseFragment> fragments = new ArrayList<FaceShowBaseFragment>(
            Arrays.asList(
                    mHomeFragment,
                    mNoticeFragment,
                    mClassCircleFragment,
                    //mMyFragment,
                    //mMyTestFragment
                    mTestFragment
                    )
    );

    public NaviFragmentFactory(FragmentManager fm) {
        FragmentTransaction transaction = fm.beginTransaction();
        for (FaceShowBaseFragment fragment : fragments) {
            transaction.add(R.id.content_main, fragment);
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    public int getCurrentItemIndex() {
        return mCurrItemIndex;
    }

    public int getCount() {
        return fragments.size();
    }

    public Fragment getItem(int item) {
        return fragments.get(item);
    }

    public HomeFragment getHomeFragment() {
        return mHomeFragment;
    }

    public NoticeFragment getNoticeFragment() {
        return mNoticeFragment;
    }

    public ClassCircleFragment getClassCircleFragment()
    {
        return mClassCircleFragment;
    }

    public MyFragment getMyFragment() {
        return mMyFragment;
    }

    public MyTestFragment getTopicFragment() {
        return mMyTestFragment;
    }

    public FaceShowBaseFragment hideAndShowFragment(FragmentManager fragmentManager, int index) {
        FaceShowBaseFragment fragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (FaceShowBaseFragment f : fragments) {
            transaction.hide(f);
        }
        mCurrItemIndex = index;
        transaction.show(fragments.get(mCurrItemIndex));
        transaction.commit();
        return fragment;
    }
}
