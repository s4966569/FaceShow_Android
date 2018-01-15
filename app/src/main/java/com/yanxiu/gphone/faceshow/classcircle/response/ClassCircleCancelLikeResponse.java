package com.yanxiu.gphone.faceshow.classcircle.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * @author frc on 2018/1/15.
 */

public class ClassCircleCancelLikeResponse extends FaceShowBaseResponse {




    private Object data;
    private long currentTime;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
