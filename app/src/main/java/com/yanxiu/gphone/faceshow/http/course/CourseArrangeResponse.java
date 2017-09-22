package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.course.bean.CourseArrangeBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 课程安排
 */

public class CourseArrangeResponse extends FaceShowBaseResponse {

    private CourseArrangeBean data = new CourseArrangeBean();

    public CourseArrangeBean getData() {
        return data;
    }

    public void setData(CourseArrangeBean data) {
        this.data = data;
    }
}
