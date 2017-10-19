package com.yanxiu.gphone.faceshow.getui;

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
        Intent resourceDetailIntent = new Intent(context, WebViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", intent.getStringExtra("url"));
        intent.putExtra("title", intent.getStringExtra("title"));
        context.startActivity(resourceDetailIntent);

    }
}
