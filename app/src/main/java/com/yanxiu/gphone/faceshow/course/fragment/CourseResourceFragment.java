package com.yanxiu.gphone.faceshow.course.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.course.GetCourseResourcesResponse;
import com.yanxiu.gphone.faceshow.course.adapter.CourseResourceAdapter;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseBean;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.recyclerview.RecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;
import com.yanxiu.gphone.faceshow.webView.WebViewForResourceActivity;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author frc on 17-11-8.
 */

public class CourseResourceFragment extends FaceShowBaseFragment {
    PublicLoadLayout mPublicLoadLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private UUID mGetCourseResourcesRequestUUID;

    private CourseBean data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPublicLoadLayout = new PublicLoadLayout(getContext());
        mPublicLoadLayout.setErrorLayoutFullScreen();
        mPublicLoadLayout.setContentView(inflater.inflate(R.layout.fragment_course_task_layout, container, false));
        unbinder = ButterKnife.bind(this, mPublicLoadLayout);

        data = (CourseBean) getArguments().get("data");
        if (data == null) {
            Log.i(TAG, "onCreateView: data is null");
            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
        } else {
            Log.i(TAG, "onCreateView: data not null ");
            if (data.getAttachmentInfos() != null && data.getAttachmentInfos().size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                CourseResourceAdapter courseResourceAdapter = new CourseResourceAdapter(data.getAttachmentInfos());
                mRecyclerView.setAdapter(courseResourceAdapter);
                courseResourceAdapter.addItemClickListener(mRecyclerViewItemClickListener);
            } else {
                mPublicLoadLayout.showOtherErrorView("暂无课程资源");
            }
        }
//        getCourseResources();
        return mPublicLoadLayout;
    }

    GetCourseResourcesResponse.DataBean mDataBean;



//    private void getCourseResources() {
//        String courseId = (getArguments() != null ? (String) getArguments().get("courseId") : null);
//        if (courseId != null) {
//            mPublicLoadLayout.showLoadingView();
//            GetCourseResourcesRequest getCourseResourcesRequest = new GetCourseResourcesRequest();
//            getCourseResourcesRequest.courseId = courseId;
//            mGetCourseResourcesRequestUUID = getCourseResourcesRequest.startRequest(GetCourseResourcesResponse.class, new HttpCallback<GetCourseResourcesResponse>() {
//                @Override
//                public void onSuccess(RequestBase request, final GetCourseResourcesResponse ret) {
//                    mPublicLoadLayout.finish();
//                    if (ResponseConfig.INT_SUCCESS == ret.getCode()) {
//                        if (ret.getData() != null && ret.getData().getResources() != null && ret.getData().getResources().getElements() != null && ret.getData().getResources().getElements().size() > 0) {
//                            mDataBean = ret.getData();
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                            mRecyclerView.setLayoutManager(linearLayoutManager);
//                            CourseResourceAdapter courseResourceAdapter = new CourseResourceAdapter(ret.getData().getResources().getElements());
//                            mRecyclerView.setAdapter(courseResourceAdapter);
//                            courseResourceAdapter.addItemClickListener(mRecyclerViewItemClickListener);
//                        } else {
//                            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
//                        }
//
//                    } else
//
//                    {
//                        mPublicLoadLayout.showOtherErrorView(ret.getError().getMessage());
//                    }
//                }
//
//                @Override
//                public void onFail(RequestBase request, Error error) {
//                    mPublicLoadLayout.finish();
//                    mPublicLoadLayout.showOtherErrorView(error.getMessage());
//                }
//            });
//
//        } else
//
//        {
//            mPublicLoadLayout.showOtherErrorView("暂无课程资源");
//        }
//
//    }

    private RecyclerViewItemClickListener mRecyclerViewItemClickListener = new RecyclerViewItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
//            GetCourseResourcesResponse.ElementsBean data = mDataBean.getResources().getElements().get(position);
            AttachmentInfosBean infosBean = data.getAttachmentInfos().get(position);

            if (TextUtils.equals(infosBean.getResType(),"1")&&!TextUtils.isEmpty(infosBean.getDownloadUrl())) {
                WebViewActivity.loadThisAct(getContext(), infosBean.getDownloadUrl(), infosBean.getResName());
            }else {
                if (infosBean.getResType() != null) {
//                    requestDetailData(data);
                    getResourceDetail(infosBean.getResId(),getActivity());
                } else {
                    ToastUtil.showToast(getContext(), "数据异常");
                }
            }

