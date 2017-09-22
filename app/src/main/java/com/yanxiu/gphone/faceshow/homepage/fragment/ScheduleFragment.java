package com.yanxiu.gphone.faceshow.homepage.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.resource.ScheduleRequest;
import com.yanxiu.gphone.faceshow.http.resource.ScheduleResponse;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 首页tab里 “日程计划”Fragment
 */
public class ScheduleFragment extends FaceShowBaseFragment {
    private final static String TAG = ScheduleFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;

    @BindView(R.id.schedule_name)
    TextView schedule_name;
    @BindView(R.id.schedule_img)
    ImageView schedule_img;

    private UUID mRequestUUID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_schedule);
        mRootView.setErrorLayoutFullScreen();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.showLoadingView();
                getScheduleInfo();
            }
        });
        ButterKnife.bind(this, mRootView);
        getScheduleInfo();
        return mRootView;
    }

    private void getScheduleInfo() {
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.clazsId = "1";
        mRequestUUID = scheduleRequest.startRequest(ScheduleResponse.class, new HttpCallback<ScheduleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ScheduleResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret.getCode() == 0) {
                    if (ret.getData().getSchedules().getElements() != null && ret.getData().getSchedules().getElements().size() > 0) {
                        schedule_name.setText(ret.getData().getSchedules().getElements().get(0).getSubject());
                        Glide.with(getActivity()).load(ret.getData().getSchedules().getElements().get(0).getImageUrl()).into(schedule_img);
                    }
                    mRootView.hiddenOtherErrorView();
                    mRootView.hiddenNetErrorView();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mRequestUUID);
        }
    }
    @OnClick({R.id.schedule_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.schedule_img:
                openPicture();
                break;
        }
    }

    private void openPicture() {
    }
}
