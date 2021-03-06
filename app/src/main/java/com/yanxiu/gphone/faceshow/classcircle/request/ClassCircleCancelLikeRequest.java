package com.yanxiu.gphone.faceshow.classcircle.request;

import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.test.yanxiu.common_base.utils.UrlRepository;


/**
 * @author frc on 2018/1/15.
 */

public class ClassCircleCancelLikeRequest extends RequestBase {
    private String method = "moment.cancelLike";
    private String token = SpManager.getToken();
    public String momentId;

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
}
