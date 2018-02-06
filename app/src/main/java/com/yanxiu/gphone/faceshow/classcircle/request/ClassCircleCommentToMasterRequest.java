package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:58.
 * Function :
 */
public class ClassCircleCommentToMasterRequest extends RequestBase {

    public String method="moment.comment";
    public String token= SpManager.getToken();
    public String clazsId;
    public String momentId;
    public String content;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected HttpType httpType() {
        return HttpType.POST;
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
//        return "comment.json";
//    }
}
