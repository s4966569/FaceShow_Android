package com.yanxiu.gphone.faceshow.http.course;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.yanxiu.gphone.faceshow.http.base.FaceShowMockRequest;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;

/**
 * 讨论
 */

public class DiscussRequest extends RequestBase {
    public String method = "app.interact.commentRecords";
    public String stepId;//环节id
    public String id;//第一次获取不传，以后每次传返回值这种的callbackValue
    public String limit = "20";//每页数据：默认20
    public String order;//排序：asc或desc 默认desc
    public String token= SpManager.getToken();

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

//    @Override
//    protected String getMockDataPath() {
//        return "discuss.json";
//    }
}
