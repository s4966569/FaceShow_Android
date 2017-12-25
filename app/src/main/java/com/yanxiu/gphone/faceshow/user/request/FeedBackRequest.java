package com.yanxiu.gphone.faceshow.user.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author  frc on 2017/12/21.
 */

public class FeedBackRequest extends FaceShowBaseRequest {
   private String method="feedback.submitFeedback";
   public String content;
   //面授
   private  String appId="22";
    //学员端
    private String platId="100";
    //面授
    private String sourceId="1";

    @Override
    protected String urlPath() {
        return null;
    }
}
