package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by frc on 17-9-18.
 */

public class GetCheckInNotesRequest extends FaceShowMockRequest {
    public String id;

    @Override
    protected String urlPath() {
        return "/checkin_notes";
    }

    @Override
    protected String getMockDataPath() {
        return "checkin_notes.json";
    }
}
