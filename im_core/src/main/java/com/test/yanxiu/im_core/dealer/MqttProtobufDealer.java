package com.test.yanxiu.im_core.dealer;

import android.os.Looper;
import android.util.Log;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.test.yanxiu.im_core.http.common.ImMsg;
import com.test.yanxiu.im_core.protobuf.ImMqttProto;
import com.test.yanxiu.im_core.protobuf.MqttMsgProto;
import com.test.yanxiu.im_core.protobuf.TopicGetProto;
import com.test.yanxiu.im_core.protobuf.TopicMsgProto;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cailei on 05/03/2018.
 */

public class MqttProtobufDealer {
    public static class NewMsgEvent {
        public ImMsg msg;
    }

    public static class TopicUpdateEvent {
        public long topicId;
    }

    public static void dealWithData(byte[] rawData) throws InvalidProtocolBufferException {
        MqttMsgProto.MqttMsg mqttMsg = MqttMsgProto.MqttMsg.parseFrom(rawData);

        //目前只有一种type，且type为""
        //if (mqttMsg.getType() == "xxx") {
            // 是im的消息
            ImMqttProto.ImMqtt imMqtt = ImMqttProto.ImMqtt.parseFrom(mqttMsg.getData());
            if ((imMqtt.getImEvent() == 101)            // 请求主题数据（client通过topicId向server请求主题全部数据）
                    || (imMqtt.getImEvent() == 111)     // 主题添加新成员，同101事件（client通过topicId向server请求主题全部数据）
                    || (imMqtt.getImEvent() == 112))    // 主题删除成员，同101事件（client通过topicId向server请求主题全部数据）
            {
                for (ByteString item : imMqtt.getBodyList()) {
                    TopicGetProto.TopicGet topicProto = TopicGetProto.TopicGet.parseFrom(item);
                    long topicId = topicProto.getTopicId();
                    // EventBus发现topic更新
                }
            }

            if (imMqtt.getImEvent() == 121)         // 下发主题聊天消息
            {
                for (ByteString item : imMqtt.getBodyList()) {
                    TopicMsgProto.TopicMsg msgProto = TopicMsgProto.TopicMsg.parseFrom(item);
                    ImMsg msg = new ImMsg();
                    msg.msgId = msgProto.getId();
                    msg.topicId = msgProto.getTopicId();
                    msg.senderId = msgProto.getSenderId();
                    msg.contentType = msgProto.getContentType();
                    msg.sendTime = msgProto.getSendTime();
                    msg.contentData = new ImMsg.ContentData();
                    msg.contentData.msg = msgProto.getContentData().getMsg();
                    // EventBus发现topic更新

                    onNewMsg(msg);
                }
            }
        //}
    }

    public static void updateDbWithNewMsg(ImMsg msg) {

    }

    public static void onNewMsg(ImMsg msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
            Log.d("Tag", "main thread");
        } else { // 非UI主线程
            Log.d("Tag", "other thread");
        }

        NewMsgEvent event = new NewMsgEvent();
        event.msg = msg;
        EventBus.getDefault().post(event);
    }
}
