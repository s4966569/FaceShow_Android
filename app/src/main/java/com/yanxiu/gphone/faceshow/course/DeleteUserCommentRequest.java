package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-11-11.
 */

public class DeleteUserCommentRequest extends FaceShowBaseRequest {
    private String method = "interact.deleteUserComment";
    public String commentRecordId;

    @Override
    protected String urlPath() {
        return null;
    }
}
