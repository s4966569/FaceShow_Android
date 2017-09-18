package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by frc on 17-9-18.
 */

public class GetCheckInNotesResponse extends FaceShowBaseResponse {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * projectName : 国培计划（２０１７）－专职培训团队研修
         * class : 19号一班
         * checkInNotes : [{"TrainingName":"针对整体项目的培训","checkInTime":"2017.08.19 13:24","checkInStatue":"0"},{"TrainingName":"针对整体项目的培训","checkInTime":"2017.08.19 13:24","checkInStatue":"１"}]
         */

        private String projectName;
        private String className;
        private List<CheckInNotesBean> checkInNotes;

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public List<CheckInNotesBean> getCheckInNotes() {
            return checkInNotes;
        }

        public void setCheckInNotes(List<CheckInNotesBean> checkInNotes) {
            this.checkInNotes = checkInNotes;
        }


    }

    public static class CheckInNotesBean {
        /**
         * TrainingName : 针对整体项目的培训
         * checkInTime : 2017.08.19 13:24
         * checkInStatue : 0
         */

        private String TrainingName;
        private String checkInTime;
        private String checkInStatue;

        public String getTrainingName() {
            return TrainingName;
        }

        public void setTrainingName(String TrainingName) {
            this.TrainingName = TrainingName;
        }

        public String getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(String checkInTime) {
            this.checkInTime = checkInTime;
        }

        public String getCheckInStatue() {
            return checkInStatue;
        }

        public void setCheckInStatue(String checkInStatue) {
            this.checkInStatue = checkInStatue;
        }
    }
}
