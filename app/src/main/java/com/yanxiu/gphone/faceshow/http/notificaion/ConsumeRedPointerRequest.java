package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-19.
 */

public class ConsumeRedPointerRequest extends FaceShowMockRequest {
    @Override
    protected String urlPath() {
        return "/consumeRedPointer";
    }

    @Override
    protected String getMockDataPath() {
        return "consume_red_pointer.json";
    }
}
