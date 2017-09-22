package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.course.bean.VoteBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 课程评价
 */

public class VoteResponse extends FaceShowBaseResponse {

    private VoteBean data = new VoteBean();

    public VoteBean getData() {
        return data;
    }

    public void setData(VoteBean data) {
        this.data = data;
    }
}
