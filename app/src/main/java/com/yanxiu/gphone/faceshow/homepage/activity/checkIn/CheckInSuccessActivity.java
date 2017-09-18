package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.util.SystemUtil;
import com.yanxiu.gphone.faceshow.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 签到成功页面
 * created by frc
 */
public class CheckInSuccessActivity extends FaceShowBaseActivity {

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

    public static void toThiAct(Activity activity) {
        activity.startActivity(new Intent(activity, CheckInDetailActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_success);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
        // TODO: 17-9-18 签到成功时间怎么显示 ？
        tvCheckInSuccessTime.setText( Utils.timeStamp2Date(String.valueOf(System.currentTimeMillis()),null));
    }

    @OnClick({R.id.img_left, R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
                this.finish();
                break;
            case R.id.sure:
                CheckInSuccessActivity.toThiAct(CheckInSuccessActivity.this);
                break;
        }
    }


}
