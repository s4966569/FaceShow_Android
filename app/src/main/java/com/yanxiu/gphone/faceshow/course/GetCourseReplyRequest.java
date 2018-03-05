package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-10.
 */

public class GetCourseReplyRequest extends FaceShowBaseRequest {
    private String method = "app.interact.getComment";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
