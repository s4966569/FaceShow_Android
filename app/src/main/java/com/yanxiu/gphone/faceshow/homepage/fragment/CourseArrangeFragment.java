package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.activity.CourseDetailActivity;
import com.yanxiu.gphone.faceshow.course.bean.CourseBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.CourseArrangeAdapter;
import com.yanxiu.gphone.faceshow.http.course.CourseArrangeRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseArrangeResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;


/**
 * 首页tab里 “课程安排”Fragment
 */
public class CourseArrangeFragment extends HomePageBaseFragment implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;

    private RecyclerView mRecyclerView;
    private CourseArrangeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_course_arrange);
        mRootView.setRetryButtonOnclickListener(this);
        mRootView.setErrorLayoutFullScreen();
        initView();
        initListener();
        requestData();
        return mRootView;
    }

    /**
     * 每次点击tab时，都要刷新数据
     */
    @Override
    public void refreshData() {
        requestData();
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
//        ToastUtil.showToast(getActivity(),"点击 item");
        // TODO: 2018/3/5  这里 学员版的 数据处理 跳转方式与管理端相同
        CourseBean bean = (CourseBean) baseBean;
        Gson gson=new Gson();

        Log.i(getClass().getSimpleName(),gson.toJson(bean));
        /*这里 两个课程详情的入口 */
//        CourseActivity.invoke(getActivity(), bean.getId());
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
        intent.putExtra("courseId",bean.getId());
        startActivity(intent);
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
        CourseArrangeRequest courseArrangeRequest = new CourseArrangeRequest();
        courseArrangeRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        courseArrangeRequest.startRequest(CourseArrangeResponse.class, new HttpCallback<CourseArrangeResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseArrangeResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && !ret.getData().getCourses().isEmpty()) {
                        mAdapter.setData(ret.getData().getCourses());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mRootView.showOtherErrorView();
                    }

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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {// 不在最前端界面显示
            Log.d("dyf", "onHiddenChanged: " + TAG + hidden);
        } else {// 重新显示到最前端中
            Log.d("dyf", "onHiddenChanged: " + TAG + hidden);
        }
    }
}
