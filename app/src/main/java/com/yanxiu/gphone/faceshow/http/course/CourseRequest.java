package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程安排请求
 */

public class CourseRequest extends FaceShowMockRequest {
    public String id;

    @Override
    protected String urlPath() {
        return "/courseList";
    }

    @Override
    protected String getMockDataPath() {
        return null;
    }
}
