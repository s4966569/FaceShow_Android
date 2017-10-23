package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 获取签到详情
 * Created by frc on 17-10-23.
 */

public class GetCheckInDetailRequest extends FaceShowBaseRequest {
    public String method = "app.interact.getSignIn";
    public String stepId;

    @Override
    protected String urlPath() {
        return null;
    }
}
