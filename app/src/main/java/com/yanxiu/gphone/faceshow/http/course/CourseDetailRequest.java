package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程详情请求
 */

public class CourseDetailRequest extends FaceShowBaseRequest {
    public String method = "app.course.getCourse";
    public String courseId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "CourseDetail.json";
//    }
}
