package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * 讨论获取标题等配置接口
 */

public class DiscussCommentRequest extends FaceShowBaseRequest{
    public String method = "app.interact.getComment";
    public String stepId;//环节id

    @Override
    protected String urlPath() {
        return null;
    }

//    @Override
//    protected String getMockDataPath() {
//        return "discussCommon.json";
//    }
}
