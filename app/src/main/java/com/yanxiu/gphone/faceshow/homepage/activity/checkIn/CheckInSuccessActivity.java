package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.homepage.CheckInSuccessEvent;
import com.yanxiu.gphone.faceshow.http.checkin.CheckInResponse;
import com.yanxiu.gphone.faceshow.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 签到成功页面
 * created by frc
 */
public class CheckInSuccessActivity extends FaceShowBaseActivity {
    private static final String DATA = "data";
    private final static String POSITION = "position";
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

    private CheckInResponse mUserSignInResponse;

    public static void toThiAct(Activity activity, CheckInResponse userSignInResponse) {
        Intent intent = new Intent(activity, CheckInSuccessActivity.class);
        intent.putExtra(DATA, userSignInResponse);
        activity.startActivity(intent);
    }

    public static void toThiAct(Activity activity, CheckInResponse userSignInResponse, int position) {
        Intent intent = new Intent(activity, CheckInSuccessActivity.class);
        intent.putExtra(DATA, userSignInResponse);
        intent.putExtra(POSITION, position);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_success);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.check_in);
        mUserSignInResponse = (CheckInResponse) getIntent().getSerializableExtra(DATA);

        if (mUserSignInResponse.getCode() == 0) {
            mTvCheckInStatue.setText(mUserSignInResponse.getData().getSuccessPrompt());
            tvCheckInSuccessTime.setText(mUserSignInResponse.getData().getSigninTime() != null ? mUserSignInResponse.getData().getSigninTime() : "");
            String string = RequestBase.getGson().toJson(mUserSignInResponse.getData());
            EventBus.getDefault().post(new CheckInSuccessEvent(string));//通知签到记录页面该记录签到成功 CheckInSuccessActivity
        } else {
            img.setImageResource(R.drawable.ic_check_in_error);
            mTvCheckInStatue.setText(mUserSignInResponse.getError().getMessage());
            tvCheckInSuccessTime.setText(mUserSignInResponse.getError().getData().getUserSignIn().getSigninTime());
        }

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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK);
        super.onBackPressed();

    }
}
