package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 18:53.
 * Function :
 */
public class SendClassCircleRequest extends FaceShowMockRequest{

    public String claszId;
    public String content;
    public String resourceIds="";

    @Override
    protected String urlPath() {
        return "http://orz.yanxiu.com/pxt/platform/data.api?method=moment.publishMoment";
    }

    @Override
    protected String getMockDataPath() {
        return "sendclasscircle.json";
    }
}
