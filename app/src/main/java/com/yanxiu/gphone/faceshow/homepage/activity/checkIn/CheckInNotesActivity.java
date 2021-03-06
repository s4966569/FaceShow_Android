package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.RecyclerViewCanLoadMore;
import com.yanxiu.gphone.faceshow.homepage.CheckInSuccessEvent;
import com.yanxiu.gphone.faceshow.homepage.adapter.CheckInNotesAdapter;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesRequest;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;
import com.yanxiu.gphone.faceshow.util.FrcLogUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 签到记录页面
 * created by frc
 */
public class CheckInNotesActivity extends FaceShowBaseActivity {
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.loadMoreRecyclerView)
    RecyclerViewCanLoadMore loadMoreRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private PublicLoadLayout mRootView;
    private UUID mGetCheckInNotesRequestUUID;
    private CheckInNotesAdapter mCheckInNotesAdapter;
    private int mOffset = 0;
    private List<GetCheckInNotesResponse.Element> mCheckInNotesList = new ArrayList<>();
    private int mTotalElements;

    RecyclerViewCanLoadMore.OnLoadMoreListener loadMoreListener = new RecyclerViewCanLoadMore.OnLoadMoreListener() {
        @Override
        public void onLoadMore(RecyclerView recyclerView, int newState, int lastVisibleItem) {
            if (mCheckInNotesList.size() < mTotalElements) {
                mRootView.showLoadingView();
                mOffset = mCheckInNotesList.size();
                getCheckInNotes();
            }
        }
    };


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mOffset = 0;
            getCheckInNotes();
        }
    };

    private View.OnClickListener retryClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mRootView.showLoadingView();
            getCheckInNotes();
        }
    };

    public static void toThisAct(Activity activity) {
        activity.startActivity(new Intent(activity, CheckInNotesActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_check_in_notes);
        setContentView(mRootView);
        ButterKnife.bind(this);
        //用来接受签到成功发来的通知CheckInSuccessActivity
        EventBus.getDefault().register(this);
        mRootView.setRetryButtonOnclickListener(retryClick);
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        tvTitle.setText(R.string.check_in_notes);
        mRootView.showLoadingView();
        getCheckInNotes();
        mCheckInNotesAdapter = new CheckInNotesAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        loadMoreRecyclerView.setLayoutManager(linearLayoutManager);
        loadMoreRecyclerView.setAdapter(mCheckInNotesAdapter);
        loadMoreRecyclerView.addLoadMoreListener(loadMoreListener);
//        loadMoreRecyclerView.setFocusableInTouchMode(false);//这个牛逼了 不要删除
    }

    private void getCheckInNotes() {
        GetCheckInNotesRequest getCheckInNotesRequest = new GetCheckInNotesRequest();
        getCheckInNotesRequest.pageSize = "10";
        getCheckInNotesRequest.offset = String.valueOf(mOffset);
        mGetCheckInNotesRequestUUID = getCheckInNotesRequest.startRequest(GetCheckInNotesResponse.class, new HttpCallback<GetCheckInNotesResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetCheckInNotesResponse ret) {
                mRootView.hiddenLoadingView();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                mRootView.hiddenOtherErrorView();
                mRootView.hiddenNetErrorView();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData() != null && ret.getData().getElements() != null) {
                        mTotalElements = ret.getData().getTotalElements();
                        if (ret.getData().getElements().size() > 0) {
                            if (mOffset == 0) {
                                mCheckInNotesList.clear();
                            }
                            mCheckInNotesList.addAll(ret.getData().getElements());
                        } else {
                            if (mOffset == 0) {
                                if (mCheckInNotesList.size() > 0) {
                                    ToastUtil.showToast(FaceShowApplication.getContext(), "刷新失败");
                                } else {
                                    mRootView.showOtherErrorView("无签到记录");
                                }
                            }
                        }
                    }
                    mCheckInNotesAdapter.update(mCheckInNotesList);
                } else {
                    if (mCheckInNotesList.size() <= 0) {
                        mRootView.showOtherErrorView("数据异常");
                    }
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                mRootView.hiddenLoadingView();
                if (mCheckInNotesList.size() <= 0) {
                    mRootView.showNetErrorView();
                } else {
                    ToastUtil.showToast(CheckInNotesActivity.this, error.getMessage());
                }

            }
        });
    }

    public void onEventMainThread(CheckInSuccessEvent event) {
        mCheckInNotesAdapter.reFreshItem(event.getMsg());
    }

    @Override
    protected void onDestroy() {
        if (mGetCheckInNotesRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCheckInNotesRequestUUID);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.img_left)
    public void onViewClicked() {
        this.finish();
    }
}
