package com.yanxiu.gphone.faceshow.homepage;

/**
 * 签到成功
 * Created by frc on 17-9-27.
 */

public class CheckInSuccessEvent {
    private String mMsg;
    private int mPosition;

    public CheckInSuccessEvent(String mMsg, int position) {
        this.mMsg = mMsg;
        this.mPosition = position;
    }

    public String getmMsg() {
        return mMsg;
    }

    public int getmPosition() {
        return mPosition;
    }
}
