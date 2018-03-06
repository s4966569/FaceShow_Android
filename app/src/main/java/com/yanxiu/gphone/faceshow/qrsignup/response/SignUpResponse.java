package com.yanxiu.gphone.faceshow.qrsignup.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;

/**
 * Created by srt on 2018/3/6.
 * 扫码注册 的 注册接口返回值
 */

public class SignUpResponse extends FaceShowBaseResponse {

    /**
     * sysUser : {"id":1351,"userId":24368269,"realName":"蜗蜗牛","mobilePhone":"15810319555","email":null,"stage":0,"subject":0,"userStatus":1,"ucnterId":24368269,"sex":null,"school":null,"avatar":null}
     */

    private SysUserBean sysUser;

    public SysUserBean getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserBean sysUser) {
        this.sysUser = sysUser;
    }


}
