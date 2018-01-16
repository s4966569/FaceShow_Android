package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * 删除已发布的班级圈
 *
 * @author frc on 2018/1/16.
 */

public class DiscardMomentRequest extends FaceShowBaseRequest {
    private String method = "moment.discardMoment";
    public String momentId;

    @Override
    protected String urlPath() {
        return null;
    }
}
