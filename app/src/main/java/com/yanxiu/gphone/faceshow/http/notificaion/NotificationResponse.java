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
        @SerializedName("id")
        private String notificationId;
        @SerializedName("name")
        private String notificationName;
        @SerializedName("time")
        private String notificationCreatedTime;

        public String getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(String notificationName) {
            this.notificationName = notificationName;
        }

        public String getNotificationCreatedTime() {
            return notificationCreatedTime;
        }

        public void setNotificationCreatedTime(String notificationCreatedTime) {
            this.notificationCreatedTime = notificationCreatedTime;
        }

        public String getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(String notificationId) {
            this.notificationId = notificationId;
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
