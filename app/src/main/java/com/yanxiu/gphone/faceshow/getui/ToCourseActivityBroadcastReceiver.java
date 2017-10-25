package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.course.activity.CourseActivity;
import com.yanxiu.gphone.faceshow.login.LoginActivity;

/**
 * 跳转到课程详情页面的广播接收者
 * Created by Think on 2017/10/19.
 */

public class ToCourseActivityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        Intent toCourseDetailIntent = new Intent(context, CourseActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toCourseDetailIntent.putExtra(CourseActivity.COURSE_ID, intent.getStringExtra("objectId"));
        context.startActivity(toCourseDetailIntent);

    }
}
