package com.test.yanxiu.im_core.http;

/**
 * Created by cailei on 02/03/2018.
 */

// 心跳接口需要进入im界面后定期请求，以保持在线状态
public class OnlineHeartbeatRequest extends ImRequestBase {
    private String method="online.heartbeat";

    public String type = "app";        // 来源,移动端为app
    public long onlineSeconds;         // 在线有效时长，单位：秒
}
