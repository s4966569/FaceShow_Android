package com.yanxiu.gphone.faceshow.http.checkin;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 签到
 * Created by frc on 17-9-19.
 */

public class CheckInResponse extends FaceShowBaseResponse {


    /**
     * data : {"id":1,"userId":10145096,"signinId":10,"signinStatus":1,"signinTime":"2017-09-16 00:09:00","signinRemark":null,"signinDevice":"pc","userName":null,"avatar":null}
     * currentUser :
     * error : null
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean  {
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
}
