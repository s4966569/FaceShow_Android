package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * 通知数据
 * Created by frc on 17-9-15.
 */

public class NotificationResponse extends FaceShowBaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private List<Notification> elements;
        private int pageSize;
        private int pageNum;
        private int offset;
        private int totalElements;
        private int lastPageNumber;

        public List<Notification> getElements() {
            return elements;
        }

        public void setElements(List<Notification> elements) {
            this.elements = elements;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getLastPageNumber() {
            return lastPageNumber;
        }

        public void setLastPageNumber(int lastPageNumber) {
            this.lastPageNumber = lastPageNumber;
        }
    }

    public class Notification {


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
