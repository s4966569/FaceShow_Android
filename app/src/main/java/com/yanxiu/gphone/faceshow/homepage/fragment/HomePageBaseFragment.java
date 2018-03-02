package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;


/**
 * “首页”tab里，4个gragment的基类
 */
public abstract class HomePageBaseFragment extends FaceShowBaseFragment {

    public static final String TAG = HomePageBaseFragment.class.getSimpleName();

    public MainBean mMainBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getMainData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 获取首页的数据，如项目id，班级id等
     * 暂时这样写，等以后可以切换项目时，再根据实际情况修改
     */
    public void getMainData() {
        mMainBean = ((HomeFragment) getParentFragment()).mMainBean;
    }

    /**
     * 每次点击tab时，都要刷新数据
     */
    public abstract void refreshData();

}
