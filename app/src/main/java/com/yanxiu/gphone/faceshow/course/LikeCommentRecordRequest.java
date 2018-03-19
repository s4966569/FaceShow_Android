package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 17-11-11.
 */

public class LikeCommentRecordRequest extends FaceShowBaseRequest {
    private String method = "interact.likeCommentRecord";
    public String commentRecordId;

    @Override
    protected String urlPath() {
        return null;
    }
}
