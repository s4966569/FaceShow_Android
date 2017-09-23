package com.yanxiu.gphone.faceshow.course.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDiscussAdapter;
import com.yanxiu.gphone.faceshow.course.bean.DiscussBean;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.course.DiscussRequest;
import com.yanxiu.gphone.faceshow.http.course.DiscussResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * 课程讨论
 */
public class CourseDiscussActivity extends FaceShowBaseActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private String mDiscussTitle;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreRecyclerView mRecyclerView;
    private CourseDiscussAdapter mAdapter;

    private int mTotalCount = 0;//数据的总量
    private int mNowTotalCount= 0;//当前以获取的数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course_discuss);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        mDiscussTitle = getIntent().getStringExtra("discussTitle");
        initView();
        initListener();
        requestData(false);
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.title_layout_left_img);
        mTitle = (TextView) findViewById(R.id.title_layout_title);
        mTitle.setText("课程讨论");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.discuss_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDiscussAdapter(this, this);

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                requestData(true);
            }
        });
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                ToastUtil.showToast(getApplicationContext(), "加载跟多");
                requestLoarMore();
            }

            @Override
            public void onLoadmoreComplte() {
                ToastUtil.showToast(getApplicationContext(), "加载更多结束");
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.retry_button:
                requestData(false);
                break;
            default:
                break;
        }
    }

    private void requestData(final boolean isRefreshIng) {
        if (!isRefreshIng)
            mRootView.showLoadingView();
        DiscussRequest discussRequest = new DiscussRequest();
        discussRequest.startRequest(DiscussResponse.class, new HttpCallback<DiscussResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscussResponse ret) {
                mRootView.finish();
                if (isRefreshIng)
                    mSwipeRefreshLayout.setRefreshing(false);
                if (ret != null && ret.getCode() == 0) {
                    mTotalCount = ret.getData().getTotalElements();
                    mNowTotalCount = ret.getData().getElements().size();
                    if(mNowTotalCount >= mTotalCount){
                        mRecyclerView.setLoadMoreEnable(false);
                    }else{
                        mRecyclerView.setLoadMoreEnable(true);
                    }
                    mAdapter.setData(ret.getData().getDataWithHeader(mDiscussTitle));
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                if (isRefreshIng)
                    mSwipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void requestLoarMore() {
        DiscussRequest discussRequest = new DiscussRequest();
        discussRequest.startRequest(DiscussResponse.class, new HttpCallback<DiscussResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscussResponse ret) {
                mRootView.finish();
                mRecyclerView.finishLoadMore();
                if (ret == null || ret.getCode() == 0) {
                    mAdapter.setData(ret.getData().getElements());

                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRootView.showOtherErrorView();
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
                mRecyclerView.finishLoadMore();

            }
        });

    }


    /**
     * 跳转CourseActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, String discussTitle) {
        Intent intent = new Intent(activity, CourseDiscussActivity.class);
        intent.putExtra("discussTitle", discussTitle);
        activity.startActivity(intent);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {

    }
}
