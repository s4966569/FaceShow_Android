package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程评价请求
 */

public class EvaluationRequest extends FaceShowMockRequest {
    public String method = "app.interact.getQuestionnaire";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String getMockDataPath() {
        return "vote.json";
    }
}
