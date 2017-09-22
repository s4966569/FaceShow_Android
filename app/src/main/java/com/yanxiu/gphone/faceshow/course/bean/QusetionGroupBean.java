package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;

/**
 * 投票封装类
 */
public class QusetionGroupBean extends BaseBean {
    private String id;
    private String title;
    private String description;
    private String questionNum;
    private String groupType;
    private String groupStatus;
    private String bizId;
    private String bizSource;
    private String createTime;
    private String stepId;
    private String answerNum;
    private String answerUserNum;
    private ArrayList<QusetionBean> questions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(String questionNum) {
        this.questionNum = questionNum;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
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

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(String answerNum) {
        this.answerNum = answerNum;
    }

    public String getAnswerUserNum() {
        return answerUserNum;
    }

    public void setAnswerUserNum(String answerUserNum) {
        this.answerUserNum = answerUserNum;
    }

    public ArrayList<QusetionBean> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QusetionBean> questions) {
        this.questions = questions;
    }
}
