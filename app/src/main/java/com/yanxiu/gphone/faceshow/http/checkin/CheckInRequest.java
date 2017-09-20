package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-19.
 */

public class CheckInRequest extends FaceShowMockRequest {
    public String scanString;

    @Override
    protected String urlPath() {
        return "/checkin";
    }

    @Override
    protected String getMockDataPath() {
        return "consume_red_pointer.json";
    }
}
