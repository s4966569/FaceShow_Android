package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

// 1.1 获取通用配置
public class PolicyConfigRequest extends ImRequestBase {
    private String method="policy.config";

    public String v;     // 版本：默认1.0
}
