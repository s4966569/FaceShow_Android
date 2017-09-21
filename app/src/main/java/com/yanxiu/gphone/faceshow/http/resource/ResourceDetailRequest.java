package com.yanxiu.gphone.faceshow.http.resource;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/9/20.
 */

public class ResourceDetailRequest   extends FaceShowBaseRequest {
    public String id;

    public String method = "resource.view";
    /*班级id*/
    public String resId;

    @Override
    protected String urlPath() {
        return null;
    }

}