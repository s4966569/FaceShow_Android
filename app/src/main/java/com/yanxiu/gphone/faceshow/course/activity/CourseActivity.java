package com.yanxiu.gphone.faceshow.course.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.adapter.CourseDetailAdapter;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailRequest;
import com.yanxiu.gphone.faceshow.http.course.CourseDetailResponse;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.webView.WebViewForResourceActivity;

import java.util.UUID;

import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.attachment;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.interact;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.lecturer;

/**
 * 课程详情
 */
public class CourseActivity extends FaceShowBaseActivity implements View.OnClickListener, OnRecyclerViewItemClickListener {

    private PublicLoadLayout mRootView;
    private ImageView mBackView;
    private View mTtitlt_bar_layout;

    private RecyclerView mRecyclerView;
    private CourseDetailAdapter mAdapter;

    public final static String COURSE_ID = "course_id";
    private String mCourseid;
    private float mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_course);
        mRootView.setRetryButtonOnclickListener(this);
        setContentView(mRootView);
        mCourseid = getIntent().getStringExtra(COURSE_ID);
        if (TextUtils.isEmpty(mCourseid)) {
            mRootView.showOtherErrorView();
            return;
        }
        initView();
        initListener();
        requestData();
    }

    private void initView() {
        mHeight = getResources().getDimensionPixelSize(R.dimen.course_img_bg_height);
        mTtitlt_bar_layout = findViewById(R.id.titlt_bar_layout);
        mTtitlt_bar_layout.getBackground().mutate();//因为加了alpha渐变，不加该方法，会在个别手机上造成闪屏。
        mTtitlt_bar_layout.setVisibility(View.GONE);
        mBackView = (ImageView) findViewById(R.id.course_backView);
        mRecyclerView = (RecyclerView) findViewById(R.id.course_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CourseDetailAdapter(this, this);

    }

    private void initListener() {
        mBackView.setOnClickListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                long y = mRecyclerView.getChildAt(0).getTop();
                if (Math.abs(y) <= mHeight - 20) {
                    float ratio = Math.abs(y) / (mHeight - 20f);
                    Log.e("dyf", "onScrolled: " + ratio);
                    int alpha = (int) (ratio * 255);
                    mTtitlt_bar_layout.getBackground().setAlpha(alpha);
                }else{
                    mTtitlt_bar_layout.getBackground().setAlpha(255);
                }

            }
        });
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
        courseDetailRequest.courseId = mCourseid;
        courseDetailRequest.startRequest(CourseDetailResponse.class, new HttpCallback<CourseDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, CourseDetailResponse ret) {
                mRootView.finish();
                if (ret != null && ret.getCode() == 0) {
                    mTtitlt_bar_layout.setVisibility(View.VISIBLE);
                    mTtitlt_bar_layout.getBackground().setAlpha(0);
                    mAdapter.setData(ret.getCourseDetailData());
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


    /**
     * 跳转CourseActivity
     *
     * @param activity
     */
    public static void invoke(Context activity, String courseId) {
        Intent intent = new Intent(activity, CourseActivity.class);
        intent.putExtra(COURSE_ID, courseId);
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
                getResourceDetail(attachmentInfosBean.getResId(),CourseActivity.this);
                break;
            case interact:
                InteractStepsBean interactStepsBean = (InteractStepsBean) itemBean;
                switch (interactStepsBean.getInteractType()) {
                    case InteractStepsBean.VOTE:
                        VoteActivity.invoke(this, interactStepsBean.getStepId());
                        break;
                    case InteractStepsBean.DISCUSS:
                        CourseDiscussActivity.invoke(CourseActivity.this, interactStepsBean);
                        break;
                    case InteractStepsBean.QUESTIONNAIRES:
                        EvaluationActivity.invoke(this, interactStepsBean.getStepId());
                        break;
                    case InteractStepsBean.CHECK_IN:
                        CheckInByQRActivity.toThisAct(this);
                        break;
                }

                break;
            default:
                break;
        }

    }


    private UUID mRequestUUID=null;
    private void getResourceDetail(String resId, final CourseActivity activity) {
        mRootView.showLoadingView();
        ResourceDetailRequest resourceDetailRequest = new ResourceDetailRequest();
        resourceDetailRequest.resId = resId;
        mRequestUUID = resourceDetailRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0) {
                    setIntent(ret.getData(),activity);
                } else {
                    ToastUtil.showToast(activity, ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(activity,error.getMessage());
            }
        });
    }

    public void setIntent(ResourceDetailResponse.ResourceDetailBean data,final CourseActivity activity) {
        if (TextUtils.equals(data.getType(), "1") && !TextUtils.isEmpty(data.getUrl())) {
            WebViewForResourceActivity.lunch(activity,data.getUrl());
        } else if(TextUtils.equals(data.getType(), "0")) {
            AttachmentInfosBean attachmentInfosBean = data.getAi();
            if (attachmentInfosBean != null && attachmentInfosBean.getResType() != null) {
                if(attachmentInfosBean.getResType().equals(AttachmentInfosBean.EXCEL) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PDF)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PPT) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.TEXT)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.WORD)) {
                    Intent intent;
                    PdfBean pdfbean = new PdfBean();
                    pdfbean.setName(attachmentInfosBean.getResName());
                    pdfbean.setUrl(attachmentInfosBean.getPreviewUrl());
                    pdfbean.setRecord(0);

                    intent = new Intent(activity, PDFViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pdfbean", pdfbean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
            } else {
                ToastUtil.showToast(activity, "数据异常");
            }
        } else {
            ToastUtil.showToast(activity, "数据异常");
        }
    }
}
