package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.common.eventbus.InteractMessage;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.activity.CourseDiscussActivity;
import com.yanxiu.gphone.faceshow.course.activity.EvaluationActivity;
import com.yanxiu.gphone.faceshow.course.activity.VoteActivity;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInActivity;
import com.yanxiu.gphone.faceshow.homepage.adapter.ProjectTaskAdapter;
import com.yanxiu.gphone.faceshow.http.course.ProjectTaskListRequest;
import com.yanxiu.gphone.faceshow.http.course.ProjectTaskListResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;


/**
 * 首页tab里 “项目任务”Fragment
 */
public class ProjectTaskFragment extends HomePageBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ProjectTaskFragment.class.getSimpleName();
    private final int mHashCode = ProjectTaskFragment.this.hashCode();

    private PublicLoadLayout mRootView;
    private RecyclerView mRecyclerView;
    private ProjectTaskAdapter mAdapter;

    List<InteractStepsBean> mProjectTaskList = new ArrayList<>();
    private UUID mRequestUUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_project_task);
        mRootView.setErrorLayoutFullScreen();
        EventBus.getDefault().register(ProjectTaskFragment.this);
        initView();
        initListener();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProjectTaskList.clear();
                getProjectTaskList();
            }
        });
        getProjectTaskList();
        return mRootView;
    }

    /**
     * 每次点击tab时，都要刷新数据
     */
    @Override
    public void refreshData() {
        getProjectTaskList();
    }

    private void getProjectTaskList() {
        mRootView.showLoadingView();
        ProjectTaskListRequest projectTaskListRequest = new ProjectTaskListRequest();
        projectTaskListRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mRequestUUID = projectTaskListRequest.startRequest(ProjectTaskListResponse.class, new HttpCallback<ProjectTaskListResponse>() {
            @Override
            public void onSuccess(RequestBase request, ProjectTaskListResponse ret) {
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().size() > 0) {
                        mProjectTaskList = ret.getData();
                        mAdapter.setData(mProjectTaskList);
                        mRootView.hiddenOtherErrorView();
                        mRootView.hiddenNetErrorView();
                    } else {
                        mRootView.showOtherErrorView(getString(R.string.no_task_hint));
                    }
                } else {
                    mRootView.showOtherErrorView(getString(R.string.no_task_hint));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
            }
        });
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
        setIntent(position, baseBean);
    }

    private void setIntent(int position, BaseBean baseBean) {
        InteractStepsBean taskBean = (InteractStepsBean) baseBean;
        switch (taskBean.getInteractType()) {
            case InteractStepsBean.VOTE:
                VoteActivity.invoke(getActivity(), taskBean.getStepId(), position, mHashCode);
                break;
            case InteractStepsBean.DISCUSS:
                CourseDiscussActivity.invoke(getActivity(), taskBean);
                break;
            case InteractStepsBean.QUESTIONNAIRES:
                EvaluationActivity.invoke(getActivity(), taskBean.getStepId(), position, mHashCode);
                break;
            case InteractStepsBean.CHECK_IN:
                CheckInActivity.toThisAct(getActivity());
                break;
        }
    }

    public void onEventMainThread(InteractMessage message) {
        if (message != null && mHashCode == message.hascode) {
            if (message.position != -1) {
                InteractStepsBean bean = mProjectTaskList.get(message.position);
                bean.setStepFinished("1");
                mAdapter.notifyItemChanged(message.position);
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mRequestUUID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
