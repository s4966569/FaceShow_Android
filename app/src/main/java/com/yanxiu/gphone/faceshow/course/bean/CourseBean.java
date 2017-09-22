package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程
 * Created by 戴延枫 on 2017/9/15.
 */

public class CourseBean extends BaseBean {
    private String date;//上一级（CoursesBean）转过来的。--为了转成符合我们自己规则的数据

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String id;
    private String subscriberId;
    private String subscriberType;
    private String courseName;
    private String lecturer;//讲师
    private String site;
    private String startTime;
    private String endTime;
    private String briefing;
    private String attachments;
    private String courseStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberType() {
        return subscriberType;
    }

    public void setSubscriberType(String subscriberType) {
        this.subscriberType = subscriberType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getBriefing() {
        return briefing;
    }

    public void setBriefing(String briefing) {
        this.briefing = briefing;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }


    /*
      *  课程详情里的字段 start
     */
    private ArrayList<LecturerInfosBean> lecturerInfos = new ArrayList<>();
    private ArrayList<AttachmentInfosBean> attachmentInfos = new ArrayList<>();

    public ArrayList<LecturerInfosBean> getLecturerInfos() {
        return lecturerInfos;
    }

    public void setLecturerInfos(ArrayList<LecturerInfosBean> lecturerInfos) {
        this.lecturerInfos = lecturerInfos;
    }

    public ArrayList<AttachmentInfosBean> getAttachmentInfos() {
        return attachmentInfos;
    }

    public void setAttachmentInfos(ArrayList<AttachmentInfosBean> attachmentInfos) {
        this.attachmentInfos = attachmentInfos;
    }
    /*
      *  课程详情里的字段 END
     */
}
