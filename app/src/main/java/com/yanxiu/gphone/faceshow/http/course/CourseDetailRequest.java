package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程详情请求
 */

public class CourseDetailRequest extends FaceShowMockRequest {
    public String method = "app.course.getCourse";
    public String courseId;

    @Override
    protected String urlPath() {
        return "/courseDetail";
    }

    @Override
    protected String getMockDataPath() {
        return "CourseDetail.json";
    }
}
