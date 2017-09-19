package com.yanxiu.gphone.faceshow.course.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 专家介绍
 * Created by lufengqing on 2017/9/19.
 */

public class SpecialistIntroductionActivity extends FaceShowBaseActivity implements View.OnClickListener {
    private PublicLoadLayout mRootView;
    @BindView(R.id.title_layout_title)
    TextView title_layout_title;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;
    @BindView(R.id.specialist_name)
    TextView specialist_name;
    @BindView(R.id.introduction_content)
    TextView introduction_content;
    @BindView(R.id.specialist_backimg)
    ImageView specialist_backimg;
    @BindView(R.id.specialist_img)
    ImageView specialist_img;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_specialist_introduction);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        title_layout_title.setText(R.string.specialist_title);
        title_layout_left_img.setVisibility(View.VISIBLE);
        initListener();
        requestData();
    }

    private void initListener() {
        title_layout_left_img.setOnClickListener(this);
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
            default:
                break;
        }
    }

    private void requestData() {
    }
}
