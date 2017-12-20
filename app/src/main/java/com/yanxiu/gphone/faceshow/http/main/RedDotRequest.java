package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 红点请求接口
 * @author  frc on 2017/12/20.
 */

public class RedDotRequest extends FaceShowBaseRequest {
    public String method="prompt.getUserPrompts";
    public String clazsId;
    public String bizIds;


    @Override
    protected String urlPath() {
        return null;
    }
}
