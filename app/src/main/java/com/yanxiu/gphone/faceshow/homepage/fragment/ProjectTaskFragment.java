package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.content.Context;
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
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.ProjectTaskAdapter;
import com.yanxiu.gphone.faceshow.http.course.ProjectTaskListRequest;
import com.yanxiu.gphone.faceshow.http.course.ProjectTaskListResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 首页tab里 “项目任务”Fragment
 */
public class ProjectTaskFragment extends FaceShowBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ProjectTaskFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;
    private RecyclerView mRecyclerView;
    private ProjectTaskAdapter mAdapter;

    List<ProjectTaskListResponse.ProjectTaskBean> mProjectTaskList = new ArrayList<>();
    private UUID mRequestUUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_project_task);
        mRootView.setErrorLayoutFullScreen();
        initView();
        initListener();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.showLoadingView();
                mProjectTaskList.clear();
                getProjectTaskList();
            }
        });
        return mRootView;
    }

    private void getProjectTaskList() {
        ProjectTaskListRequest projectTaskListRequest = new ProjectTaskListRequest();
        projectTaskListRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mRequestUUID = projectTaskListRequest.startRequest(ProjectTaskListResponse.class, new HttpCallback<ProjectTaskListResponse>() {
            @Override
            public void onSuccess(RequestBase request, ProjectTaskListResponse ret) {
                if (ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().size() > 0) {
                        mProjectTaskList = ret.getData();
                    }
                    mAdapter.setData(mProjectTaskList);
                    mRootView.hiddenOtherErrorView();
                    mRootView.hiddenNetErrorView();
                } else {
                    if (mProjectTaskList.size() <= 0) {
                        mRootView.showOtherErrorView();
                    }
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
    public void onAttach(Context context) {
        super.onAttach(context);
        getProjectTaskList();
    }

    private void initListener() {
    }

    private void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ProjectTaskAdapter(getActivity(), this);
//        mAdapter.setData(ProjectTaskBean.getMockData());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
        ToastUtil.showToast(getActivity(), position + "");
        setIntent();
    }

    private void setIntent() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mRequestUUID);
        }
    }
}
