package com.yanxiu.gphone.faceshow.user.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;

/**
 * Created by Think on 2017/10/20.
 */

public class ModifyUserInfoRequest extends FaceShowBaseRequest {
    public String method = "app.sysUser.updateMyInfo";
    public String realName;
    public String sex;
    public String subject;
    public String stage;
    public String school;
    public String url;

    @Override
    protected String urlPath() {
        return null;
    }
}
