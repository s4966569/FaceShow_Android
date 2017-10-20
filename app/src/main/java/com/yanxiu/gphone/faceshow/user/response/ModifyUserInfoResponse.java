package com.yanxiu.gphone.faceshow.user.response;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by Think on 2017/10/20.
 */

public class ModifyUserInfoResponse extends FaceShowBaseResponse {

    /**
     * data : null
     * currentUser :
     * currentTime : 1508470178078
     * error : null
     */

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
