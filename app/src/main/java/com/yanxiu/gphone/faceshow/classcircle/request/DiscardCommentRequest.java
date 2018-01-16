package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/16.
 */

public class DiscardCommentRequest extends FaceShowBaseRequest {
    private String method="moment.discardComment";
    public String commentId;
    @Override
    protected String urlPath() {
        return null;
    }
}
