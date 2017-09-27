package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 获取是否有未读通知
 * Created by frc on 17-9-19.
 */

public class GetHasNotificationsNeedReadRequest extends FaceShowBaseRequest {
    public String method = "notice.hasUnView";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

}
