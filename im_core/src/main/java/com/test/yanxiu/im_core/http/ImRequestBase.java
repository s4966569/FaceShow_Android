package com.test.yanxiu.im_core.http;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;

import java.util.UUID;

/**
 * Created by cailei on 02/03/2018.
 */

public class ImRequestBase extends RequestBase {
    public String bizSource = "1";  // 来源，移动端用1
    public String bizId = "1";      // 业务id，研修宝用1
    public String bizToken;         // App用的Token
    public String imToken;          // 专门为im用的Token
    protected String reqId;         // 客户端生成的，保证唯一性的32位uuid

    ImRequestBase() {
        reqId = UUID.randomUUID().toString();
    }

    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getImServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
