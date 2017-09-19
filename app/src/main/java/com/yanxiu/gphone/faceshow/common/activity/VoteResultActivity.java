package com.yanxiu.gphone.faceshow.common.activity;

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
import com.yanxiu.gphone.faceshow.common.adapter.VoteAdapter;
import com.yanxiu.gphone.faceshow.common.adapter.VoteResultAdapter;
import com.yanxiu.gphone.faceshow.common.bean.VoteBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.course.EvaluationRequest;
import com.yanxiu.gphone.faceshow.http.course.EvalutionResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import static android.view.View.GONE;

/**
 * 投票结果页面
 */
public class VoteResultActivity extends FaceShowBaseActivity implements View.OnClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private TextView mSubmit;

    private RecyclerView mRecyclerView;
    private VoteResultAdapter mAdapter;


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
        mSubmit.setVisibility(GONE);
        mTitle.setText("课程投票结果");
        mRecyclerView = (RecyclerView) findViewById(R.id.evlaution_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new VoteResultAdapter(this);

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
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
            default:
                break;
        }
    }

    private void requestData() {
        mRootView.showLoadingView();
        EvaluationRequest courseEvalutionlRequest = new EvaluationRequest();
        courseEvalutionlRequest.startRequest(EvalutionResponse.class, new HttpCallback<EvalutionResponse>() {
            @Override
            public void onSuccess(RequestBase request, EvalutionResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getStatus().getCode() == 0) {
                    mAdapter.setData(VoteBean.getMockData());
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

    public static void invoke(Context context) {
        Intent intent = new Intent(context, VoteResultActivity.class);
        context.startActivity(intent);
    }
}
