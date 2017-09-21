package com.yanxiu.gphone.faceshow.classcircle.request;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/20 15:36.
 * Function :
 */
public class ClassCircleRequest extends FaceShowMockRequest {

    public String claszId;
    public String limit="10";
    public String offset;

    @Override
    protected String urlPath() {
        return "http://orz.yanxiu.com/pxt/platform/data.api?method=app.moment.getMoments";
    }

    @Override
    protected String getMockDataPath() {
        return "classcircle.json";
    }
}
