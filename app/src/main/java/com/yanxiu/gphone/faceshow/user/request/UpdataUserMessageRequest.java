package com.yanxiu.gphone.faceshow.user.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/28 18:21.
 * Function :
 */
public class UpdataUserMessageRequest extends RequestBase {

//    http://yxb.yanxiu.com/pxt/platform/data.api?
//    // avatar=http%3A%2F%2Fs1.jsyxw.cn%2Feasygo%2Ffile%2F2017%2F9%2F28%2F1506593862111l29215_640-640.jpg&
//    // token=b7458e92c55c295b1e7030ae79dcfe6a&
//    // method=sysUser.updateAvatar
    public String avatar;
    public String token= SpManager.getToken();
    public String method="sysUser.updateAvatar";

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
