package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by frc on 17-9-14.
 */

public class SignInResponse extends FaceShowBaseResponse {
    private UserInfo.Info data;

    public UserInfo.Info getData() {
        return data;
    }

    public void setData(UserInfo.Info data) {
        this.data = data;
    }
}
