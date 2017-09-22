package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.course.bean.EvaluationBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 课程评价
 */

public class EvalutionResponse extends FaceShowBaseResponse {
    private EvaluationBean data = new EvaluationBean();

    public EvaluationBean getData() {
        return data;
    }

    public void setData(EvaluationBean data) {
        this.data = data;
    }
}
