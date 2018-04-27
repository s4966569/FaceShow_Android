package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowWithOutTokenBaseRequest;

/**
 * Created by srt on 2018/3/7.
 * 扫码注册 保存用户信息请求
 * 适用于 无登录token 时更改用户信息
 */

public class UpdateProfileRequest extends FaceShowWithOutTokenBaseRequest {
    public String method = "app.sysUser.updateUserInfo";
    public String userId;
    public String realName;
    public String sex;
    public String subject;
    public String stage;
    public String school;

    @Override
    protected String urlPath() {
        return null;
    }
}
