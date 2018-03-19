package com.yanxiu.gphone.faceshow.course;


import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * @author frc  on 17-11-11.
 */

public class DeleteUserCommentResponse extends FaceShowBaseResponse {


    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
