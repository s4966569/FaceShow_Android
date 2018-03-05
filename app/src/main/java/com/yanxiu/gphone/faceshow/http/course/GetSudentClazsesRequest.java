package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseRequest;
import com.test.yanxiu.common_base.utils.UrlRepository;

/**
 * @author frc on 2018/1/18.
 */

public class GetSudentClazsesRequest extends FaceShowBaseRequest {
    private String method="app.clazs.getStudentClazses";

    @Override
    protected String urlServer() {
        return UrlRepository.getInstance().getChooseClassServer();
    }

    @Override
    protected String urlPath() {
        return null;
    }
}
