package com.yanxiu.gphone.faceshow.util;

import com.test.yanxiu.network.ResponseBodyDealer;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class DESBodyDealer implements ResponseBodyDealer {
    @Override
    public String dealWithBody(String body) {
        return SysEncryptUtil.decryptDES(body,"DES");
    }
}