//            if (TextUtils.equals(data.getType(), "1") && !TextUtils.isEmpty(data.getUrl())) {
//                WebViewActivity.loadThisAct(getContext(), data.getUrl(), data.getResName());
//            } else {
//                if (data.getType() != null) {
//                    requestDetailData(data);
//                } else {
//                    ToastUtil.showToast(getContext(), "数据异常");
//                }
//            }
        }
    };
    private UUID mRequestUUID=null;
    private void getResourceDetail(String resId, final Activity activity) {
        mPublicLoadLayout.showLoadingView();
        ResourceDetailRequest resourceDetailRequest = new ResourceDetailRequest();
        resourceDetailRequest.resId = resId;
        mRequestUUID = resourceDetailRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                mPublicLoadLayout.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0) {
                    setIntent(ret.getData(),activity);
                } else {
                    ToastUtil.showToast(activity, ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mPublicLoadLayout.hiddenLoadingView();
                ToastUtil.showToast(activity,error.getMessage());
            }
        });
    }

    public void setIntent(ResourceDetailResponse.ResourceDetailBean data,final Activity activity) {
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
//    private void requestDetailData(String resId){
//        mPublicLoadLayout.showLoadingView();
//        ResourceDetailRequest resourceRequest = new ResourceDetailRequest();
//        resourceRequest.resId = String.valueOf(resId);
//        resourceRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
//            @Override
//            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
//                mPublicLoadLayout.finish();
//                if (ret != null && ret.getCode() == 0) {
//                    AttachmentInfosBean attachmentInfosBean = ret.getData().getAi();
//                    switch (attachmentInfosBean.getResType()) {
//                        case "word":
//                        case "doc":
//                        case "docx":
//                        case "xls":
//                        case "xlsx":
//                        case "excel":
//                        case "ppt":
//                        case "pptx":
//                        case "pdf":
//                        case "text":
//                        case "txt":
//                            PDFViewActivity.invoke(getActivity(), attachmentInfosBean.getResName(), attachmentInfosBean.getPreviewUrl());
//                            break;
//                        default:
//                            ToastUtil.showToast(getContext(), ret.getError().getMessage());
//                            break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFail(RequestBase request, Error error) {
//                mPublicLoadLayout.finish();
//                ToastUtil.showToast(getContext(), error.getMessage());
//            }
//        });
//    }
    /**
     * 获取资源详情数据
     */
    private void requestDetailData(GetCourseResourcesResponse.ElementsBean bean) {
//        requestDetailData(bean.getResId()+"");
//        mPublicLoadLayout.showLoadingView();
//        ResourceDetailRequest resourceRequest = new ResourceDetailRequest();
//        resourceRequest.resId = String.valueOf(bean.getResId());
//        resourceRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
//            @Override
//            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
//                mPublicLoadLayout.finish();
//                if (ret != null && ret.getCode() == 0) {
//                    AttachmentInfosBean attachmentInfosBean = ret.getData().getAi();
//                    switch (attachmentInfosBean.getResType()) {
//                        case "word":
//                        case "doc":
//                        case "docx":
//                        case "xls":
//                        case "xlsx":
//                        case "excel":
//                        case "ppt":
//                        case "pptx":
//                        case "pdf":
//                        case "text":
//                        case "txt":
//                            PDFViewActivity.invoke(getActivity(), attachmentInfosBean.getResName(), attachmentInfosBean.getPreviewUrl());
//                            break;
//                        default:
//                            ToastUtil.showToast(getContext(), ret.getError().getMessage());
//                            break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFail(RequestBase request, Error error) {
//                mPublicLoadLayout.finish();
//                ToastUtil.showToast(getContext(), error.getMessage());
//            }
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mGetCourseResourcesRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetCourseResourcesRequestUUID);
        }
    }
}
