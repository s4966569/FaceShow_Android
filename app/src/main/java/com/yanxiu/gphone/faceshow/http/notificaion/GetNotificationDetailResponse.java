package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-18.
 */

public class GetNotificationDetailResponse extends FaceShowBaseResponse {

    /**
     * data : {"notificationTitle":"报道、用餐时间及地点","notificationCreator":"孙长龙","notificationCreateTime":"2014.02.23","notificationContent":"报到、用餐时间及地点、校园WI-FI、第一节课上课时间及地点、班主任联系方式等。请各位同学参见下图","notificationPic":"https://ws4.sinaimg.cn/large/006tKfTcly1fj8lidbk4gj30c909jq34.jpg"}
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
         * notificationTitle : 报道、用餐时间及地点
         * notificationCreator : 孙长龙
         * notificationCreateTime : 2014.02.23
         * notificationContent : 报到、用餐时间及地点、校园WI-FI、第一节课上课时间及地点、班主任联系方式等。请各位同学参见下图
         * notificationPic : https://ws4.sinaimg.cn/large/006tKfTcly1fj8lidbk4gj30c909jq34.jpg
         */

        private String notificationTitle;
        private String notificationCreator;
        private String notificationCreateTime;
        private String notificationContent;
        private String notificationPic;

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public String getNotificationCreator() {
            return notificationCreator;
        }

        public void setNotificationCreator(String notificationCreator) {
            this.notificationCreator = notificationCreator;
        }

        public String getNotificationCreateTime() {
            return notificationCreateTime;
        }

        public void setNotificationCreateTime(String notificationCreateTime) {
            this.notificationCreateTime = notificationCreateTime;
        }

        public String getNotificationContent() {
            return notificationContent;
        }

        public void setNotificationContent(String notificationContent) {
            this.notificationContent = notificationContent;
        }

        public String getNotificationPic() {
            return notificationPic;
        }

        public void setNotificationPic(String notificationPic) {
            this.notificationPic = notificationPic;
        }
    }
}
