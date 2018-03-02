package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

public class PolicyConfigResponse extends  ImResponseBase {
    public Data data;

    public class Data {
        public long heartbeatInterval;
        public long nextPolicyConfigTime;
    }
}
