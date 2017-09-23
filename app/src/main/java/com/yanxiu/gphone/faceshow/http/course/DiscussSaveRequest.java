package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 提交讨论
 */

public class DiscussSaveRequest extends FaceShowBaseRequest {
    public String method = "interact.saveUserComment";
    public String stepId;//环节id
    public String content;//

    @Override
    protected String urlPath() {
        return null;
    }

}
