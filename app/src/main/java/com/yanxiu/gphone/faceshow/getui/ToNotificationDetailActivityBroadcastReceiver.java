package com.yanxiu.gphone.faceshow.getui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.login.LoginActivity;
import com.yanxiu.gphone.faceshow.notification.activity.NotificationDetailActivity;

/**
 * 跳转到通知详情页面的广播接收者
 * Created by frc on 2017/10/19.
 */

public class ToNotificationDetailActivityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationDetailIntent = new Intent(context, NotificationDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationDetailIntent.putExtra(NotificationDetailActivity.NOTIFICATION_ID, intent.getStringExtra(NotificationDetailActivity.NOTIFICATION_ID));
        context.startActivity(notificationDetailIntent);

    }
}
