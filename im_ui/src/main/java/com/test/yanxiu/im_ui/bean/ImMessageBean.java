package com.test.yanxiu.im_ui.bean;


import com.test.yanxiu.im_ui.ImConstants;
import com.test.yanxiu.im_ui.adapter.ChartRoomAdapter;

import java.util.ArrayList;

/**
 * im聊天消息数据封装类
 * Created by 戴延枫 on 2018/3/13.
 */

public class ImMessageBean extends ImBaseBean {

    private ArrayList<MessageBean> msg;

    public ArrayList<MessageBean> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<MessageBean> msg) {
        this.msg = msg;
    }

    public static class MessageBean extends ImBaseBean {

        /**
         * 消息类型
         * 参见：{@link com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_MYSELF}
         */
        private int type;//消息类型

        private String userName;//消息发送者姓名
        private String userAvatarUrl;//消息发送者头像

        private String msgId;//消息id
        private String msgContent;//消息内容
        private String msgTime;//消息时间
        /**
         * 我自己的消息的状态
         * 参见：{@link com.test.yanxiu.im_ui.ImConstants.MyselfMsgStatus}
         */
        private ImConstants.MyselfMsgStatus myselfMsgStatus = ImConstants.MyselfMsgStatus.MYSELF_MESSAGE_STATUS_NORMAL;//默认值

        public ImConstants.MyselfMsgStatus getMyselfMsgStatus() {
            return myselfMsgStatus;
        }

        public void setMyselfMsgStatus(ImConstants.MyselfMsgStatus myselfMsgStatus) {
            this.myselfMsgStatus = myselfMsgStatus;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserAvatarUrl() {
            return userAvatarUrl;
        }

        public void setUserAvatarUrl(String userAvatarUrl) {
            this.userAvatarUrl = userAvatarUrl;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }

        public String getMsgTime() {
            return msgTime;
        }

        public void setMsgTime(String msgTime) {
            this.msgTime = msgTime;
        }
    }


}
