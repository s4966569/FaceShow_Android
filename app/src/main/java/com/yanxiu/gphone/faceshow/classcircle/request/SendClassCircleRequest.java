package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 18:53.
 * Function :
 */
public class SendClassCircleRequest extends RequestBase{

    public String method="moment.publishMoment";
    public String token="ce0d56d0d8a214fb157be3850476ecb5";
    public String clazsId;
    public String content;
    public String resourceIds="";

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
//        return "sendclasscircle.json";
//    }
}
