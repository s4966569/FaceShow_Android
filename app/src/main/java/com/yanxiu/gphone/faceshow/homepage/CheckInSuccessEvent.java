package com.yanxiu.gphone.faceshow.homepage;

/**
 * 签到成功
 * Created by frc on 17-9-27.
 */

public class CheckInSuccessEvent {
    private String mMsg;

    public CheckInSuccessEvent(String mMsg) {
        this.mMsg = mMsg;
    }

    public String getmMsg() {
        return mMsg;
    }
}
