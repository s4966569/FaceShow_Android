package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.CourseArrangeAdapter;
import com.yanxiu.gphone.faceshow.homepage.adapter.HomeResourcesAdapter;
import com.yanxiu.gphone.faceshow.homepage.adapter.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshow.homepage.bean.ResourceBean;
import com.yanxiu.gphone.faceshow.util.ToastUtil;


/**
 * 首页tab里 “资源”Fragment
 */
public class ResourcesFragment extends FaceShowBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ResourcesFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;
    private RecyclerView mRecyclerView;
    private HomeResourcesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_resources);
        initView();
        initListener();
        return mRootView;
    }

    private void initListener() {
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HomeResourcesAdapter(getActivity(), this);
        mAdapter.setData(ResourceBean.getMockData());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
        ToastUtil.showToast(getActivity(), position + "");
        setIntent();
    }

    private void setIntent() {

    }
}
