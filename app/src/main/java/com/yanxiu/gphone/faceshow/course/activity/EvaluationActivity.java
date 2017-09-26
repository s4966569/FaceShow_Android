package com.yanxiu.gphone.faceshow.course.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.listener.KeyboardChangeListener;
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

import static android.view.View.GONE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_TEXT;

/**
 * 评价页面
 */
public class EvaluationActivity extends FaceShowBaseActivity implements View.OnClickListener, EvaluationAdapter.CanSubmitListener, KeyboardChangeListener.KeyBoardListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private TextView mSubmit;

    private RecyclerView mRecyclerView;
    private EvaluationAdapter mAdapter;

    private VoteBean mData;
    private final static String STEP_ID = "stepid";
    private String mStepId;

//    private boolean onlyLook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_evaluation);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        mStepId = getIntent().getStringExtra(STEP_ID);
        if (TextUtils.isEmpty(mStepId)) {
            mRootView.showOtherErrorView();
            return;
        }
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
        mRecyclerView.setFocusableInTouchMode(false);
        mAdapter = new EvaluationAdapter(this, this);
    }

    private void initListener() {
        mBackView.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                finish();
                break;
            case R.id.retry_button:
                requestData();
                break;
            case R.id.submit:
//                ToastUtil.showToast(getApplication(), "asdasd");
                submitDialog();
                break;
            default:
                break;
        }
    }

    private void requestData() {
        mRootView.showLoadingView();
        EvaluationRequest courseEvalutionlRequest = new EvaluationRequest();
        courseEvalutionlRequest.stepId = mStepId;
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
                    mTitle.setText(getString(R.string.evaluation));
                    mAdapter.setTitle(ret.getData().getQuestionGroup().getTitle());
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

    private void submitDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.submit_tip));
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitVote();
            }
        });
        dialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    private void submitVote() {
        mRootView.showLoadingView();
        SubmitVoteRequest submitVoteRequest = new SubmitVoteRequest();
        submitVoteRequest.method = SubmitVoteRequest.EVALUATION;
        submitVoteRequest.stepId = mStepId;
        submitVoteRequest.answers = makeAnswerString();
        submitVoteRequest.startRequest(FaceShowBaseResponse.class, new HttpCallback<FaceShowBaseResponse>() {
            @Override
            public void onSuccess(RequestBase request, FaceShowBaseResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    ToastUtil.showToast(getApplication(), getString(R.string.submit_success));
                    invoke(EvaluationActivity.this, mStepId);
                    finish();
                } else {
                    ToastUtil.showToast(getApplication(), getString(R.string.error_tip));
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
                ArrayList<String> answerList = bean.getAnswerList();
                for (int j = 0; j < answerList.size(); j++) {
                    int index = Integer.parseInt(answerList.get(j));
                    sb.append(bean.getVoteInfo().getVoteItems().get(index).getItemId());
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
    public static void invoke(Context context, String setpId) {
        Intent intent = new Intent(context, EvaluationActivity.class);
        intent.putExtra(STEP_ID, setpId);
        context.startActivity(intent);
    }

    @Override
    public void canSubmit(boolean is) {
        mSubmit.setEnabled(is);
    }

    private KeyboardChangeListener mKeyboardChangeListener;

    /**
     * call back
     *
     * @param isShow         true is show else hidden
     * @param keyboardHeight keyboard height
     */
    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        if (isShow) {
            mSubmit.setVisibility(GONE);
        } else {
            mSubmit.setVisibility(View.VISIBLE);
            boolean allChoose = true;
            for (int i = 0; i < mData.getQuestionGroup().getQuestions().size(); i++) {
                QusetionBean bean = mData.getQuestionGroup().getQuestions().get(i);
                if (bean.getQuestionType() == TYPE_TEXT && TextUtils.isEmpty(bean.getFeedBackText())) {
                    allChoose = false;
                    break;
                }
                if ((bean.getQuestionType() == TYPE_SINGLE || bean.getQuestionType() == TYPE_MULTI) && bean.getAnswerList().isEmpty()) {
                    allChoose = false;
                    break;
                }

            }
            mSubmit.setEnabled(allChoose);
        }
    }
}
