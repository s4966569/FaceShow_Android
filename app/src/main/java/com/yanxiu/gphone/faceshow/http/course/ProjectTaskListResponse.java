package com.yanxiu.gphone.faceshow.http.course;

import com.yanxiu.gphone.faceshow.base.BaseBean;
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

    private List<ProjectTaskBean> data;

    public List<ProjectTaskBean> getData() {
        return data;
    }

    public void setData(List<ProjectTaskBean> data) {
        this.data = data;
    }

    public static class ProjectTaskBean extends BaseBean{
        /**
         * stepId : 2
         * projectId : 1
         * clazsId : 2
         * courseId : 2
         * interactName : test投票
         * interactType : 3
         * interactId : 481
         * stepStatus : 1
         * createTime : 2017-09-15 17:01:04
         * stepFinished : 0
         * interactTypeName : 投票
         */

        private int stepId;
        private int projectId;
        private int clazsId;
        private int courseId;
        private String interactName;
        private int interactType;
        private int interactId;
        private int stepStatus;
        private String createTime;
        private String stepFinished;
        private String interactTypeName;

        public int getStepId() {
            return stepId;
        }

        public void setStepId(int stepId) {
            this.stepId = stepId;
        }

        public int getProjectId() {
            return projectId;
        }

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getClazsId() {
            return clazsId;
        }

        public void setClazsId(int clazsId) {
            this.clazsId = clazsId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getInteractName() {
            return interactName;
        }

        public void setInteractName(String interactName) {
            this.interactName = interactName;
        }

        public int getInteractType() {
            return interactType;
        }

        public void setInteractType(int interactType) {
            this.interactType = interactType;
        }

        public int getInteractId() {
            return interactId;
        }

        public void setInteractId(int interactId) {
            this.interactId = interactId;
        }

        public int getStepStatus() {
            return stepStatus;
        }

        public void setStepStatus(int stepStatus) {
            this.stepStatus = stepStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStepFinished() {
            return stepFinished;
        }

        public void setStepFinished(String stepFinished) {
            this.stepFinished = stepFinished;
        }

        public String getInteractTypeName() {
            return interactTypeName;
        }

        public void setInteractTypeName(String interactTypeName) {
            this.interactTypeName = interactTypeName;
        }
    }
}
