package com.yanxiu.gphone.faceshow.http.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.constant.Constants;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.util.NetWorkUtils;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/7 17:17.
 * Function :
 */
public class InitializeRequest extends RequestBase {
    public String did= Constants.deviceId;
    public String brand=Constants.BRAND;
    public String nettype= NetWorkUtils.getNetWorkType();
    public String osType=Constants.osType;
    public String os=Constants.OS;
    public String debugtoken="";
    public String trace_uid= "123";
    public String appVersion=Constants.version;
    public String osVer=Constants.version;
    public String mode= UrlRepository.getInstance().getMode()!=null?UrlRepository.getInstance().getMode():"test";
    public String operType=Constants.OPERTYPE;
    public String phone="-";
    public String remoteIp="";
    public String productLine=Constants.PRODUCTLINE;
    public String content="";
    public String channel;

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getInitializeServer();
    }

    @Override
    protected String urlPath() {
        return "/app/log/uploadDeviceLog/release.do";
    }
}
