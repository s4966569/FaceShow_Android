package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 18:53.
 * Function :
 */
public class SendClassCircleRequest extends RequestBase {

    public String method = "moment.publishMoment";
    public String token = SpManager.getToken();
    //    public String clazsId="7";
    public String clazsId = UserInfo.getInstance().getInfo().getClassId();
    public String content;
    public String resourceIds = "";
    /**
     * 通过七牛上传,写死
     */
    private String resourceSource = "qiniu";

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
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
