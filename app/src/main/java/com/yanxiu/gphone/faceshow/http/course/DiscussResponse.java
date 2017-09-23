package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 课程详情
 */

public class DiscussResponse extends FaceShowBaseResponse {

    private DiscussResponseBean data = new DiscussResponseBean();

    public DiscussResponseBean getData() {
        return data;
    }

    public void setData(DiscussResponseBean data) {
        this.data = data;
    }
}
