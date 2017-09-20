package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInDetailRequest;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInDetailResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到详情页
 * created by frc
 */
public class CheckInDetailActivity extends FaceShowBaseActivity {

    private static String mCheckInStatue;
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_check_in_name)
    TextView tvCheckInName;
    @BindView(R.id.tv_check_in_time_plan)
    TextView tvCheckInTimePlan;
    @BindView(R.id.tv_check_in_statue)
    TextView tvCheckInStatue;
    @BindView(R.id.tv_check_in_here)
    TextView tvCheckInHere;
    @BindView(R.id.tv_check_in)
    TextView tvCheckIn;
    @BindView(R.id.tv_check_in_time)
    TextView tvCheckInTime;
    @BindView(R.id.tv_check_in_time_here)
    TextView tvCheckInTimeHere;

    private PublicLoadLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_check_in_detail);
        setContentView(mRootView);
        ButterKnife.bind(this, mRootView);
        mRootView.showLoadingView();
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRootView.showLoadingView();
                getCheckInDetail();
            }
        });
        tvTitle.setText(R.string.check_in_detail);
        getCheckInDetail();
    }

    private void getCheckInDetail() {
        CheckInDetailRequest checkInDetailRequest = new CheckInDetailRequest();
        checkInDetailRequest.startRequest(CheckInDetailResponse.class, new HttpCallback<CheckInDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, CheckInDetailResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret.getCode() == 0) {
                    mRootView.hiddenNetErrorView();
                    mRootView.hiddenOtherErrorView();

                    tvCheckInName.setText(ret.getData().getTrainingName());
                    tvCheckInTimePlan.setText(ret.getData().getCheckInTimePlan());
                    // TODO: 17-9-18 临时数据
                    if (mCheckInStatue.equals("0")) {
                        tvCheckIn.setVisibility(View.GONE);
                        tvCheckInHere.setVisibility(View.GONE);
                        tvCheckInTimeHere.setVisibility(View.VISIBLE);
                        tvCheckInTime.setVisibility(View.VISIBLE);
                        tvCheckInStatue.setText(R.string.you_have_check_in_success);
                        tvCheckInTime.setText(ret.getData().getCheckInTime());
                    } else {
                        tvCheckIn.setVisibility(View.VISIBLE);
                        tvCheckInHere.setVisibility(View.GONE);
                        tvCheckInTimeHere.setVisibility(View.GONE);
                        tvCheckInTime.setVisibility(View.GONE);
                        tvCheckInStatue.setText("您还未签到");
                    }


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


    public static void toThisAct(Context activity, String checkInStatue) {
        mCheckInStatue = checkInStatue;// TODO: 17-9-18 临时数据
        activity.startActivity(new Intent(activity, CheckInDetailActivity.class));
    }

    @OnClick({R.id.img_left, R.id.tv_check_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.tv_check_in:
                QRCodeCheckInActivity.toThisAct(this);
                break;
        }
    }
}
