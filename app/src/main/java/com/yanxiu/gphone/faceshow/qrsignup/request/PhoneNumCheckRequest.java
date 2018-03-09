package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowWithOutTokenBaseRequest;

/**
 * Created by zhuxiaolong on 2018/3/2.
 */

public class PhoneNumCheckRequest extends FaceShowWithOutTokenBaseRequest {

    public String method = "app.clazs.registYxb";
    public String mobile;
    public String clazsId;
    public String code;


    @Override
    protected String urlPath() {
        return null;
    }
}
