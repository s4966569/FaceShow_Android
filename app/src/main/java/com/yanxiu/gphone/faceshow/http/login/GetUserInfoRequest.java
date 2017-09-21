package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-21.
 */

public class GetUserInfoRequest extends FaceShowMockRequest {
    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String getMockDataPath() {
        return "sign_in.json";
    }

    @Override
    protected double setMockRequestErrorProbability() {
        return 0;
    }
}
