package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:36.
 * Function :
 */
public class ClassCircleLikeRequest extends RequestBase {

    public String method="moment.like";
    public String token= UserInfo.getInstance().getInfo().getToken();
    public String clazsId=UserInfo.getInstance().getInfo().getClassId();
    public String momentId;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "like.json";
//    }
}
