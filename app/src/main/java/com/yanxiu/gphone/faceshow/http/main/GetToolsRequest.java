package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * @author frc on 2018/1/23.
 */

public class GetToolsRequest extends FaceShowBaseRequest {
    private String method = "app.tools.getTools";
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }
}
