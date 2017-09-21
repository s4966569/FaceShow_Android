package com.yanxiu.gphone.faceshow.http.login;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.login.UserInfo;

/**
 * Created by frc on 17-9-21.
 */

public class GetUserInfoResponse extends FaceShowBaseResponse {

    /**
     * currentUser :
     * error : null
     * data : {"userId":"123456","userName":"孙长龙","headImg":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=2100774563,3164085752&fm=173&s=D400D7105E93A09CE5A05D870300B0E1&w=218&h=146&img.JPEG","phone":"15655240000","sex":"0","stageId":"123","stageName":"小学","classId":"02","className":"二班","subjectId":"56","subjectName":"语文","token":"ce0d56d0d8a214fb157be3850476ecb5","accountNumber":"15655240000","accountPassword":"123456"}
     */

    private UserInfo.Info data;


    public UserInfo.Info getData() {
        return data;
    }

    public void setData(UserInfo.Info data) {
        this.data = data;
    }


}
