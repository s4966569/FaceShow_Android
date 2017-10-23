package com.yanxiu.gphone.faceshow.getui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.R;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 * Created by frc on 2017/10/18.
 */

public class FaceShowGeTuiIntentService extends GTIntentService {
    @Override
    public void onReceiveServicePid(Context context, int i) {

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage gtTransmitMessage) {
        String msgStr = new String(gtTransmitMessage.getPayload());
        GTPayloadBean bean = RequestBase.getGson().fromJson(msgStr, GTPayloadBean.class);
        Intent intent = null;
        switch (bean.getType()) {
            case 100://签到任务
            case 101://投票任务
            case 102://问卷任务
                intent = new Intent(context, ToEvaluationActivityBroadcastReceiver.class);
                intent.putExtra("type", bean.getType());
                break;
            case 120://通知
                intent = new Intent(context, ToNotificationDetailActivityBroadcastReceiver.class);
                break;
            case 130://班级即将开班
                break;
            case 131://班主任发布新资源
                intent = new Intent(context, ToResourceDetailActivityBroadcastReceiver.class);
                break;
            case 140://课程详情
                intent = new Intent(context, ToCourseActivityBroadcastReceiver.class);
                break;
        }
        Log.e(TAG, msgStr);
        int id = (int) (System.currentTimeMillis() / 1000);
        intent.putExtra("objectId", bean.getObjectId());
        intent.putExtra("notificationId", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.push_small);
        builder.setContentTitle(bean.getTitle());
        builder.setContentText(bean.getContent());
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {

    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
    }
}
