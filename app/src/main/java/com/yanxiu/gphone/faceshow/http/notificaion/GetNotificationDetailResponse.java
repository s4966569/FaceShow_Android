package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-18.
 */

public class GetNotificationDetailResponse extends FaceShowBaseResponse {


    /**
     * data : {"id":3,"title":"12314","content":"testceshi","authorId":1,"clazzId":1,"createTime":"2017-09-15 12:03:52","updateTime":"2017-09-15 12:03:52","state":1,"attachUrl":null,"readNum":8,"authorName":null,"createTimeStr":null,"updateTimeStr":null,"noticeNum":null,"noticeReadNumSum":null,"viewed":true}
     * error : null
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * id : 3
         * title : 12314
         * content : testceshi
         * authorId : 1
         * clazzId : 1
         * createTime : 2017-09-15 12:03:52
         * updateTime : 2017-09-15 12:03:52
         * state : 1
         * attachUrl : null
         * readNum : 8
         * authorName : null
         * createTimeStr : null
         * updateTimeStr : null
         * noticeNum : null
         * noticeReadNumSum : null
         * viewed : true
         */

        private int id;
        private String title;
        private String content;
        private int authorId;
        private int clazzId;
        private String createTime;
        private String updateTime;
        private int state;
        private Object attachUrl;
        private int readNum;
        private Object authorName;
        private Object createTimeStr;
        private Object updateTimeStr;
        private Object noticeNum;
        private Object noticeReadNumSum;
        private boolean viewed;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public int getClazzId() {
            return clazzId;
        }

        public void setClazzId(int clazzId) {
            this.clazzId = clazzId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Object getAttachUrl() {
            return attachUrl;
        }

        public void setAttachUrl(Object attachUrl) {
            this.attachUrl = attachUrl;
        }

        public int getReadNum() {
            return readNum;
        }

        public void setReadNum(int readNum) {
            this.readNum = readNum;
        }

        public Object getAuthorName() {
            return authorName;
        }

        public void setAuthorName(Object authorName) {
            this.authorName = authorName;
        }

        public Object getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(Object createTimeStr) {
            this.createTimeStr = createTimeStr;
        }

        public Object getUpdateTimeStr() {
            return updateTimeStr;
        }

        public void setUpdateTimeStr(Object updateTimeStr) {
            this.updateTimeStr = updateTimeStr;
        }

        public Object getNoticeNum() {
            return noticeNum;
        }

        public void setNoticeNum(Object noticeNum) {
            this.noticeNum = noticeNum;
        }

        public Object getNoticeReadNumSum() {
            return noticeReadNumSum;
        }

        public void setNoticeReadNumSum(Object noticeReadNumSum) {
            this.noticeReadNumSum = noticeReadNumSum;
        }

        public boolean isViewed() {
            return viewed;
        }

        public void setViewed(boolean viewed) {
            this.viewed = viewed;
        }
    }
}
