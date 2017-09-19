package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程评价请求
 */

public class EvaluationRequest extends FaceShowMockRequest {
    public String id;

    @Override
    protected String urlPath() {
        return "/courseDetail";
    }

    @Override
    protected String getMockDataPath() {
        return null;
    }
}
