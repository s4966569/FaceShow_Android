package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-18.
 */

public class CheckInDetailResponse extends FaceShowBaseResponse {

    /**
     * data : {"trainingName":"国培计划-高级教师研修班研修班研修班研修班研修班研修班研修班研修班","checkInTimePlan":"2017.8.27 8:00 - 2017.8.27 8:30","checkInStatue":"0","checkInTime":"2017.8.27 8:00"}
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
         * trainingName : 国培计划-高级教师研修班研修班研修班研修班研修班研修班研修班研修班
         * checkInTimePlan : 2017.8.27 8:00 - 2017.8.27 8:30
         * checkInStatue : 0
         * checkInTime : 2017.8.27 8:00
         */

        private String trainingName;
        private String checkInTimePlan;
        private String checkInStatue;
        private String checkInTime;

        public String getTrainingName() {
            return trainingName;
        }

        public void setTrainingName(String trainingName) {
            this.trainingName = trainingName;
        }

        public String getCheckInTimePlan() {
            return checkInTimePlan;
        }

        public void setCheckInTimePlan(String checkInTimePlan) {
            this.checkInTimePlan = checkInTimePlan;
        }

        public String getCheckInStatue() {
            return checkInStatue;
        }

        public void setCheckInStatue(String checkInStatue) {
            this.checkInStatue = checkInStatue;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }
    }
}
