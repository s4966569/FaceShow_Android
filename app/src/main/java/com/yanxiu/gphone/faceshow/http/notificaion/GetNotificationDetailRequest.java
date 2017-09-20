package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-9-18.
 */

public class GetNotificationDetailRequest extends FaceShowBaseRequest {

    /*通知id*/
    public String noticeId;

    public String method = "notice.detail";

    @Override
    protected String urlPath() {
        return null;
    }

}
