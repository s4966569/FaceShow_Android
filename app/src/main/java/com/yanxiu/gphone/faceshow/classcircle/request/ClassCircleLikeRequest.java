package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:36.
 * Function :
 */
public class ClassCircleLikeRequest extends FaceShowMockRequest {

    public String clazsId;
    public String momentId;

    @Override
    protected String urlPath() {
        return "http://orz.yanxiu.com/pxt/platform/data.api?method=moment.like";
    }

    @Override
    protected String getMockDataPath() {
        return "like.json";
    }
}
