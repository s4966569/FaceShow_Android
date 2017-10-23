package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseCallback;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInDetailRequest;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInDetailResponse;
import com.yanxiu.gphone.faceshow.http.checkin.GetCheckInNotesResponse;
import com.yanxiu.gphone.faceshow.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到详情页
 * created by frc
 */
public class CheckInDetailActivity extends FaceShowBaseActivity {

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

    private final static String CHECK_IN_DETAIL = "check_in_detail";
    private final static String STEP_ID = "stepId";
    private PublicLoadLayout publicLoadLayout;

    public static void toThisAct(Context activity, String stepId) {
        Intent intent = new Intent(activity, CheckInDetailActivity.class);
        intent.putExtra(STEP_ID, stepId);
        activity.startActivity(intent);
    }

    public static void toThisAct(Context activity, GetCheckInNotesResponse.CheckInNotesBean data) {
        Intent intent = new Intent(activity, CheckInDetailActivity.class);
        intent.putExtra(CHECK_IN_DETAIL, data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        publicLoadLayout = new PublicLoadLayout(this);
        publicLoadLayout.setContentView(R.layout.activity_check_in_detail);
        setContentView(publicLoadLayout);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in_detail);
    /*从签到列表跳转过来，直接携带了签到详情，从其他地方跳转过来需要携带stepId，然后再获取详情信息*/
        GetCheckInNotesResponse.CheckInNotesBean data = (GetCheckInNotesResponse.CheckInNotesBean) getIntent().getSerializableExtra(CHECK_IN_DETAIL);
        String stepId = getIntent().getStringExtra(STEP_ID);
        if (data != null) {
            showData(data);
        } else if (!TextUtils.isEmpty(stepId)) {
            getCheckInDetail(stepId);
        }
    }

    private void getCheckInDetail(String stepId) {
        GetCheckInDetailRequest getCheckInDetailRequest = new GetCheckInDetailRequest();
        getCheckInDetailRequest.stepId = stepId;
        publicLoadLayout.showLoadingView();
        getCheckInDetailRequest.startRequest(GetCheckInDetailResponse.class, new FaceShowBaseCallback<GetCheckInDetailResponse>() {
            @Override
            protected void onResponse(RequestBase request, GetCheckInDetailResponse response) {
                publicLoadLayout.hiddenLoadingView();
                if (response.getCode() == 0) {
                    if (response.getData() != null && response.getData().getSignIn() != null) {
                        showData(response.getData().getSignIn());
                    } else {
                        publicLoadLayout.showOtherErrorView("没有详情");
                    }
                } else {
                    publicLoadLayout.showOtherErrorView(response.getMessage());
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                publicLoadLayout.showLoadingView();
                publicLoadLayout.showNetErrorView();

            }
        });

    }


    private void showData(GetCheckInNotesResponse.CheckInNotesBean data) {
        tvCheckInName.setText(data.getTitle());
        tvCheckInTimePlan.setText(getString(R.string.check_in_plan, StringUtils.getCourseTime(data.getStartTime()), StringUtils.getCourseTime(data.getEndTime())));
        if (data.getUserSignIn() != null && data.getUserSignIn().getSigninStatus() == 1) {//签到chengg
            tvCheckIn.setVisibility(View.GONE);
            tvCheckInHere.setVisibility(View.GONE);
            tvCheckInTimeHere.setVisibility(View.VISIBLE);
            tvCheckInTime.setVisibility(View.VISIBLE);
            tvCheckInStatue.setText(R.string.you_have_check_in_success);
            tvCheckInTime.setText(data.getUserSignIn().getSigninTime());
        } else {
            tvCheckIn.setVisibility(View.VISIBLE);
            tvCheckInHere.setVisibility(View.GONE);
            tvCheckInTimeHere.setVisibility(View.GONE);
            tvCheckInTime.setVisibility(View.GONE);
            tvCheckInStatue.setText("您还未签到");
            if (data.getOpenStatus() == 7) {
                //签到已过期
                tvCheckIn.setVisibility(View.GONE);
            }
        }
    }


    @OnClick({R.id.img_left, R.id.tv_check_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.tv_check_in:
                CheckInByQRActivity.toThisAct(this);
                this.finish();
                break;
        }
    }
}
