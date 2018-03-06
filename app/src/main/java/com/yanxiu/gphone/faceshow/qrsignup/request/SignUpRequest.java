package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by srt on 2018/3/6.
 */

public class SignUpRequest extends FaceShowBaseRequest {
    public String method="app.clazs.registUnenterAndYxb";
    public String clazsId;
    public String mobile;
    public String name;
    public String password;


    @Override
    protected String urlPath() {
        return null;
    }
}
