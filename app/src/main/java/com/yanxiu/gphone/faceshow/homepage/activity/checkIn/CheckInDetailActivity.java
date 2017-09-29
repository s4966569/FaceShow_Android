package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
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
    private final static String POSITION = "position";
    private int mPositionInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_detail);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in_detail);
        GetCheckInNotesResponse.CheckInNotesBean data = (GetCheckInNotesResponse.CheckInNotesBean) getIntent().getSerializableExtra(CHECK_IN_DETAIL);
        mPositionInList =  getIntent().getIntExtra(POSITION, -1);
        showData(data);
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
            if (data.getOpenStatus()==7){
                //签到已过期
                tvCheckIn.setVisibility(View.GONE);
            }
        }
    }


    public static void toThisAct(Context activity, GetCheckInNotesResponse.CheckInNotesBean data, int position) {
        Intent intent = new Intent(activity, CheckInDetailActivity.class);
        intent.putExtra(CHECK_IN_DETAIL, data);
        intent.putExtra(POSITION, position);
        activity.startActivity(intent);
    }

    @OnClick({R.id.img_left, R.id.tv_check_in})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.tv_check_in:
                CheckInByQRActivity.toThisAct(this, mPositionInList);
                this.finish();
                break;
        }
    }
}
