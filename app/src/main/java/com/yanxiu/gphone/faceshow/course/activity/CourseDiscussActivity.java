package com.yanxiu.gphone.faceshow.course.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleResponse;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDiscussAdapter;
import com.yanxiu.gphone.faceshow.course.bean.DiscussBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.course.DiscussCommentRequest;
import com.yanxiu.gphone.faceshow.http.course.DiscussCommentResponse;
import com.yanxiu.gphone.faceshow.http.course.DiscussRequest;
import com.yanxiu.gphone.faceshow.http.course.DiscussResponse;
import com.yanxiu.gphone.faceshow.http.course.DiscussResponseBean;
import com.yanxiu.gphone.faceshow.http.course.DiscussSaveRequest;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * 课程讨论
 */
public class CourseDiscussActivity extends FaceShowBaseActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private EditText mEd_comment;
    private TextView mTitle;
    private String mDiscussTitle;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadMoreRecyclerView mRecyclerView;
    private CourseDiscussAdapter mAdapter;

    private int mTotalCount = 0;//数据的总量
    private int mNowTotalCount = 0;//当前以获取的数量

    private InteractStepsBean stepsBean;

    private DiscussResponseBean mData;
    private TextView mTvSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course_discuss);
        mRootView.setRetryButtonOnclickListener(this);
        stepsBean = (InteractStepsBean) getIntent().getSerializableExtra("InteractStepsBean");
        setContentView(mRootView);
        initView();
        initListener();
        requestTitleData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.VISIBLE);
        mEd_comment = (EditText) findViewById(R.id.ed_comment);
        mTitle = (TextView) findViewById(R.id.title_layout_title);
        mTitle.setText("课程讨论");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.discuss_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDiscussAdapter(this, this);
        mTvSure = (TextView) findViewById(R.id.tv_sure);

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
        mTvSure.setOnClickListener(this);
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
                if (mNowTotalCount < mTotalCount) {
                    requestLoarMore();
                } else {
                    mRecyclerView.finishLoadMore();
                    ToastUtil.showToast(CourseDiscussActivity.this, "没有更多数据了");
                }

            }

            @Override
            public void onLoadmoreComplte() {
            }
        });
        mEd_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mTvSure.setEnabled(false);
                } else {
                    mTvSure.setEnabled(true);
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                hiddenInputMethod();
                finish();
                break;
            case R.id.retry_button:
                if (TextUtils.isEmpty(mDiscussTitle)) {
                    requestTitleData();
                } else {
                    requestData(false);
                }

                break;
            case R.id.tv_sure:
                String comment = mEd_comment.getText().toString();
                if (!TextUtils.isEmpty(comment.trim())) {
                    submitData(comment);
                } else {
                    ToastUtil.showToast(getApplicationContext(), "提交内容不能为空");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取讨论标题数据
     */
    private void requestTitleData() {
        mRootView.showLoadingView();
        DiscussCommentRequest discussRequest = new DiscussCommentRequest();
        discussRequest.stepId = stepsBean.getStepId();
        discussRequest.startRequest(DiscussCommentResponse.class, new HttpCallback<DiscussCommentResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscussCommentResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    mDiscussTitle = ret.getData().getDescription();
                    requestData(false);
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

    private void requestData(final boolean isRefreshIng) {
        if (!isRefreshIng) {
            mRootView.showLoadingView();
        }
        DiscussRequest discussRequest = new DiscussRequest();
        discussRequest.stepId = stepsBean.getStepId();
        discussRequest.startRequest(DiscussResponse.class, new HttpCallback<DiscussResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscussResponse ret) {
                mRootView.finish();
                if (isRefreshIng) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (ret != null && ret.getCode() == 0) {
                    mData = ret.getData();
                    mTotalCount = ret.getData().getTotalElements();
                    mNowTotalCount = ret.getData().getElements().size();
                    if (mNowTotalCount >= mTotalCount) {
                        mRecyclerView.setLoadMoreEnable(false);
                    } else {
                        mRecyclerView.setLoadMoreEnable(true);
                    }
                    mAdapter.setData(ret.getData().getDataWithHeader(mDiscussTitle), ret.getCurrentTime());
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
        mRootView.showLoadingView();
        DiscussRequest discussRequest = new DiscussRequest();
        discussRequest.stepId = stepsBean.getStepId();
        discussRequest.id = mData.getCallbackValue();
        discussRequest.startRequest(DiscussResponse.class, new HttpCallback<DiscussResponse>() {
            @Override
            public void onSuccess(RequestBase request, DiscussResponse ret) {
                mRootView.finish();
                mRecyclerView.finishLoadMore();
                if (ret == null || ret.getCode() == 0) {
                    if (mNowTotalCount < mTotalCount) {
                        mAdapter.addData(ret.getData().getElements(), ret.getCurrentTime());
                        mNowTotalCount += ret.getData().getElements().size();
                    } else {
                        ToastUtil.showToast(CourseDiscussActivity.this, "没有更多数据了");
                    }
//                    mRecyclerView.setAdapter(mAdapter);
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
     * 提交讨论数据
     */
    private void submitData(final String content) {
        mRootView.showLoadingView();
        DiscussSaveRequest discussSaveRequest = new DiscussSaveRequest();
        discussSaveRequest.content = content;
        discussSaveRequest.stepId = stepsBean.getStepId();
        discussSaveRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mRootView.finish();
                hiddenInputMethod();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(CourseDiscussActivity.this, "提交成功");
                } else {
                    ToastUtil.showToast(CourseDiscussActivity.this, ret.getError().getMessage());
                }
                requestData(false);
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(CourseDiscussActivity.this, error.getMessage());
                hiddenInputMethod();
            }
        });

    }

    private void hiddenInputMethod() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mEd_comment.setText("");
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 跳转CourseActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity, InteractStepsBean interactStepsBean) {
        Intent intent = new Intent(activity, CourseDiscussActivity.class);
        intent.putExtra("InteractStepsBean", interactStepsBean);
        activity.startActivity(intent);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {

    }


}
