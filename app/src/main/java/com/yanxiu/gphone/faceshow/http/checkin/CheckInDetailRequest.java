package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-18.
 */

public class CheckInDetailRequest extends FaceShowMockRequest {
    public String ceckInId;
    @Override
    protected String urlPath() {
        return "checkInDetail";
    }

    @Override
    protected String getMockDataPath() {
        return "check_in_detail.json";
    }
}
