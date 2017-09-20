package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 签到记录
 * Created by frc on 17-9-18.
 */

public class GetCheckInNotesRequest extends FaceShowMockRequest {
    public String method = "app.interact.userSignInRecords";
    public String pageSize;
    public String offset;

    @Override
    protected String urlPath() {
        return null;
    }

    @Override
    protected String getMockDataPath() {
        return "checkin_notes.json";
    }
}
