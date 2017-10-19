package com.yanxiu.gphone.faceshow.getui;

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
        Intent toCourseDetailIntent = new Intent(context, CourseActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toCourseDetailIntent.putExtra(CourseActivity.COURSE_ID,intent.getStringExtra(CourseActivity.COURSE_ID));
        context.startActivity(toCourseDetailIntent);

    }
}
