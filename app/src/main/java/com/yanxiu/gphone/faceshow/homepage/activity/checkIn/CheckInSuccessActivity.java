package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到成功页面
 * created by frc
 */
public class CheckInSuccessActivity extends FaceShowBaseActivity {
    private static final String CHECK_IN_STATUE = "check_in_statue";
    private static final String SIGN_IN_TIME = "sign_in_time";
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv_check_in_success_time)
    TextView tvCheckInSuccessTime;
    @BindView(R.id.sure)
    TextView sure;
    @BindView(R.id.tv_check_in_statue)
    TextView mTvCheckInStatue;

    public static void toThiAct(Activity activity, int checkInStatue, String signInTime) {
        Intent intent = new Intent(activity, CheckInSuccessActivity.class);
        intent.putExtra(CHECK_IN_STATUE, checkInStatue);
        intent.putExtra(SIGN_IN_TIME, signInTime);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_success);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
        int checkInStatue = getIntent().getIntExtra(CHECK_IN_STATUE, 0);
        if (checkInStatue == 210414) {
            mTvCheckInStatue.setText("用户已签到");
        } else {
            mTvCheckInStatue.setText("签到成功");
        }
        tvCheckInSuccessTime.setText(getIntent().getStringExtra(SIGN_IN_TIME));

        // TODO: 17-9-18 签到成功时间怎么显示 ？
    }

    @OnClick({R.id.img_left, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.sure:
                this.finish();
                break;
        }
    }


}
