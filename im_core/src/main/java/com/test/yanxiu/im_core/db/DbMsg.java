package com.test.yanxiu.im_core.db;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by cailei on 06/03/2018.
 */

public class DbMsg extends DataSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    protected String reqId;        // 客户端生成的唯一id

    protected long msgId;          // 由于client发送可能失败，所以msgId不作为主键
    protected long topicId;       // 此msg所属的topic
    protected long senderId;     // 此msg的owner
    protected long sendTime;       // msg的发送时间

    protected int contentType;     // 目前为txt，未来支持pic
    protected String msg;          // txt怎插入msg，pic插入
    protected String thumbnail;
    protected String viewUrl;

    //region getter setter
    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(long topicId) {
        this.topicId = topicId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }
    //endregion
}
