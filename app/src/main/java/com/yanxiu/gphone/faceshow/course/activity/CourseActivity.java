package com.yanxiu.gphone.faceshow.course.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDetailAdapter;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailResponse;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;

/**
 * 课程
 */
public class CourseActivity extends FaceShowBaseActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;

    private RecyclerView mRecyclerView;
    private CourseDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mBackView = (ImageView) findViewById(R.id.course_backView);
        mRecyclerView = (RecyclerView) findViewById(R.id.course_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDetailAdapter(this, this);

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
        Intent intent = new Intent(activity, CourseActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
//        CourseDetailBean.CourseDetailBeanItem itemBean = (CourseDetailBean.CourseDetailBeanItem) baseBean;
//        ToastUtil.showToast(this, "" + position);
        if (position == 2) {
            EvaluationActivity.invoke(this, false);
        }
        if (position == 3) {
            VoteActivity.invoke(this);
        }
        if (position == 4) {
            VoteResultActivity.invoke(this);
        }
        if (position == 0) {
            WebViewActivity.loadThisAct(CourseActivity.this, "http://www.sina.com");
        }
        if (position == 6) {
            CourseDiscussActivity.invoke(CourseActivity.this);
        }

        Intent intent;
        if (position == 5) {
            intent = new Intent(this, SpecialistIntroductionActivity.class);
            startActivity(intent);
        } else if (position == 1) {
            PdfBean pdfbean = new PdfBean();
            pdfbean.setName("pdfTest");
            pdfbean.setUrl("http://upload.ugc.yanxiu.com/doc/6bb6378e16add583a879bc94a2829127.pdf?from=107&rid=30089466");
            pdfbean.setRecord(0);

            intent = new Intent(this, PDFViewActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("pdfbean", pdfbean);
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }
}
