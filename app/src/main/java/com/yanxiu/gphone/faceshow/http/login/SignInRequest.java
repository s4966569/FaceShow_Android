package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-9-14.
 */

public class SignInRequest extends FaceShowBaseRequest {
    public String accountNumber;
    public String accountPassword;

    @Override
    protected String urlPath() {
        return null;
    }
}
