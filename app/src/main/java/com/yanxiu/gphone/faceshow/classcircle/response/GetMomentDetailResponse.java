package com.yanxiu.gphone.faceshow.classcircle.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * @author frc on 2018/1/26.
 */

public class GetMomentDetailResponse extends FaceShowBaseResponse {




    private ClassCircleResponse.Data.Moments data;
    private long currentTime;

    public ClassCircleResponse.Data.Moments getData() {
        return data;
    }

    public void setData(ClassCircleResponse.Data.Moments data) {
        this.data = data;
    }
}
