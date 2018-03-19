package com.yanxiu.gphone.faceshow.http.base;

/**
 * 网络请求返回的类型
 *
 * @author frc
 *         created at 17-2-10.
 */

public class ResponseConfig {
    public static final String SUCCESS = "0";
    public static final String NET_ERROR = "当前无网络";
//    public static final int SUCCESS=0;
    public static final int INT_SUCCESS=0;



    /**
     * 在这里添加 各种异常码的 说明
     * */
    /**
     * 请求 参数 丢失
     * */
    public static final int ERROR_PARAMS_NOT_PRESENT=100;
    /**
     *
     * */
    public static final int ERROR_QR_VERIFYCODE_INVAID=111106;
    /**
     * 获取验证码 异常
     * */
    public static final int ERROR_QR_HAS_JOINED_CLASS=210309;


}
