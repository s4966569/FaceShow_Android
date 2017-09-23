package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 课程评价请求
 */

public class VoteRequest extends FaceShowBaseRequest {
    public String method = "app.interact.getVote";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "vote.json";
//    }
}
