package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-18.
 */

public class GetNotificationDetailRequest extends FaceShowMockRequest {
    public String id;

    @Override
    protected String urlPath() {
        return "/getNotificationDetial";
    }

    @Override
    protected String getMockDataPath() {
        return "notificationDetail.json";
    }
}
