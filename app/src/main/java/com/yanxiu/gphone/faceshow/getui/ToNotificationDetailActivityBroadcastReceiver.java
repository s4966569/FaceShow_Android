package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
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
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        Intent notificationDetailIntent = new Intent(context, NotificationDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationDetailIntent.putExtra(NotificationDetailActivity.NOTIFICATION_ID, String.valueOf(intent.getIntExtra("objectId", -1)));
        context.startActivity(notificationDetailIntent);

    }
}
