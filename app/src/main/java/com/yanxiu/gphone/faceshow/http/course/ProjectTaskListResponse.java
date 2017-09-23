package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by lufengqing on 2017/9/20.
 */

public class ProjectTaskListResponse extends FaceShowBaseResponse {
    /**
     * data : [{"stepId":2,"projectId":1,"clazsId":2,"courseId":2,"interactName":"test投票","interactType":3,"interactId":481,"stepStatus":1,"createTime":"2017-09-15 17:01:04","stepFinished":0,"interactTypeName":"投票"},{"stepId":3,"projectId":1,"clazsId":2,"courseId":0,"interactName":"test投票","interactType":3,"interactId":482,"stepStatus":1,"createTime":"2017-09-15 17:01:04","stepFinished":0,"interactTypeName":"投票"}]
     * error : null
     */

    private List<InteractStepsBean> data;

    public List<InteractStepsBean> getData() {
        return data;
    }

    public void setData(List<InteractStepsBean> data) {
        this.data = data;
    }


}
