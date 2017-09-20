package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程详情请求
 */

public class DiscussRequest extends FaceShowMockRequest {
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
