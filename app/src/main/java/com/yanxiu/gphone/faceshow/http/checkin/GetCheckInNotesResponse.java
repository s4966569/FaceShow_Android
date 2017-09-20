package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by frc on 17-9-18.
 */

public class GetCheckInNotesResponse extends FaceShowBaseResponse {


    /**
     * error : null
     * data : [{"projectName":"国培计划（２０１７）－专职培训团队研修","className":"19号一班","checkInNotes":[{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]},{"projectName":"国培计划（２０１７）－专职培训团队研修","className":"19号二班","checkInNotes":[{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]},{"projectName":"国培计划（２０１７）－专职培训团队研修","className":"19号三班","checkInNotes":[{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]},{"projectName":"国培计划（２０１７）－专职培训团队研修","className":"19号四班","checkInNotes":[{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]},{"projectName":"国培计划（２０１７）－专职培训团队研修","className":"19号五班","checkInNotes":[{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]}]
     */

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
         * className : 19号一班
         * checkInNotes : [{"id":8,"title":"2017-09-15上午签到","startTime":"2017-09-15 08:00:00","endTime":"2017-09-15 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":4,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":null},{"id":10,"title":"2017-09-16上午签到","startTime":"2017-09-16 00:00:00","endTime":"2017-09-16 10:00:00","antiCheat":1,"successPrompt":"签到成功，感谢您的参与！","openStatus":7,"bizId":6,"bizSource":"clazs","createTime":"2017-09-15 11:50:32","stepId":6,"totalUserNum":0,"signInUserNum":0,"opentStatusName":"已结束","percent":0,"userSignIn":{"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}}]
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

    public class UserSignIn implements Serializable {

        /**
         * id : 1
         * userId : 10145096
         * signinId : 10
         * signinStatus : 1
         * signinTime : 2017-09-16 00:09:00
         * signinRemark : null
         * signinDevice : pc
         * userName : null
         * avatar : null
         */

        private int id;
        private int userId;
        private int signinId;
        private int signinStatus;
        private String signinTime;
        private Object signinRemark;
        private String signinDevice;
        private Object userName;
        private Object avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getSigninId() {
            return signinId;
        }

        public void setSigninId(int signinId) {
            this.signinId = signinId;
        }

        public int getSigninStatus() {
            return signinStatus;
        }

        public void setSigninStatus(int signinStatus) {
            this.signinStatus = signinStatus;
        }

        public String getSigninTime() {
            return signinTime;
        }

        public void setSigninTime(String signinTime) {
            this.signinTime = signinTime;
        }

        public Object getSigninRemark() {
            return signinRemark;
        }

        public void setSigninRemark(Object signinRemark) {
            this.signinRemark = signinRemark;
        }

        public String getSigninDevice() {
            return signinDevice;
        }

        public void setSigninDevice(String signinDevice) {
            this.signinDevice = signinDevice;
        }

        public Object getUserName() {
            return userName;
        }

        public void setUserName(Object userName) {
            this.userName = userName;
        }

        public Object getAvatar() {
            return avatar;
        }

        public void setAvatar(Object avatar) {
            this.avatar = avatar;
        }
    }

    public static class CheckInNotesBean implements Serializable {
        /**
         * id : 8
         * title : 2017-09-15上午签到
         * startTime : 2017-09-15 08:00:00
         * endTime : 2017-09-15 10:00:00
         * antiCheat : 1
         * successPrompt : 签到成功，感谢您的参与！
         * openStatus : 7
         * bizId : 6
         * bizSource : clazs
         * createTime : 2017-09-15 11:50:32
         * stepId : 4
         * totalUserNum : 0
         * signInUserNum : 0
         * opentStatusName : 已结束
         * percent : 0
         * userSignIn : null
         */

        private int id;
        private String title;
        private String startTime;
        private String endTime;
        private int antiCheat;
        private String successPrompt;
        private int openStatus;
        private int bizId;
        private String bizSource;
        private String createTime;
        private int stepId;
        private int totalUserNum;
        private int signInUserNum;
        private String opentStatusName;
        private int percent;
        private UserSignIn userSignIn;

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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getAntiCheat() {
            return antiCheat;
        }

        public void setAntiCheat(int antiCheat) {
            this.antiCheat = antiCheat;
        }

        public String getSuccessPrompt() {
            return successPrompt;
        }

        public void setSuccessPrompt(String successPrompt) {
            this.successPrompt = successPrompt;
        }

        public int getOpenStatus() {
            return openStatus;
        }

        public void setOpenStatus(int openStatus) {
            this.openStatus = openStatus;
        }

        public int getBizId() {
            return bizId;
        }

        public void setBizId(int bizId) {
            this.bizId = bizId;
        }

        public String getBizSource() {
            return bizSource;
        }

        public void setBizSource(String bizSource) {
            this.bizSource = bizSource;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getStepId() {
            return stepId;
        }

        public void setStepId(int stepId) {
            this.stepId = stepId;
        }

        public int getTotalUserNum() {
            return totalUserNum;
        }

        public void setTotalUserNum(int totalUserNum) {
            this.totalUserNum = totalUserNum;
        }

        public int getSignInUserNum() {
            return signInUserNum;
        }

        public void setSignInUserNum(int signInUserNum) {
            this.signInUserNum = signInUserNum;
        }

        public String getOpentStatusName() {
            return opentStatusName;
        }

        public void setOpentStatusName(String opentStatusName) {
            this.opentStatusName = opentStatusName;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        public UserSignIn getUserSignIn() {
            return userSignIn;
        }

        public void setUserSignIn(UserSignIn userSignIn) {
            this.userSignIn = userSignIn;
        }
    }
}
