package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 2017/12/7.
 */

public class UserSignInRequest extends FaceShowBaseRequest {
    private String method="interact.userSignIn";
    public String position;
    public String site;
    public String stepId;
    public String timestamp;
    private String from="yxbApp";
    private String device="android";
    @Override
    protected String urlPath() {
        return null;
    }
}
