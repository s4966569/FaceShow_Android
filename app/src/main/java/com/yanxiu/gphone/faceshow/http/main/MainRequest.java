package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 首页请求
 */

public class MainRequest extends FaceShowBaseRequest {
    public String method = "app.clazs.getCurrentClazs";

    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "main.json";
//    }
}
