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
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.common.activity.PhotoActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.resource.ScheduleRequest;
import com.yanxiu.gphone.faceshow.http.resource.ScheduleResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 首页tab里 “日程计划”Fragment
 */
public class ScheduleFragment extends HomePageBaseFragment {
    private final static String TAG = ScheduleFragment.class.getSimpleName();
    private PublicLoadLayout mRootView;

    @BindView(R.id.schedule_name)
    TextView schedule_name;
    @BindView(R.id.schedule_img)
    ImageView schedule_img;

    private UUID mRequestUUID;
    private String mSchedualImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = new PublicLoadLayout(getActivity());
        mRootView.setContentView(R.layout.fragment_schedule);
        mRootView.setErrorLayoutFullScreen();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getScheduleInfo();
            }
        });
        ButterKnife.bind(this, mRootView);
        getScheduleInfo();
        return mRootView;
    }

    /**
     * 每次点击tab时，都要刷新数据
     */
    @Override
    public void refreshData() {
        getScheduleInfo();
    }

    private void getScheduleInfo() {
        mRootView.showLoadingView();
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.clazsId = UserInfo.getInstance().getInfo().getClassId();
        mRequestUUID = scheduleRequest.startRequest(ScheduleResponse.class, new HttpCallback<ScheduleResponse>() {
            @Override
            public void onSuccess(RequestBase request, ScheduleResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret != null && ret.getCode() == 0) {
                    if (ret.getData().getSchedules().getElements() != null && ret.getData().getSchedules().getElements().size() > 0) {
                        schedule_name.setText(ret.getData().getSchedules().getElements().get(0).getSubject());
                        mSchedualImageUrl = ret.getData().getSchedules().getElements().get(0).getImageUrl();
                        Glide.with(getActivity()).load(mSchedualImageUrl).into(schedule_img);
                        mRootView.hiddenOtherErrorView();
                        mRootView.hiddenNetErrorView();
                    } else {
                        mRootView.showOtherErrorView(getString(R.string.no_schedule_hint));
                    }
                } else {
                    mRootView.showOtherErrorView(getString(R.string.no_schedule_hint));
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
        if(mSchedualImageUrl != null) {
            ArrayList<String> list = new ArrayList<>();
            list.add(mSchedualImageUrl);
            PhotoActivity.LaunchActivity(getActivity(), list, 0, getActivity().hashCode(), PhotoActivity.DELETE_CANNOT);
        }
    }
}
