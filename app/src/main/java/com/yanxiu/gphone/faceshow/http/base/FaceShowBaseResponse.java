package com.yanxiu.gphone.faceshow.http.base;

/**
 * rersponse基类
 */

public class FaceShowBaseResponse {
    protected StatusBean status;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}
