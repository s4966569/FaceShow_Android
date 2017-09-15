package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-15.
 */

public class NotificationRequest extends FaceShowMockRequest {

    public String pageIdx;
    public String pageSize;

    @Override
    protected String urlPath() {
        return "file/";
    }

    @Override
    protected String getMockDataPath() {
        return "notification.json";
    }
}
