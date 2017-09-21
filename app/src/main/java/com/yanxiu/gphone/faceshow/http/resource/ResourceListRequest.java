package com.yanxiu.gphone.faceshow.http.resource;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by lufengqing on 2017/9/20.
 */

public class ResourceListRequest  extends FaceShowBaseRequest {
    public String id;

    public String method = "resource.list";
    /*班级id*/
    public String clazsId;
    /*从第几条开始 默认0*/
    public String offset;
    /*每页多少条 默认10*/
    public String pageSize;
    /*搜索关键字*/
    public String keyword;

    @Override
    protected String urlPath() {
        return null;
    }

}

