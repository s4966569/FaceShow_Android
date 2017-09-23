package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 讨论
 * Created by 戴延枫 on 2017/9/20.
 */

public class DiscussBean extends BaseBean {


    private String id;
    private String userId;//
    private String anonymous;//
    private String content;//
    private int replyNum;//
    private int likeNum;//点攒数量
    private String replyCommentRecordId;//
    private String commentId;//
    private String createTime;//
    private String userName;//
    private String avatar;//
    private String replays;//

    //自己的数据 start
    private String title;//header标题
    private boolean isHeader = false;
    private boolean hasLaud = false;//已经点赞 -- 用来记录已经点攒
    private int totalElements;
    //自己的数据 end

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getReplyCommentRecordId() {
        return replyCommentRecordId;
    }

    public void setReplyCommentRecordId(String replyCommentRecordId) {
        this.replyCommentRecordId = replyCommentRecordId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReplays() {
        return replays;
    }

    public void setReplays(String replays) {
        this.replays = replays;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHasLaud() {
        return hasLaud;
    }

    public void setHasLaud(boolean hasLaud) {
        this.hasLaud = hasLaud;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

}
