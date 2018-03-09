package com.yanxiu.gphone.faceshow.qrsignup.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowWithOutTokenBaseRequest;

/**
 * Created by srt on 2018/3/7.
 *
 * 扫描班级二维码后 获取目标班级信息的请求
 * 返回值 会带有 班级详细信息
 */

public class QrClazsInfoRequest extends FaceShowWithOutTokenBaseRequest {

    public String clazsId;
    public String method="clazs.scanClazsCode";
    public String from="yxbApp";
    public String platId="100";

    @Override
    protected String urlPath() {
        return null;
    }
}
