package com.yanxiu.gphone.faceshow.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationDetailActivity extends FaceShowBaseActivity {

    public final static String NOTIFICATION_ID = "notification_id";
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_notification_title)
    TextView tvNotificationTitle;
    @BindView(R.id.tv_notification_created_person_and_name)
    TextView tvNotificationCreatedPersonAndName;
    @BindView(R.id.tv_notification_content)
    TextView tvNotificationContent;
    @BindView(R.id.img_notification)
    ImageView imgNotification;
    private PublicLoadLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_notification_detail);
        setContentView(mRootView);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.notification_detail);
    }

    public static void toThisAct(Activity activity, String notificationId) {
        Intent intent = new Intent(activity, NotificationDetailActivity.class);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        activity.startActivity(intent);
    }

    @OnClick(R.id.img_left)
    public void onViewClicked() {
        this.finish();
    }
}
