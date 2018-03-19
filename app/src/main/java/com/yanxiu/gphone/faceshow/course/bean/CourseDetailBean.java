package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

import java.util.ArrayList;


/**
 * 课程详情
 * Created by 戴延枫 on 2017/9/18.
 */

public class CourseDetailBean extends BaseBean {
    private CourseBean course = new CourseBean();
    private ArrayList<InteractStepsBean> interactSteps = new ArrayList<>();

    public CourseBean getCourse() {
        return course;
    }



    public void setCourse(CourseBean course) {
        this.course = course;
    }

    public ArrayList<InteractStepsBean> getInteractSteps() {
        return interactSteps;
    }

    public void setInteractSteps(ArrayList<InteractStepsBean> interactSteps) {
        this.interactSteps = interactSteps;
    }
//    private ArrayList<LecturerInfosBean> lecturerInfos = new ArrayList<>();
//    private ArrayList<AttachmentInfosBean> attachmentInfos = new ArrayList<>();
//
//    public ArrayList<LecturerInfosBean> getLecturerInfos() {
//        return lecturerInfos;
//    }
//
//    public void setLecturerInfos(ArrayList<LecturerInfosBean> lecturerInfos) {
//        this.lecturerInfos = lecturerInfos;
//    }
//
//    public ArrayList<AttachmentInfosBean> getAttachmentInfos() {
//        return attachmentInfos;
//    }
//
//    public void setAttachmentInfos(ArrayList<AttachmentInfosBean> attachmentInfos) {
//        this.attachmentInfos = attachmentInfos;
//    }

}
