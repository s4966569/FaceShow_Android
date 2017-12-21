package com.yanxiu.gphone.faceshow.user.request;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * @author  frc on 2017/12/21.
 */

public class FeedBackResponse extends FaceShowBaseResponse {



    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }



    public static class DataBean {
    }
}
