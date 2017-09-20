package com.yanxiu.gphone.faceshow.http.notificaion;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * 通知详情
 * Created by frc on 17-9-15.
 */

public class NotificationResponse extends FaceShowBaseResponse {

    @SerializedName("data")
    private List<Notification> notificationList;

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public class Notification {


        /**
         * id : 2
         * title : 12314
         * content : testceshi
         * authorId : 1
         * clazzId : 1
         * createTime : 2017-09-15 12:03:42
         * updateTime : 2017-09-15 12:03:42
         * state : 1
         * attachUrl : null
         * readNum : 3
         * authorName : 111
         * createTimeStr : 2017-09-15
         * updateTimeStr : 2017-09-15
         * viewed : false
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
        private String authorName;
        private String createTimeStr;
        private String updateTimeStr;
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

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public String getCreateTimeStr() {
            return createTimeStr;
        }

        public void setCreateTimeStr(String createTimeStr) {
            this.createTimeStr = createTimeStr;
        }

        public String getUpdateTimeStr() {
            return updateTimeStr;
        }

        public void setUpdateTimeStr(String updateTimeStr) {
            this.updateTimeStr = updateTimeStr;
        }

        public boolean isViewed() {
            return viewed;
        }

        public void setViewed(boolean viewed) {
            this.viewed = viewed;
        }
    }


    public class NotificationDetail {
        private String notificationTitle;
        private String notificationCreatedPerson;
        private String notificationDetail;
        private String notificationPic;

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public String getNotificationCreaedPerson() {
            return notificationCreatedPerson;
        }

        public void setNotificationCreaedPerson(String notificationCreatedPerson) {
            this.notificationCreatedPerson = notificationCreatedPerson;
        }

        public String getNotificationDetail() {
            return notificationDetail;
        }

        public void setNotificationDetail(String notificationDetail) {
            this.notificationDetail = notificationDetail;
        }

        public String getNotificationPic() {
            return notificationPic;
        }

        public void setNotificationPic(String notificationPic) {
            this.notificationPic = notificationPic;
        }
    }
}
