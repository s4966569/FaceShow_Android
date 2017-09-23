package com.yanxiu.gphone.faceshow.course.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDetailAdapter;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.QRCodeCheckInActivity;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailResponse;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.attachment;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.interact;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.lecturer;

/**
 * 课程详情
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
        courseDetailRequest.courseId = "";
        courseDetailRequest.startRequest(CourseDetailResponse.class, new HttpCallback<CourseDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseDetailResponse ret) {
                mRootView.finish();
                if (ret == null || ret.getCode() == 0) {
                    mAdapter.setData(ret.getCourseDetailData());
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
        CourseDetailItemBean itemBean = (CourseDetailItemBean) baseBean;
        switch (itemBean.getMyDataType()) {
            case lecturer:
                LecturerInfosBean lecturerInfosBean = (LecturerInfosBean) itemBean;
                SpecialistIntroductionActivity.invoke(CourseActivity.this, lecturerInfosBean);
                break;
            case attachment:
                AttachmentInfosBean attachmentInfosBean = (AttachmentInfosBean) itemBean;
                if (attachmentInfosBean.getResType().equals(AttachmentInfosBean.EXCEL) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PDF)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PPT) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.TEXT)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.WORD)) {
                    Intent intent;
                    PdfBean pdfbean = new PdfBean();
//                    pdfbean.setName("pdfTest");
//                    pdfbean.setUrl("http://upload.ugc.yanxiu.com/doc/6bb6378e16add583a879bc94a2829127.pdf?from=107&rid=30089466");
                    pdfbean.setName(attachmentInfosBean.getResName());
                    pdfbean.setUrl(attachmentInfosBean.getPreviewUrl());
                    pdfbean.setRecord(0);

                    intent = new Intent(this, PDFViewActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("pdfbean", pdfbean);
                    intent.putExtras(mBundle);
                    startActivity(intent);

                } else {
                    WebViewActivity.loadThisAct(CourseActivity.this, attachmentInfosBean.getPreviewUrl());
                }

                break;
            case interact:
                InteractStepsBean interactStepsBean = (InteractStepsBean) itemBean;
                switch (interactStepsBean.getInteractType()) {
                    case InteractStepsBean.VOTE:
                        if (InteractStepsBean.NO_FINISH.equals(interactStepsBean.getStepFinished())) {
                            VoteActivity.invoke(this);
                        } else if (InteractStepsBean.FINISH.equals(interactStepsBean.getStepFinished())) {
                            VoteResultActivity.invoke(this);
                        } else {
                            ToastUtil.showToast(this, interactStepsBean.getStepFinished());
                        }
                        break;
                    case InteractStepsBean.DISCUSS:
                        CourseDiscussActivity.invoke(CourseActivity.this, interactStepsBean.getInteractName());
                        break;
                    case InteractStepsBean.QUESTIONNAIRES:
                        EvaluationActivity.invoke(this);
                        break;
                    case InteractStepsBean.CHECK_IN:
                        QRCodeCheckInActivity.toThisAct(this);
                        break;
                }

                break;
            default:
                break;
        }

    }
}
