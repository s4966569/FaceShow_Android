package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.activity.CourseActivity;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.HomeResourcesAdapter;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshow.http.resource.ResourceListRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceListResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 首页tab里 “资源”Fragment
 */
public class ResourcesFragment extends HomePageBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ResourcesFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;
    private LoadMoreRecyclerView mRecyclerView;
    private HomeResourcesAdapter mAdapter;

    List<ResourceListResponse.DataBean.ElementsBean> mResourceList = new ArrayList<>();
    private UUID mRequestUUID;
    /*从第几条开始加载数据*/
    private int mOffset = 0;
    /*每页多少条*/
    private String mPageSize = "10000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_resources);
        mRootView.setErrorLayoutFullScreen();
        initView();
        initListener();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResourceList.clear();
                mOffset = 0;
                getResourcesList();
            }
        });
        getResourcesList();
        return mRootView;
    }



    /**
     * 每次点击tab时，都要刷新数据
     */
    @Override
    public void refreshData() {
        mOffset = 0;
        getResourcesList();
    }

    private void initListener() {
    }

    private void initView() {
        mRecyclerView = (LoadMoreRecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HomeResourcesAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {

            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                getResourcesList();
            }

            @Override
            public void onLoadmoreComplte() {
                mRootView.hiddenLoadingView();
            }
        });
    }

    private void getResourcesList() {
        mRootView.showLoadingView();
        ResourceListRequest resourceListRequest = new ResourceListRequest();
        resourceListRequest.offset = String.valueOf(mOffset);
        resourceListRequest.pageSize = mPageSize;
        resourceListRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mRequestUUID = resourceListRequest.startRequest(ResourceListResponse.class, new HttpCallback<ResourceListResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceListResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        if (mOffset == 0) {
                            mResourceList.clear();
                        }
                        mResourceList.addAll(ret.getData().getElements());
                        mAdapter.updateData(mResourceList);
                        mRootView.hiddenOtherErrorView();
                        mRootView.hiddenNetErrorView();
                    } else {
                        mRootView.showOtherErrorView(getString(R.string.no_resource_hint));
                    }
                } else {
                    mRootView.showOtherErrorView(getString(R.string.no_resource_hint));
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                mRootView.showNetErrorView();
            }
        });
    }

    @Override
    public void onItemClick(int position, BaseBean baseBean) {
        ResourceListResponse.DataBean.ElementsBean bean = (ResourceListResponse.DataBean.ElementsBean) baseBean;
        getResourceDetail(bean.getResId());
    }

    private void getResourceDetail(String resId) {
        mRootView.showLoadingView();
        ResourceDetailRequest resourceDetailRequest = new ResourceDetailRequest();
        resourceDetailRequest.resId = resId;
        mRequestUUID = resourceDetailRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0) {
                    setIntent(ret.getData());
                    ((HomeFragment)getParentFragment()).hideResourceRedDot();
                } else {
                    ToastUtil.showToast(getActivity(), ret.getError().getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mRootView.hiddenLoadingView();
                ToastUtil.showToast(getActivity(), error.getMessage());
            }
        });
    }

    public void setIntent(ResourceDetailResponse.ResourceDetailBean data) {
        if (TextUtils.equals(data.getType(), "1") && !TextUtils.isEmpty(data.getUrl())) {
            WebViewActivity.loadThisAct(getActivity(), data.getUrl(), data.getResName());
        } else if (TextUtils.equals(data.getType(), "0")) {
            AttachmentInfosBean attachmentInfosBean = data.getAi();
            if (attachmentInfosBean != null && attachmentInfosBean.getResType() != null) {
                if (attachmentInfosBean.getResType().equals(AttachmentInfosBean.EXCEL) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PDF)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.PPT) || attachmentInfosBean.getResType().equals(AttachmentInfosBean.TEXT)
                        || attachmentInfosBean.getResType().equals(AttachmentInfosBean.WORD)) {
                    Intent intent;
                    PdfBean pdfbean = new PdfBean();
                    pdfbean.setName(attachmentInfosBean.getResName());
                    pdfbean.setUrl(attachmentInfosBean.getPreviewUrl());
                    pdfbean.setRecord(0);

                    intent = new Intent(getActivity(), PDFViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("pdfbean", pdfbean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
            } else {
                ToastUtil.showToast(getActivity(), "数据异常");
            }
        } else {
            ToastUtil.showToast(getActivity(), "数据异常");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mRequestUUID);
        }
    }
}
