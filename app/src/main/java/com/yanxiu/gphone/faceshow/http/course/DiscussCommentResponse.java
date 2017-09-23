package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 讨论获取标题等配置接口
 */

public class DiscussCommentResponse extends FaceShowBaseResponse {

    private DiscussCommonResponseBean data = new DiscussCommonResponseBean();

    public DiscussCommonResponseBean getData() {
        return data;
    }

    public void setData(DiscussCommonResponseBean data) {
        this.data = data;
    }
}
