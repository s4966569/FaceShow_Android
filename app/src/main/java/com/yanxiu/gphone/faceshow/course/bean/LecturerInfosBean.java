package com.yanxiu.gphone.faceshow.course.bean;

import com.yanxiu.gphone.faceshow.base.BaseBean;

/**
 * 课程详情里的讲师信息
 * Created by 戴延枫 on 2017/9/21.
 */

public class LecturerInfosBean extends CourseDetailItemBean {
    private String lecturerName;
    private String lecturerBriefing;
    private String lecturerAvatar;//imgUrl

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getLecturerBriefing() {
        return lecturerBriefing;
    }

    public void setLecturerBriefing(String lecturerBriefing) {
        this.lecturerBriefing = lecturerBriefing;
    }

    public String getLecturerAvatar() {
        return lecturerAvatar;
    }

    public void setLecturerAvatar(String lecturerAvatar) {
        this.lecturerAvatar = lecturerAvatar;
    }
}
