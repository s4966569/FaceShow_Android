package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by srt on 2018/3/9.
 */

public class ScanClazsCodeRequest extends FaceShowBaseRequest {
    public String clazsId;
    public String method="clazs.scanClazsCode";
    public String from="yxbApp";
    public String platId="100";

    @Override
    protected String urlPath() {
        return null;
    }
}
