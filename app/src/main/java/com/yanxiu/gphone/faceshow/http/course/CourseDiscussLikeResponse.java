package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean;
import com.yanxiu.gphone.faceshow.course.bean.CourseDiscussLikeBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.ArrayList;

/**
 * 点攒返回
 */

public class CourseDiscussLikeResponse extends FaceShowBaseResponse {
    private CourseDiscussLikeBean data = new CourseDiscussLikeBean();

    public CourseDiscussLikeBean getData() {
        return data;
    }

    public void setData(CourseDiscussLikeBean data) {
        this.data = data;
    }
}
