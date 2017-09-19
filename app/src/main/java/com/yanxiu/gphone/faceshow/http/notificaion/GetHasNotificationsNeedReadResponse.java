package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-19.
 */

public class GetHasNotificationsNeedReadResponse extends FaceShowBaseResponse {

    /**
     * data : {}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
    }
}
