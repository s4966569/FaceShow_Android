package com.yanxiu.gphone.faceshow.qrsignup;

import java.io.Serializable;

/**
 * Created by srt on 2018/3/6.
 */

public class SysUserBean implements Serializable{

    /**
     * id : 667
     * userId : 23249336
     * realName : 李涛
     * mobilePhone : 18600988448
     * email : null
     * stage : 0
     * subject : 0
     * userStatus : 1
     * ucnterId : null
     * sex : 1
     * school : 技术产品中心
     * avatar : http://orz.yanxiu.com/easygo/file/2018/1/27/1517034207965l89434_110-110.jpg
     * stageName : null
     * imTokenInfo : null
     * subjectName : null
     * sexName : null
     */

    private int id;
    private int userId;
    private String realName;
    private String mobilePhone;
    private String email;
    private int stage;
    private int subject;
    private int userStatus;
    private String ucnterId;
    private int sex;
    private String school;
    private String avatar;
    private String stageName;
    private String imTokenInfo;
    private String subjectName;
    private String sexName;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public String getUcnterId() {
        return ucnterId;
    }

    public void setUcnterId(String ucnterId) {
        this.ucnterId = ucnterId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getImTokenInfo() {
        return imTokenInfo;
    }

    public void setImTokenInfo(String imTokenInfo) {
        this.imTokenInfo = imTokenInfo;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSexName() {
        return sexName;
    }

    public void setSexName(String sexName) {
        this.sexName = sexName;
    }
}
