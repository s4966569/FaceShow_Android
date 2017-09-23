package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 课程评价请求
 */

public class SubmitVoteRequest extends FaceShowBaseRequest {
    public static final String VOTE = "interact.saveUserVote";
    public static final String EVALUATION = "interact.saveUserQuestionnaire";
    public String method;
    public String stepId;
    public String answers;

    @Override
    protected String urlPath() {
        return null;
    }

}
