package com.yanxiu.gphone.faceshow.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.http.notificaion.GetNotificationDetailRequest;
import com.yanxiu.gphone.faceshow.http.notificaion.GetNotificationDetailResponse;

import java.util.UUID;

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
    private String mNotificationID;
    private UUID mGetNotificationDetailRequestUUID;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRootView = new PublicLoadLayout(this);
        mRootView.setContentView(R.layout.activity_notification_detail);
        mRootView.setRetryButtonOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotificationDetailRequest();
            }
        });
        setContentView(mRootView);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.notification_detail);
        mNotificationID = getIntent().getStringExtra(NOTIFICATION_ID);
        getNotificationDetailRequest();
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

    private void getNotificationDetailRequest() {
        mRootView.showLoadingView();
        GetNotificationDetailRequest getNotificationDetailRequest = new GetNotificationDetailRequest();
        getNotificationDetailRequest.id = mNotificationID;
        mGetNotificationDetailRequestUUID = getNotificationDetailRequest.startRequest(GetNotificationDetailResponse.class, new HttpCallback<GetNotificationDetailResponse>() {
            @Override
            public void onSuccess(RequestBase request, GetNotificationDetailResponse ret) {
                mRootView.hiddenLoadingView();
                if (ret.getStatus().getCode() == 0) {
                    tvNotificationTitle.setText(ret.getData().getNotificationTitle());
                    tvNotificationCreatedPersonAndName.setText(getString(R.string.notificationCreatedPersonAndTime, ret.getData().getNotificationCreator(), ret.getData().getNotificationCreateTime()));
                    tvNotificationContent.setText(ret.getData().getNotificationContent());
                    Glide.with(mContext).load(ret.getData().getNotificationPic()).asBitmap().into(imgNotification);
                    mRootView.hiddenNetErrorView();
                    mRootView.hiddenOtherErrorView();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGetNotificationDetailRequestUUID != null) {
            RequestBase.cancelRequestWithUUID(mGetNotificationDetailRequestUUID);
        }
    }
}