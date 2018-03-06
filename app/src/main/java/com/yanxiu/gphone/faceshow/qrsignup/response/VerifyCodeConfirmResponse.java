package com.yanxiu.gphone.faceshow.qrsignup.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;

/**
 * Created by srt on 2018/3/6.
 * 扫码注册功能内  验证手机号与验证码的返回值
 */

public class VerifyCodeConfirmResponse extends FaceShowBaseResponse {
    /**
     * msg : 您之前已经是研修宝用户，用此手机号登录后即可查看到该班级。
     * hasRegistUser : 2
     * sysUser : {"id":667,"userId":23249336,"realName":"李涛","mobilePhone":"18600988448","email":null,"stage":0,"subject":0,"userStatus":1,"ucnterId":null,"sex":1,"school":"技术产品中心","avatar":"http://orz.yanxiu.com/easygo/file/2018/1/27/1517034207965l89434_110-110.jpg","stageName":null,"imTokenInfo":null,"subjectName":null,"sexName":null}
     */

    private String msg;
    private int hasRegistUser;
    private SysUserBean sysUser;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getHasRegistUser() {
        return hasRegistUser;
    }

    public void setHasRegistUser(int hasRegistUser) {
        this.hasRegistUser = hasRegistUser;
    }

    public SysUserBean getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUserBean sysUser) {
        this.sysUser = sysUser;
    }




/*
//正常情况返回
{
    "code":0,
    "message":"",
    "data":{
        "msg":"您之前已经是研修宝用户，用此手机号登录后即可查看到该班级。",
        "hasRegistUser":2,  // 0-未注册用户  1-用户中心已注册用户  2-研修宝已注册用户
        "sysUser":{
            "id":667,
            "userId":23249336,
            "realName":"李涛",
            "mobilePhone":"18600988448",
            "email":null,
            "stage":0,
            "subject":0,
            "userStatus":1,
            "ucnterId":null,
            "sex":1,
            "school":"技术产品中心",
            "avatar":"http://orz.yanxiu.com/easygo/file/2018/1/27/1517034207965l89434_110-110.jpg",
            "stageName":null,
            "imTokenInfo":null,
            "subjectName":null,
            "sexName":null
        }
    },
    "currentUser":"",
    "currentTime":1520245092679,
    "error":null
}

//异常情况返回
{
    "currentUser":"",
    "code":110000,
    "message":"",
    "data":null,
    "error":{
        "code":111106,
        "title":"验证码错误或已失效",
        "message":"验证码错误!",
        "data":null
    }
}*/


}
