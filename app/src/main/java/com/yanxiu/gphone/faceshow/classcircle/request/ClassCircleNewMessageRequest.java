package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleNewMessageRequest extends FaceShowBaseRequest {
    private String method="app.moment.getUserMomentMsg";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
