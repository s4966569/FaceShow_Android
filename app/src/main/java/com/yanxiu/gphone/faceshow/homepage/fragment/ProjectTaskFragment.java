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
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.HomeResourcesAdapter;
import com.yanxiu.gphone.faceshow.homepage.adapter.ProjectTaskAdapter;
import com.yanxiu.gphone.faceshow.homepage.bean.ProjectTaskBean;
import com.yanxiu.gphone.faceshow.homepage.bean.ResourceBean;
import com.yanxiu.gphone.faceshow.util.ToastUtil;


/**
 * 首页tab里 “项目任务”Fragment
 */
public class ProjectTaskFragment extends FaceShowBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ProjectTaskFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;
    private RecyclerView mRecyclerView;
    private ProjectTaskAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_project_task);
        initView();
        initListener();
        return mRootView;
    }

    private void initListener() {
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectTaskAdapter(getActivity(), this);
        mAdapter.setData(ProjectTaskBean.getMockData());
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
