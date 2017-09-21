package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-9-19.
 */

public class CheckInRequest extends FaceShowBaseRequest {
    public String scanString;
    public String method="interact.replenishSignIn";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }

}
