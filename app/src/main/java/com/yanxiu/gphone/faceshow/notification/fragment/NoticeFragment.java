package com.yanxiu.gphone.faceshow.notification.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.notificaion.NotificationListRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.NotificationResponse;
import com.yanxiu.gphone.faceshow.notification.NotificationDetailActivity;
import com.yanxiu.gphone.faceshow.notification.adapter.NotificationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 首页 “通知”Fragment
 */
public class NoticeFragment extends FaceShowBaseFragment {
    private final static String TAG = NoticeFragment.class.getSimpleName();
    PublicLoadLayout mRootView;
    @BindView(R.id.loadMoreRecyclerView)
    LoadMoreRecyclerView loadMoreRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private NotificationAdapter mNotificationAdapter;
    private List<NotificationResponse.Notification> mNotificationList = new ArrayList<>();
    private UUID mNotificationRequestUUID;
    /*从第几条开始加载数据*/
    private int mOffset = 0;
    /*每页多少条*/
    private String mPageSize = "10";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(view);
        unbinder = ButterKnife.bind(this, mRootView);
        setRecyclerView(loadMoreRecyclerView);
        mRootView.showLoadingView();
        getNotifications();
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.showLoadingView();
                mNotificationList.clear();
                getNotifications();
            }
        });
        return mRootView;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mOffset = 0;
            getNotifications();
        }
    };

    private void getNotifications() {
        NotificationListRequest notificationRequest = new NotificationListRequest();
        notificationRequest.offset = String.valueOf(mOffset);
        notificationRequest.pageSize = mPageSize;
        notificationRequest.clazsId = "1";
        mNotificationRequestUUID = notificationRequest.startRequest(NotificationResponse.class, new HttpCallback<NotificationResponse>() {
            @Override
            public void onSuccess(RequestBase request, NotificationResponse ret) {
                mRootView.hiddenLoadingView();
                swipeRefreshLayout.setRefreshing(false);
                if (ret.getCode() == 0) {
                    if (ret.getNotificationList() != null && ret.getNotificationList().size() > 0) {
                        if (mOffset == 0) {
                            mNotificationList.clear();
                        }
                        mNotificationList.addAll(ret.getNotificationList());
                    }
                    mNotificationAdapter.update(mNotificationList);
                    mRootView.hiddenOtherErrorView();
                    mRootView.hiddenNetErrorView();
                } else {
                    if (mNotificationList.size() <= 0) {
                        mRootView.showOtherErrorView();
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                swipeRefreshLayout.setRefreshing(false);
                mRootView.showNetErrorView();
            }
        });
    }

    private void setRecyclerView(LoadMoreRecyclerView loadMoreRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreRecyclerView.setLayoutManager(layoutManager);
        mNotificationAdapter = new NotificationAdapter();
        loadMoreRecyclerView.setAdapter(mNotificationAdapter);
        loadMoreRecyclerView.setLoadMoreEnable(true);
        loadMoreRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                mRootView.showLoadingView();
                getNotifications();
            }

            @Override
            public void onLoadmoreComplte() {
                mRootView.hiddenLoadingView();
            }
        });
        mNotificationAdapter.setItemClickListener(new NotificationAdapter.ItemClickListener() {
            @Override
            public void itemClick(int position) {
                // TODO: 17-9-19 消耗小红点的网络请求   小红点消耗请求失败怎么办？

                NotificationDetailActivity.toThisAct(getActivity(), String.valueOf(mNotificationList.get(position).getId()));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mNotificationRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mNotificationRequestUUID);
        }
    }
}
