package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.test.yanxiu.common_base.utils.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 15:36.
 * Function :
 */
public class ClassCircleRequest extends RequestBase {

    public String method="app.moment.getMoments";
    public String clazsId=UserInfo.getInstance().getInfo().getClassId();
    public String limit="10";
    public String offset;
    public String token= SpManager.getToken();
//    public String token="ce0d56d0d8a214fb157be3850476ecb5";

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
