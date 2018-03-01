package com.yanxiu.gphone.faceshow.qrsignup;

/**
 * Created by srt on 2018/3/1.
 * 负责处理用户信息注册流程的类
 */

public final class SignUpManager {


    /**
     * 发送网路请求 验证目标手机号 是否已经注册 由服务器返回 结果
     * 1、未注册手机号
     * 2、已注册手机号 但是未添加目标课程
     * 3、已注册手机号 并且已经添加目标课程
     * */
    public void checkPhoneNumber(String number){
        // TODO: 2018/3/1  网络请求验证 手机号
    }

    /**
     * 手机验证的第二步  目标号码为未注册手机时
     * 1、对当前手机号进行密码设置  上传参数应为手机号与密码
     *
     * */
    public void setPasword(String phoneNumber,String password){

    }


    /**
     * 完善用户资料
     * */
    public void setProfile(){

    }


}
