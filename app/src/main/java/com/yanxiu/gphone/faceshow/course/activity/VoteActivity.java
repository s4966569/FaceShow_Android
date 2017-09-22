package com.yanxiu.gphone.faceshow.course.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.course.adapter.VoteAdapter;
import com.yanxiu.gphone.faceshow.course.adapter.VoteResultAdapter;
import com.yanxiu.gphone.faceshow.course.bean.VoteBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.course.VoteRequest;
import com.yanxiu.gphone.faceshow.http.course.VoteResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

/**
 * 投票页面
 */
public class VoteActivity extends FaceShowBaseActivity implements View.OnClickListener, VoteAdapter.CanSubmitListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private TextView mSubmit;

    private RecyclerView mRecyclerView;
    private VoteAdapter mVoteAdapter;
    private VoteResultAdapter mVoteResultAdapter;
    private boolean mIsAnswer;//true:已投票-投票结果 ；fasle:未投票


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_vote);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.title_layout_left_img);
        mTitle = (TextView) findViewById(R.id.title_layout_title);
        mSubmit = (TextView) findViewById(R.id.submit);
        mBackView.setVisibility(View.VISIBLE);
        mSubmit.setEnabled(false);
        mSubmit.setVisibility(View.GONE);
        mTitle.setText("课程投票");
        mRecyclerView = (RecyclerView) findViewById(R.id.evlaution_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.course_backView:
                finish();
                break;
            case R.id.retry_button:
                requestData();
                break;
            case R.id.submit:
                ToastUtil.showToast(getApplication(), "asdasd");
                break;
            default:
                break;
        }
    }

    private void requestData() {
        mRootView.showLoadingView();
        VoteRequest voteRequest = new VoteRequest();
        voteRequest.startRequest(VoteResponse.class, new HttpCallback<VoteResponse>() {
            @Override
            public void onSuccess(RequestBase request, VoteResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getCode() == 0) {
                    mIsAnswer = ret.getData().isAnswer();
                    mTitle.setText(ret.getData().getQuestionGroup().getTitle());
                    initAdapter(ret.getData());
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

    //根据是否已答，确定显示哪个adapter
    private void initAdapter(VoteBean data) {
        if (mIsAnswer) {
            mSubmit.setVisibility(View.GONE);
            mVoteResultAdapter = new VoteResultAdapter(this);
            mVoteResultAdapter.setData(data);
            mRecyclerView.setAdapter(mVoteResultAdapter);
            mSubmit.setVisibility(View.GONE);
        } else {
            mSubmit.setVisibility(View.VISIBLE);
            mVoteAdapter = new VoteAdapter(this, this);
            mVoteAdapter.setData(data);
            mRecyclerView.setAdapter(mVoteAdapter);
            mSubmit.setVisibility(View.VISIBLE);
        }
    }

    public static void invoke(Context context) {
        Intent intent = new Intent(context, VoteActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void canSubmit(boolean is) {
        mSubmit.setEnabled(is);
    }
}
