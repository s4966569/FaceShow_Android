package com.test.yanxiu.im_core.http;

import com.test.yanxiu.common_base.utils.UrlRepository;
import com.test.yanxiu.network.RequestBase;

/**
 * Created by cailei on 02/03/2018.
 */

public class ImRequestBase extends RequestBase {
    @Override
    protected boolean shouldLog() {
        return false;
    }

    @Override
    protected String urlServer() {
        return null;
    }

    @Override
    protected String urlPath() {
        return null;
    }

    public void test() {
        UrlRepository.getInstance().getLoginServer();
    }
}
