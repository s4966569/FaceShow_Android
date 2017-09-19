package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.course.activity.CourseActivity;
import com.yanxiu.gphone.faceshow.homepage.adapter.CourseArrangeAdapter;
import com.yanxiu.gphone.faceshow.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshow.http.course.CourseListRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseListResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;


/**
 * 首页tab里 “课程安排”Fragment
 */
public class CourseArrangeFragment extends FaceShowBaseFragment implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;

    private RecyclerView mRecyclerView;
    private CourseArrangeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_course_arrange);
        mRootView.setRetryButtonOnclickListener(this);
        mRootView.setErrorLayoutFullScreen();
        initView();
        initListener();
        requestData();
        return mRootView;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CourseArrangeAdapter(getActivity(), this);
    }

    private void initListener() {
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
        ToastUtil.showToast(getActivity(), position + "");
        CourseActivity.invoke(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_button:
                requestData();
                break;
        }
    }

    private void requestData() {
        mRootView.showLoadingView();
        CourseListRequest courseListRequest = new CourseListRequest();
        courseListRequest.startRequest(CourseListResponse.class, new HttpCallback<CourseListResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseListResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getStatus().getCode() == 0) {
                    mAdapter.setData(CourseArrangeBean.getMockData());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();

            }
        });

    }
}
