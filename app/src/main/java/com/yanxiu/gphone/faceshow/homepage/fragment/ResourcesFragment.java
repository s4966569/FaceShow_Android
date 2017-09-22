package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.common.activity.PDFViewActivity;
import com.yanxiu.gphone.faceshow.common.bean.PdfBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.homepage.adapter.HomeResourcesAdapter;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceDetailResponse;
import com.yanxiu.gphone.faceshow.http.resource.ResourceListRequest;
import com.yanxiu.gphone.faceshow.http.resource.ResourceListResponse;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 首页tab里 “资源”Fragment
 */
public class ResourcesFragment extends FaceShowBaseFragment implements OnRecyclerViewItemClickListener {
    private final static String TAG = ResourcesFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;
    private LoadMoreRecyclerView mRecyclerView;
    private HomeResourcesAdapter mAdapter;

    List<ResourceListResponse.DataBean.ElementsBean> mResourceList = new ArrayList<>();
    private UUID mRequestUUID;
    /*从第几条开始加载数据*/
    private int mOffset = 0;
    /*每页多少条*/
    private String mPageSize = "10";

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
                mRootView.showLoadingView();
                mResourceList.clear();
                mOffset = 0;
                getResourcesList();
            }
        });
        return mRootView;
    }

    private void initListener() {
    }

    private void initView() {
        mRecyclerView = (LoadMoreRecyclerView) mRootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HomeResourcesAdapter(getActivity(), this);
//        mAdapter.setData(ResourceBean.getMockData());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {

            @Override
            public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
                mRootView.showLoadingView();
                getResourcesList();
            }

            @Override
            public void onLoadmoreComplte() {
                mRootView.hiddenLoadingView();
            }
        });
        mRootView.showLoadingView();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getResourcesList();
    }

    private void getResourcesList() {
        ResourceListRequest resourceListRequest = new ResourceListRequest();
        resourceListRequest.offset = String.valueOf(mOffset);
        resourceListRequest.pageSize = mPageSize;
        resourceListRequest.clazsId = "1";
        mRequestUUID = resourceListRequest.startRequest(ResourceListResponse.class, new HttpCallback<ResourceListResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceListResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret.getCode() == 0) {
                    if (ret.getData().getElements() != null && ret.getData().getElements().size() > 0) {
                        if (mOffset == 0) {
                            mResourceList.clear();
                        }
                        mResourceList.addAll(ret.getData().getElements());
                    }
                    mAdapter.updateData(mResourceList);
                    mRootView.hiddenOtherErrorView();
                    mRootView.hiddenNetErrorView();
                } else {
                    if (mResourceList.size() <= 0) {
                        mRootView.showOtherErrorView();
                    }
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
        ToastUtil.showToast(getActivity(), position + "");
        ResourceListResponse.DataBean.ElementsBean bean = (ResourceListResponse.DataBean.ElementsBean) baseBean;
        getResourceDetail(bean.getResId());
    }

    private void getResourceDetail(String resId) {
        ResourceDetailRequest resourceDetailRequest = new ResourceDetailRequest();
        resourceDetailRequest.resId = resId;
        mRequestUUID = resourceDetailRequest.startRequest(ResourceDetailResponse.class, new HttpCallback<ResourceDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, ResourceDetailResponse ret) {
                if (ret.getCode() == 0 && ret.getData().getUrl() != null && ret.getData().getSuffix() != null) {
                    setIntent(ret.getData());
                } else {
                    ToastUtil.showToast(getActivity(), "数据异常");
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                ToastUtil.showToast(getActivity(), "网络异常");
            }
        });
    }

    public void setIntent(ResourceDetailResponse.ResourceDetailBean data) {
        PdfBean pdfbean = new PdfBean();
        pdfbean.setName(data.getResName());
//        pdfbean.setUrl("http://upload.ugc.yanxiu.com/doc/6bb6378e16add583a879bc94a2829127.pdf?from=107&rid=30089466");
        pdfbean.setUrl(data.getUrl());
        pdfbean.setRecord(0);

        Intent intent = new Intent(getActivity(), PDFViewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("pdfbean", pdfbean);
        intent.putExtras(mBundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mRequestUUID);
        }
    }
}
