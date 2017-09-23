package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程安排请求
 */

public class CourseArrangeRequest extends FaceShowBaseRequest {
    public String method = "app.clazs.courses";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "courseArrange.json";
//    }
}
