package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by zhuxiaolong on 2018/3/2.
 */

public class PhoneNumCheckRequest extends FaceShowBaseRequest {

    private String method = "app.clazs.getCheckCode";
    public String mobile;
    public String clazsId;
    public String code;


    @Override
    protected String urlPath() {
        return null;
    }
}
