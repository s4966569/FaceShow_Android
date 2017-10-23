package com.yanxiu.gphone.faceshow.http.checkin;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 获取签到详情
 * Created by frc on 17-10-23.
 */

public class GetCheckInDetailResponse extends FaceShowBaseResponse {
    private DataBean data;

    public DataBean getData() {
        return data;
    }


    public class DataBean {
        private int interactType;

        private GetCheckInNotesResponse.CheckInNotesBean signIn;

        public int getInteractType() {
            return interactType;
        }

        public void setInteractType(int interactType) {
            this.interactType = interactType;
        }

        public GetCheckInNotesResponse.CheckInNotesBean getSignIn() {
            return signIn;
        }

        public void setSignIn(GetCheckInNotesResponse.CheckInNotesBean signIn) {
            this.signIn = signIn;
        }
    }

}
