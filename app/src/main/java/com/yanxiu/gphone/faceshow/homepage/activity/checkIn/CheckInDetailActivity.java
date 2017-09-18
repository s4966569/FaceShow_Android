package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_detail);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in_detail);
    }

    public static void toThisAct(Context activity) {
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
