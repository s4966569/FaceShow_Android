package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowWithOutTokenBaseRequest;

/**
 * Created by srt on 2018/3/9.
 */

public class VerifyCodeRequest extends FaceShowWithOutTokenBaseRequest {
    public  String method="app.clazs.getCheckCode";
    public String mobile;
    public String clazsId;
    @Override
    protected String urlPath() {
        return null;
    }
}
