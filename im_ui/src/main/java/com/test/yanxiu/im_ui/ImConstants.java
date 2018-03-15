package com.test.yanxiu.im_ui;

/**
 * Created by 戴延枫 on 2018/3/14.
 */

public class ImConstants {

    /**
     * 消息类型
     */
    public static final int MESSAGE_TYPE_MYSELF = 1;      //消息类型：我自己的消息
    public static final int MESSAGE_TYPE_OTHER_PEOPLE = 2;//消息类型：其他人的消息
    public static final int MESSAGE_TYPE_TIME = 3;        //消息类型：时间（该类型时，msgTime务必不为空）
    public static final int MESSAGE_TYPE_LOADING = 4;        //消息类型：加载更多loading

    /**
     * 我发送的消息的状态
     */
    public enum MyselfMsgStatus {
        MYSELF_MESSAGE_STATUS_NORMAL,//我的消息的类型：正常状态
        MYSELF_MESSAGE_STATUS_SENDING,//我的消息的类型：发送中
        MYSELF_MESSAGE_STATUS_FAIL,//我的消息的类型：失败
    }
}
