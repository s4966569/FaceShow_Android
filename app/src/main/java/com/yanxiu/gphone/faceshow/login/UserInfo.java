package com.yanxiu.gphone.faceshow.login;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 17:26.
 * Function :
 */
public class UserInfo {

    private static UserInfo instance;

    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }


    private Info info;

    public Info getInfo() {
        return new Info();
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info {
        private boolean isLogined;
        private String userId="123";
        private String userName="asd";
        private String headImg="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505979814335&di=61b7087885542aeb9a56e4c02c178085&imgtype=0&src=http%3A%2F%2Flady.southcn.com%2F6%2Fimages%2Fattachement%2Fjpg%2Fsite4%2F20130105%2F90fba609e4271251cfce4a.jpg";
        private String phone="123456789";
        private String sex="0";
        private String stageId="12";
        private String stageName="ss";
        private String classId="22";
        private String className="ss";
        private String subjectId="22";
        private String subjectName="ss";
        private String token="sss";
        private String accountNumber="124341";
        private String accountPassword="121441";

        public boolean isLogined() {
            return isLogined;
        }

        public void setLogined(boolean logined) {
            isLogined = logined;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getStageId() {
            return stageId;
        }

        public void setStageId(String stageId) {
            this.stageId = stageId;
        }

        public String getStageName() {
            return stageName;
        }

        public void setStageName(String stageName) {
            this.stageName = stageName;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountPassword() {
            return accountPassword;
        }

        public void setAccountPassword(String accountPassword) {
            this.accountPassword = accountPassword;
        }
    }


}
