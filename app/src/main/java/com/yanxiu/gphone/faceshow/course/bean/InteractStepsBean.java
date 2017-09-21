package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

/**
 * 课程详情里的投票等互动数据
 * Created by 戴延枫 on 2017/9/21.
 */

public class InteractStepsBean extends CourseDetailItemBean {

    public static final String VOTE = "3";//投票
    public static final String DISCUSS = "4";//讨论
    public static final String QUESTIONNAIRES = "5";//问卷
    public static final String CHECK_IN = "6";//签到

    public static final String FINISH = "1";//投票完成
    public static final String NO_FINISH = "2";//投票未完成

    private String stepId;
    private String projectId;
    private String clazsId;
    private String courseId;
    private String interactName;
    private String interactType;//3投票，4讨论，5问卷，6签到
    private String interactId;
    private String stepStatus;
    private String createTime;
    private String stepFinished;//1,完成  2,未完成
    private String interactTypeName;

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getClazsId() {
        return clazsId;
    }

    public void setClazsId(String clazsId) {
        this.clazsId = clazsId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getInteractName() {
        return interactName;
    }

    public void setInteractName(String interactName) {
        this.interactName = interactName;
    }

    public String getInteractType() {
        return interactType;
    }

    public void setInteractType(String interactType) {
        this.interactType = interactType;
    }

    public String getInteractId() {
        return interactId;
    }

    public void setInteractId(String interactId) {
        this.interactId = interactId;
    }

    public String getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(String stepStatus) {
        this.stepStatus = stepStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStepFinished() {
        return stepFinished;
    }

    public void setStepFinished(String stepFinished) {
        this.stepFinished = stepFinished;
    }

    public String getInteractTypeName() {
        return interactTypeName;
    }

    public void setInteractTypeName(String interactTypeName) {
        this.interactTypeName = interactTypeName;
    }
}
