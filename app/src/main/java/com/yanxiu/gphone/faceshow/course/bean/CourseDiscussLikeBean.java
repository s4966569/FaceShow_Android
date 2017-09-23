package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 点攒返回
 * Created by 戴延枫 on 2017/9/18.
 */

public class CourseDiscussLikeBean extends BaseBean {
    private String id;
    private String userNum;
    private String totalScore;
    private String bizId;
    private String bizSource;
    private String createTime;
    private String avgScores;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
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

    public String getAvgScores() {
        return avgScores;
    }

    public void setAvgScores(String avgScores) {
        this.avgScores = avgScores;
    }
}
