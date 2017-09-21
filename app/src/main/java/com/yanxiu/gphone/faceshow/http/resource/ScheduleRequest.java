package com.yanxiu.gphone.faceshow.http.resource;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/9/21.
 */

public class ScheduleRequest  extends FaceShowBaseRequest {
    public String id;

    public String method = "schedule.list";
    /*班级id*/
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

}

