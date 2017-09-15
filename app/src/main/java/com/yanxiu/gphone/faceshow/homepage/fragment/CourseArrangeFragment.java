package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.CourseActivity;
import com.yanxiu.gphone.faceshow.homepage.adapter.CourseArrangeAdapter;
import com.yanxiu.gphone.faceshow.homepage.adapter.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshow.util.ToastUtil;


/**
 * 首页tab里 “课程安排”Fragment
 */
public class CourseArrangeFragment extends FaceShowBaseFragment implements OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;

    private RecyclerView mRecyclerView;
    private CourseArrangeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_course_arrange);
        initView();
        return mRootView;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CourseArrangeAdapter(getActivity(), this);
        mAdapter.setData(CourseArrangeBean.getMockData());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
        ToastUtil.showToast(getActivity(),position+"");
        CourseActivity.invoke(getActivity());
    }
}
