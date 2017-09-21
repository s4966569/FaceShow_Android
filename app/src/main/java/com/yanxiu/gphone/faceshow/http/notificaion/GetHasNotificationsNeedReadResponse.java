package com.yanxiu.gphone.faceshow.http.notificaion;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * Created by frc on 17-9-19.
 */

public class GetHasNotificationsNeedReadResponse extends FaceShowBaseResponse {

    /**
     * data : {"hasUnView":true}
     * currentUser :
     * error : null
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * hasUnView : true
         */

        private boolean hasUnView;

        public boolean isHasUnView() {
            return hasUnView;
        }

        public void setHasUnView(boolean hasUnView) {
            this.hasUnView = hasUnView;
        }
    }
}
