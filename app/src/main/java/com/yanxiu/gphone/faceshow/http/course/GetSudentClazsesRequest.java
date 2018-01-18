package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/18.
 */

public class GetSudentClazsesRequest extends FaceShowBaseRequest {
    private String method="app.clazs.getStudentClazses";

    @Override
    protected String urlServer() {
        return "http://orz.yanxiu.com/pxt/v1.1/platform/data.api";
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
