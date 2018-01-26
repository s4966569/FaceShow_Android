package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/26.
 */

public class GetMomentDetailRequest extends FaceShowBaseRequest {
    private String method = "moment.getMoment";
    public String momentId;

    @Override
    protected String urlPath() {
        return null;
    }
}
