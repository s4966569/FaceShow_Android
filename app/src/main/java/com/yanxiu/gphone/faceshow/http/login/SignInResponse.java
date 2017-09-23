package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-14.
 */

public class SignInResponse extends FaceShowBaseResponse {


    /**
     * data : success
     * token : 13a6560ba06956f76424a91bec792f6d
     */

    private String data;
    private String token;
    private String passport;

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
