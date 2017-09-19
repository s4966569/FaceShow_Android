package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-19.
 */

public class GetHasNotificationsNeedReadRequest extends FaceShowMockRequest {
    @Override
    protected String urlPath() {
        return "/red_pointer";
    }

    @Override
    protected String getMockDataPath() {
        return "red_pointer.json";
    }
}
