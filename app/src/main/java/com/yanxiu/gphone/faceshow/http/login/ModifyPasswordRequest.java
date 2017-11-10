package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowWithOutTokenBaseRequest;

/**
 * 修改用户密码
 * Created by frc on 17-10-23.
 */

public class ModifyPasswordRequest extends FaceShowWithOutTokenBaseRequest {
    public String method = "sysUser.app.initPassword";
    public String mobile;
    public String code;
    public String password;

    @Override
    protected String urlPath() {
        return null;
    }
}
