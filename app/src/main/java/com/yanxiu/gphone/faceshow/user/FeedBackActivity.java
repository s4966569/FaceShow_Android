package com.yanxiu.gphone.faceshow.user;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.user.request.FeedBackRequest;
import com.yanxiu.gphone.faceshow.user.request.FeedBackResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author frc
 */
public class FeedBackActivity extends FaceShowBaseActivity {

    @BindView(R.id.title_layout_left_img)
    ImageView mTitleLayoutLeftImg;
    @BindView(R.id.title_layout_title)
    TextView mTitleLayoutTitle;
    @BindView(R.id.title_layout_right_txt)
    TextView mTitleLayoutRightTxt;
    @BindView(R.id.edt_feedback)
    EditText mEdtFeedback;
    private boolean canSubmit = false;
    PublicLoadLayout mPublicLoadLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPublicLoadLayout = new PublicLoadLayout(this);
        mPublicLoadLayout.setContentView(R.layout.activity_feed_back);
        setContentView(mPublicLoadLayout);
        ButterKnife.bind(this);
        mTitleLayoutLeftImg.setVisibility(View.VISIBLE);
        mTitleLayoutTitle.setText(R.string.feedback);
        mTitleLayoutTitle.setVisibility(View.VISIBLE);
        mTitleLayoutRightTxt.setText(R.string.submit);
        mTitleLayoutRightTxt.setTextColor(ContextCompat.getColor(this, R.color.color_999999));
        mTitleLayoutRightTxt.setVisibility(View.VISIBLE);
        mEdtFeedback.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEdtFeedback.getText().length() > 0) {
                canSubmit = true;
                mTitleLayoutRightTxt.setTextColor(ContextCompat.getColor(FeedBackActivity.this,R.color.color_1da1f2));
            } else {
                canSubmit = false;
                mTitleLayoutRightTxt.setTextColor(ContextCompat.getColor(FeedBackActivity.this,R.color.color_999999));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @OnClick({R.id.title_layout_left_img, R.id.title_layout_right_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_left_img:
                this.finish();
                break;
            case R.id.title_layout_right_txt:
                if (canSubmit){
                    submitFeedbackContent(mEdtFeedback.getText().toString());
                }else {
                    ToastUtil.showToast(FeedBackActivity.this,getString(R.string.feedback_content_cannot_be_empty));
                }
                break;
            default:
                break;
        }
    }

    private void submitFeedbackContent(String content) {
        mPublicLoadLayout.showLoadingView();
        FeedBackRequest feedBackRequest = new FeedBackRequest();
        feedBackRequest.content =content;
        feedBackRequest.startRequest(FeedBackResponse.class, new HttpCallback<FeedBackResponse>() {
            @Override
            public void onSuccess(RequestBase request, FeedBackResponse ret) {
                mPublicLoadLayout.finish();
                if (ret.getCode()==0){
                    ToastUtil.showToast(FeedBackActivity.this,"提交成功");
                    FeedBackActivity.this.finish();
                }else {
                    ToastUtil.showToast(FeedBackActivity.this,ret.getError().getMessage());
                }

            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.finish();
                ToastUtil.showToast(FeedBackActivity.this,error.getMessage());
            }
        });
    }
}
