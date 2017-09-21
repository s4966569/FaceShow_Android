package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;

/**
 * 登录请求
 * Created by frc on 17-9-14.
 */

public class SignInRequest extends FaceShowBaseRequest {
    public String type = "MOBILE";
    public String loginName;
    public String password;
    public String appKey = "f749edf6-bc39-6ef9-8f81-158se5fds842";


    @Override
    protected String urlPath() {
        return "/appLogin";
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getLoginServer();
    }

}
