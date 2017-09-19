package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-14.
 */

public class SignInRequest extends FaceShowMockRequest {
    public String accountNumber;
    public String accountPassword;

    @Override
    protected String urlPath() {
        return "/sign_in";
    }

    @Override
    protected String getMockDataPath() {
        return "sign_in.json";
    }
}
