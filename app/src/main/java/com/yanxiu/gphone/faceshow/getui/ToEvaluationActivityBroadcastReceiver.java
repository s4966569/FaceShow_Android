package com.yanxiu.gphone.faceshow.getui;

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

        Intent interactIntent = null;
        switch (intent.getStringExtra("type")) {
            case InteractStepsBean.VOTE:
                interactIntent = new Intent(context, VoteActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case InteractStepsBean.DISCUSS:
                interactIntent = new Intent(context, CourseDiscussActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case InteractStepsBean.QUESTIONNAIRES:
                interactIntent = new Intent(context, EvaluationActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
            case InteractStepsBean.CHECK_IN:
                interactIntent = new Intent(context, CheckInDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                break;
        }
        interactIntent.putExtra("stepid", intent.getStringExtra(EvaluationActivity.STEP_ID));
        context.startActivity(interactIntent);

    }
}
