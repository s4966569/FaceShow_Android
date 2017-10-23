package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yanxiu.gphone.faceshow.constant.Constants;
import com.yanxiu.gphone.faceshow.course.activity.CourseActivity;
import com.yanxiu.gphone.faceshow.course.activity.CourseDiscussActivity;
import com.yanxiu.gphone.faceshow.course.activity.EvaluationActivity;
import com.yanxiu.gphone.faceshow.course.activity.VoteActivity;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInByQRActivity;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInDetailActivity;

/**
 * 跳转到问卷(评论/投票/签到)详情页面的广播接收者
 * Created by Think on 2017/10/19.
 */

public class ToEvaluationActivityBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        Intent interactIntent = null;
        switch (intent.getIntExtra("type", -1)) {
            case 101://投票
                interactIntent = new Intent(context, VoteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
//            case InteractStepsBean.DISCUSS:
//                interactIntent = new Intent(context, CourseDiscussActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                break;
            case 120://问卷
                interactIntent = new Intent(context, EvaluationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case 100://签到
                interactIntent = new Intent(context, CheckInDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
        interactIntent.putExtra("stepid", String.valueOf(intent.getIntExtra("objectId", -1)));
        context.startActivity(interactIntent);

    }
}
