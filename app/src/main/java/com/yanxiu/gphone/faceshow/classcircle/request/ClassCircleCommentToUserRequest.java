package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/21 11:17.
 * Function :
 */
public class ClassCircleCommentToUserRequest extends FaceShowMockRequest {

    public String clazsId;
    public String momentId;
    public String content;
    public String toUserId;
    public String commentId;

    @Override
    protected String urlPath() {
        return "http://orz.yanxiu.com/pxt/platform/data.api?method=moment.reply";
    }

    @Override
    protected String getMockDataPath() {
        return "comment.json";
    }
}
