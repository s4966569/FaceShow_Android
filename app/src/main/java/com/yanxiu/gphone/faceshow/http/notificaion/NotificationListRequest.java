package com.yanxiu.gphone.faceshow.http.notificaion;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by frc on 17-9-15.
 */

public class NotificationListRequest extends FaceShowBaseRequest {

    public String method = "app.notice.list";
    /*班级id*/
    public String clazzId;
    /*从第几条开始 默认0*/
    public String offset;
    /*每页多少条 默认10*/
    public String pageSize;
    /*搜索关键字*/
    public String title="";

    @Override
    protected String urlPath() {
        return null;
    }

}
