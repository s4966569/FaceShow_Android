package com.yanxiu.gphone.faceshow.course.activity;

import android.app.Activity;
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
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDetailAdapter;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailResponse;

/**
 * 课程讨论
 */
public class CourseDiscussActivity extends FaceShowBaseActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private TextView mTitle;

    private RecyclerView mRecyclerView;
    private CourseDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course_discuss);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.title_layout_left_img);
        mTitle = (TextView) findViewById(R.id.title_layout_title);
        mTitle.setText("课程讨论");
        mRecyclerView = (RecyclerView) findViewById(R.id.discuss_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDetailAdapter(this, this);

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
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
        mRootView.showLoadingView();
        CourseDetailRequest courseDetailRequest = new CourseDetailRequest();
        courseDetailRequest.startRequest(CourseDetailResponse.class, new HttpCallback<CourseDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseDetailResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getStatus().getCode() == 0) {
                    mAdapter.setData(CourseDetailBean.getMockData().getCourseItem());
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


    /**
     * 跳转CourseActivity
     *
     * @param activity
     */
    public static void invoke(Activity activity) {
        Intent intent = new Intent(activity, CourseDiscussActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {

    }
}
