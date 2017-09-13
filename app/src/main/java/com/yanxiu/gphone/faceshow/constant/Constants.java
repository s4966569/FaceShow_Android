package com.yanxiu.gphone.faceshow.constant;


import com.yanxiu.gphone.faceshow.util.SystemUtil;

/**
 * Created by dyf
 * 存储常量
 */

public class Constants {
    public static final String TAG = "faceshow";
    //server配置 开始
    public static final String URL_SERVER_FILE_NAME = "env_config.json"; //存放server配置的文件名
    public static final String MULTICONFIG = "multiConfig"; //多环境节点配置模式
    //server配置 结束


    public static final String OS = "android";
    public static final String osType = "0";
    public static final String pcode = "010110000";
    public static String version = String.valueOf(SystemUtil.getVersionCode());

    public static final String DIR_ROOT = "/FaceShow";
    public static final String DIR_APP = "/app";
    public static final String DIR_IMAGE = "/image";

}
