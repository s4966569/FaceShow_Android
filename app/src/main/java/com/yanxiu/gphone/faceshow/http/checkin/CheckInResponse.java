package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 签到
 * Created by frc on 17-9-19.
 */

public class CheckInResponse extends FaceShowBaseResponse {


    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
