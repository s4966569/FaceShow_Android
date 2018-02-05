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
import com.yanxiu.gphone.faceshow.common.eventbus.InteractMessage;
import com.yanxiu.gphone.faceshow.common.listener.KeyboardChangeListener;
import com.yanxiu.gphone.faceshow.constant.Constants;
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

import de.greenrobot.event.EventBus;

import static android.view.View.GONE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_MULTI;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_SINGLE;
import static com.yanxiu.gphone.faceshow.course.bean.VoteBean.TYPE_TEXT;

/**
 * 评价页面或者叫讨论
 */
public class EvaluationActivity extends FaceShowBaseActivity implements View.OnClickListener, EvaluationAdapter.CanSubmitListener, KeyboardChangeListener.KeyBoardListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;
    private TextView mSubmit;
    private boolean mCanSubmit = false;

    private RecyclerView mRecyclerView;
    private EvaluationAdapter mAdapter;

    private VoteBean mData;
    public final static String STEP_ID = "stepid";
    private String mStepId;
    //eventbus 数据 start
    private int mHashCode;
    private int mPosition;
    //eventbus 数据 end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_evaluation);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        mStepId = getIntent().getStringExtra(STEP_ID);
        mHashCode = getIntent().getIntExtra(Constants.HASHCODE, -1);
        mPosition = getIntent().getIntExtra(Constants.POSITON, -1);
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
//        mSubmit.setEnabled(false);
        mSubmit.setVisibility(View.GONE);
        initSubmitView();
        mRecyclerView = (RecyclerView) findViewById(R.id.evlaution_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setFocusableInTouchMode(false);
        mAdapter = new EvaluationAdapter(this, this);
    }

    private void initSubmitView() {
        if (mCanSubmit) {
            mSubmit.setBackgroundColor(getResources().getColor(R.color.color_1da1f2));
            mSubmit.setTextColor(getResources().getColor(R.color.color_ffffff));
        } else {
            mSubmit.setBackgroundColor(getResources().getColor(R.color.color_a6abad));
            mSubmit.setTextColor(getResources().getColor(R.color.color_e2e2e2));
        }
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
                if (mCanSubmit) {
                    submitDialog();
                } else {
                    ToastUtil.showToast(this, getString(R.string.submit_no_complete_tip));
                }

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
                    } else {
                        mSubmit.setVisibility(View.VISIBLE);
                    }
                    mTitle.setText(getString(R.string.evaluation));
                    mAdapter.setTitle(ret.getData().getQuestionGroup().getTitle());
                    mAdapter.setDescription(ret.getData().getQuestionGroup().getDescription());
                    mAdapter.setData(ret.getData());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    mRootView.showOtherErrorView(ret.getError().getMessage());
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
                    postEnventBus();
                    invoke(EvaluationActivity.this, mStepId);
                    finish();
                } else {
                    ToastUtil.showToast(getApplication(), ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.finish();
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
        return jsonString(sb.toString());
    }

    /**
     * @param context
     */
    public static void invoke(Context context, String setpId) {
        Intent intent = new Intent(context, EvaluationActivity.class);
        intent.putExtra(STEP_ID, setpId);
        context.startActivity(intent);
    }

    /**
     * 首页的ProjectTaskFragment跳转，需要传ProjectTaskFragment的hashCode
     *
     * @param context
     */
    public static void invoke(Context context, String setpId, int position, int hashCode) {
        Intent intent = new Intent(context, EvaluationActivity.class);
        intent.putExtra(STEP_ID, setpId);
        intent.putExtra(Constants.HASHCODE, hashCode);
        intent.putExtra(Constants.POSITON, position);
        context.startActivity(intent);
    }

    @Override
    public void canSubmit(boolean is) {
//        mSubmit.setEnabled(is);
        mCanSubmit = is;
        initSubmitView();
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
//            mSubmit.setEnabled(allChoose);
            mCanSubmit = allChoose;
            initSubmitView();
        }
    }

    private void postEnventBus() {
        InteractMessage interactMessage = new InteractMessage();
        interactMessage.hascode = mHashCode;
        interactMessage.setpId = mStepId;
        interactMessage.position = mPosition;
        EventBus.getDefault().post(interactMessage);
    }
    /**
     * 将json格式字符串中返回的英文双引号改成中文的
     *
     * @param s
     * @return
     */
    private String jsonString(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                int count = 0;
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        count++;
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            if (count / 2 == 1) {
                                temp[j] = '”';
                            } else {
                                temp[j] = '“';
                            }
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
}
