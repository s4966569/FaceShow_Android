package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 通过token获取用户信息
 * Created by frc on 17-9-21.
 */

public class GetUserInfoRequest extends FaceShowBaseRequest {
    public String method = "app.sysUser.userInfo";

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "sign_in.json";
//    }
//
//    @Override
//    protected double setMockRequestErrorProbability() {
//        return 0;
//    }
//
//    @Override
//    protected int getDelayTime() {
//        return 0;
//    }
}
