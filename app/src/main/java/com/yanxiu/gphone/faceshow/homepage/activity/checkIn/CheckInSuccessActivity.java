package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_success);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
    }

    @OnClick(R.id.img_left)
    public void onViewClicked() {
        this.finish();
    }
}
