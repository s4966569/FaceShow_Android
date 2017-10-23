package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.common.activity.WebViewActivity;
import com.yanxiu.gphone.faceshow.login.LoginActivity;

/**
 * 跳转到资源详情页面的广播接收者
 * Created by frc on 2017/10/19.
 */

public class ToResourceDetailActivityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        Intent resourceDetailIntent = new Intent(context, WebViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", intent.getStringExtra("url"));
        intent.putExtra("title", intent.getStringExtra("title"));
        context.startActivity(resourceDetailIntent);

    }
}
