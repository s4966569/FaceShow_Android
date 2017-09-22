package com.yanxiu.gphone.faceshow.course.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lufengqing on 2017/9/19.
 */

public class CourseIntroductionActivity extends FaceShowBaseActivity implements View.OnClickListener {
    private PublicLoadLayout mRootView;
    @BindView(R.id.title_layout_title)
    TextView title_layout_title;
    @BindView(R.id.title_layout_left_img)
    ImageView title_layout_left_img;
    @BindView(R.id.introduction_content)
    TextView introduction_content;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course_introduction);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        unbinder = ButterKnife.bind(this);
        title_layout_title.setText(R.string.course_jianjie);
        title_layout_left_img.setVisibility(View.VISIBLE);
        String courseIntroduction = getIntent().getStringExtra("COURSE_INTRODUCTION");
        introduction_content.setText(courseIntroduction);
        initListener();
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
//            case R.id.retry_button:
//                requestData();
//                break;
            default:
                break;
        }
    }

    /**
     */
    public static void invoke(Context context, String courseIntroduction) {
        Intent intent = new Intent(context, CourseIntroductionActivity.class);
        intent.putExtra("COURSE_INTRODUCTION", courseIntroduction);
        context.startActivity(intent);
    }
}

