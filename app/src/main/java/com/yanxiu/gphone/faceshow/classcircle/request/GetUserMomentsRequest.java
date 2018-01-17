package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * @author frc on 2018/1/17.
 */

public class GetUserMomentsRequest extends RequestBase {

    public String method = "app.moment.getUserMoments";
    public String clazsId = UserInfo.getInstance().getInfo().getClassId();
    public String limit = "10";
    public String userId;
    public String offset;
    public String token = SpManager.getToken();

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
}
