package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 10:58.
 * Function :
 */
public class ClassCircleCommentToMasterRequest extends FaceShowMockRequest {

    public String clazsId;
    public String momentId;
    public String content;

    @Override
    protected String urlPath() {
        return "http://orz.yanxiu.com/pxt/platform/data.api?method=moment.comment";
    }

    @Override
    protected String getMockDataPath() {
        return "comment.json";
    }
}
