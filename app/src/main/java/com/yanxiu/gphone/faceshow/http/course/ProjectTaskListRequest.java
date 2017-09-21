package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/9/20.
 */

public class ProjectTaskListRequest   extends FaceShowBaseRequest {
    public String id;

    public String method = "app.clazs.getTasks";
    /*班级id*/
    public String clazsId;

    @Override
    protected String urlPath() {
        return null;
    }

}
