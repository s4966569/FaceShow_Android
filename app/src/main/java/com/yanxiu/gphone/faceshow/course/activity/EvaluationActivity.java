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
import com.yanxiu.gphone.faceshow.course.adapter.EvaluationAdapter;
import com.yanxiu.gphone.faceshow.course.bean.EvaluationBean;
import com.yanxiu.gphone.faceshow.course.bean.QusetionBean;
import com.yanxiu.gphone.faceshow.course.bean.VoteBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.http.course.EvaluationRequest;
import com.yanxiu.gphone.faceshow.http.course.EvalutionResponse;
import com.yanxiu.gphone.faceshow.http.course.SubmitVoteRequest;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;

import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_TEXT;

/**
 * 评价页面
 */
public class EvaluationActivity extends FaceShowBaseActivity implements View.OnClickListener, EvaluationAdapter.CanSubmitListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private TextView mSubmit;

    private RecyclerView mRecyclerView;
    private EvaluationAdapter mAdapter;

    private VoteBean mData;

//    private boolean onlyLook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_evaluation);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
//        onlyLook = getIntent().getBooleanExtra("onlyLook", false);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.evlaution_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EvaluationAdapter(this, this);

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
//                ToastUtil.showToast(getApplication(), "asdasd");
                submitVote();
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
                if (ret == null || ret.getCode() == 0) {
                    mData = ret.getData();
                    if (ret.getData().isAnswer()) {
                        mSubmit.setVisibility(View.GONE);
//                        mTitle.setText("我的课程评价");
                    } else {
                        mSubmit.setVisibility(View.VISIBLE);
//                        mTitle.setText("课程评价");
                    }
                    mTitle.setText(ret.getData().getQuestionGroup().getTitle());
                    mAdapter.setData(ret.getData());
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

    private void submitVote() {
        mRootView.showLoadingView();
        SubmitVoteRequest voteRequest = new SubmitVoteRequest();
        voteRequest.method = SubmitVoteRequest.EVALUATION;
        voteRequest.stepId = "";
        voteRequest.answers = makeAnswerString();
        voteRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mRootView.finish();
                if (ret != null || ret.getCode() == 0) {
                    finish();
                } else {
//                    ToastUtil.showToast(getApplication(), ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
//                ToastUtil.showToast(getApplication(), error.getMessage());
            }
        });

    }

    private String makeAnswerString() {
        final String questionId = "\"questionId\":";
        final String answers = "\"answers\":";
        StringBuffer sb = new StringBuffer("[");
        ArrayList<QusetionBean> questionBeanList = mData.getQuestionGroup().getQuestions();
        for (int i = 0; i < questionBeanList.size(); i++) {
            QusetionBean bean = questionBeanList.get(i);
            sb.append("{");
            sb.append(questionId);
            sb.append(bean.getId());
            sb.append(",");
            sb.append(answers);
            sb.append("\"");
            if (bean.getQuestionType() == TYPE_SINGLE || bean.getQuestionType() == TYPE_MULTI) {
                ArrayList answerList = bean.getAnswerList();
                for (int j = 0; j < answerList.size(); j++) {
                    sb.append(answerList.get(j));
                    if (j < answerList.size() - 1) { //不是最后一个
                        sb.append(",");
                    }
                }

            } else if (bean.getQuestionType() == TYPE_TEXT) {
                sb.append(bean.getFeedBackText());
            } else {
                //错误
            }
            sb.append("\"");
            sb.append("}");
            if (i < questionBeanList.size() - 1) { //不是最后一个
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * @param context
     */
    public static void invoke(Context context) {
        Intent intent = new Intent(context, EvaluationActivity.class);
//        intent.putExtra("onlyLook", onlyLook);
        context.startActivity(intent);
    }

    @Override
    public void canSubmit(boolean is) {
        mSubmit.setEnabled(is);
    }
}
