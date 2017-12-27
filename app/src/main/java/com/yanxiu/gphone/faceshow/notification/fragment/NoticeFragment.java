package com.yanxiu.gphone.faceshow.notification.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.RecyclerViewCanLoadMore;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.GetHasNotificationsNeedReadResponse;
import com.yanxiu.gphone.faceshow.http.notificaion.NotificationListRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.NotificationResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.notification.activity.NotificationDetailActivity;
import com.yanxiu.gphone.faceshow.notification.adapter.NotificationAdapter;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

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
    RecyclerViewCanLoadMore loadMoreRecyclerView;
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
    private int mCurrentItemPosition;

    private int tatleNum=0;

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
                mOffset = 0;
                getNotifications();
            }
        });
        return mRootView;
    }

    /**
     * 手动刷新页面，当底部通知tab被点击时调用
     */
    public void toRefresh() {
        mOffset = 0;
        mRootView.showLoadingView();
        getNotifications();
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
        notificationRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mNotificationRequestUUID = notificationRequest.startRequest(NotificationResponse.class, new HttpCallback<NotificationResponse>() {
            @Override
            public void onSuccess(RequestBase request, NotificationResponse ret) {
                mRootView.hiddenLoadingView();
                mRootView.hiddenOtherErrorView();
                mRootView.hiddenNetErrorView();
                if (mOffset == 0) {
                    mNotificationList.clear();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().getElements() != null) {
                        tatleNum=ret.getData().getTotalElements();
                        if (ret.getData().getElements().size() > 0) {

                            mNotificationList.addAll(ret.getData().getElements());
                        } else {
                            if (mOffset == 0) {
                                if (mNotificationList.size() > 0) {
                                    ToastUtil.showToast(FaceShowApplication.getContext(), "刷新失败");
                                } else {
                                    mRootView.showOtherErrorView(getString(R.string.no_notify));
                                }
                            }
                        }
                    }
                    mNotificationAdapter.update(mNotificationList);

                } else {
                    if (mNotificationList.size() <= 0) {
                        mRootView.showOtherErrorView("数据异常");
                    } else {
                        ToastUtil.showToast(FaceShowApplication.getContext(), "数据异常");
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (mNotificationList.size() <= 0) {
                    mRootView.showNetErrorView();
                } else {
                    ToastUtil.showToast(getContext(), error.getMessage());
                }
            }
        });
    }

    private void setRecyclerView(RecyclerViewCanLoadMore loadMoreRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreRecyclerView.setLayoutManager(layoutManager);
        mNotificationAdapter = new NotificationAdapter();
        loadMoreRecyclerView.setAdapter(mNotificationAdapter);
        loadMoreRecyclerView.addLoadMoreListener(new RecyclerViewCanLoadMore.OnLoadMoreListener() {
            @Override
            public void onLoadMore(RecyclerView recyclerView, int newState, int lastVisibleItem) {
                if (mOffset > 0) {
                    mOffset = mNotificationList.size();
                    if (mOffset<tatleNum) {
                        mRootView.showLoadingView();
                        getNotifications();
                    }
                } else {
                    mOffset = mNotificationList.size();
                }
            }
        });

        mNotificationAdapter.setItemClickListener(new NotificationAdapter.ItemClickListener() {
            @Override
            public void itemClick(int position) {
                mCurrentItemPosition = position;
                Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                intent.putExtra(NotificationDetailActivity.NOTIFICATION_ID, String.valueOf(mNotificationList.get(position).getId()));
                startActivityForResult(intent, 0);

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mNotificationList.get(mCurrentItemPosition).setViewed(true);
            mNotificationAdapter.notifyItem(mNotificationList, mCurrentItemPosition);
            getRedPointersRequest();
        }
    }
    private void getRedPointersRequest() {
        GetHasNotificationsNeedReadRequest getHasNotificationsNeedReadRequest = new GetHasNotificationsNeedReadRequest();
        getHasNotificationsNeedReadRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
         getHasNotificationsNeedReadRequest.startRequest(GetHasNotificationsNeedReadResponse.class, new HttpCallback<GetHasNotificationsNeedReadResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetHasNotificationsNeedReadResponse ret) {
                if (ret.getCode() == 0) {
                    if (ret.getData().isHasUnView()) {

                    } else {
                        ((MainActivity) getActivity()).hideNoticeRedDot();
                    }
                } else {
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
            }
        });
    }
}
