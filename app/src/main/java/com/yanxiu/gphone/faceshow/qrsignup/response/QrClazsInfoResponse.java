package com.yanxiu.gphone.faceshow.qrsignup.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;

import java.io.Serializable;

/**
 * Created by srt on 2018/3/7.
 */

public class QrClazsInfoResponse extends FaceShowBaseResponse {


    /**
     * data : {"clazsInfo":{"id":88,"platId":1,"projectId":79,"clazsName":"尚睿通2018公司年会","clazsStatus":1,"clazsType":1,"startTime":"2018-02-12 00:00:00","endTime":"2018-02-12 00:00:00","description":"尚睿通公司年会","topicId":null,"manager":null,"master":null,"clazsStatusName":null,"projectName":null},"clazsId":88,"sysUser":{"id":82,"userId":9768712,"realName":"多隆测试","mobilePhone":"18012345678","email":null,"stage":1202,"subject":1103,"userStatus":1,"ucnterId":null,"sex":1,"school":null,"avatar":null,"stageName":"小学","imTokenInfo":null,"subjectName":"数学","sexName":null}}
     * currentUser :
     * currentTime : 1520306066805
     * error : null
     */

    private DataBean data;

    private long currentTime;


    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }



    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }


    public static class DataBean implements Serializable{
        /**
         * clazsInfo : {"id":88,"platId":1,"projectId":79,"clazsName":"尚睿通2018公司年会","clazsStatus":1,"clazsType":1,"startTime":"2018-02-12 00:00:00","endTime":"2018-02-12 00:00:00","description":"尚睿通公司年会","topicId":null,"manager":null,"master":null,"clazsStatusName":null,"projectName":null}
         * clazsId : 88
         * sysUser : {"id":82,"userId":9768712,"realName":"多隆测试","mobilePhone":"18012345678","email":null,"stage":1202,"subject":1103,"userStatus":1,"ucnterId":null,"sex":1,"school":null,"avatar":null,"stageName":"小学","imTokenInfo":null,"subjectName":"数学","sexName":null}
         */

        private ClazsInfoBean clazsInfo;
        private int clazsId;
        private SysUserBean sysUser;

        public ClazsInfoBean getClazsInfo() {
            return clazsInfo;
        }

        public void setClazsInfo(ClazsInfoBean clazsInfo) {
            this.clazsInfo = clazsInfo;
        }

        public int getClazsId() {
            return clazsId;
        }

        public void setClazsId(int clazsId) {
            this.clazsId = clazsId;
        }

        public SysUserBean getSysUser() {
            return sysUser;
        }

        public void setSysUser(SysUserBean sysUser) {
            this.sysUser = sysUser;
        }

        public static class ClazsInfoBean {
            /**
             * id : 88
             * platId : 1
             * projectId : 79
             * clazsName : 尚睿通2018公司年会
             * clazsStatus : 1
             * clazsType : 1
             * startTime : 2018-02-12 00:00:00
             * endTime : 2018-02-12 00:00:00
             * description : 尚睿通公司年会
             * topicId : null
             * manager : null
             * master : null
             * clazsStatusName : null
             * projectName : null
             */

            private int id;
            private int platId;
            private int projectId;
            private String clazsName;
            private int clazsStatus;
            private int clazsType;
            private String startTime;
            private String endTime;
            private String description;
            private String topicId;
            private String manager;
            private String master;
            private String clazsStatusName;
            private String projectName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPlatId() {
                return platId;
            }

            public void setPlatId(int platId) {
                this.platId = platId;
            }

            public int getProjectId() {
                return projectId;
            }

            public void setProjectId(int projectId) {
                this.projectId = projectId;
            }

            public String getClazsName() {
                return clazsName;
            }

            public void setClazsName(String clazsName) {
                this.clazsName = clazsName;
            }

            public int getClazsStatus() {
                return clazsStatus;
            }

            public void setClazsStatus(int clazsStatus) {
                this.clazsStatus = clazsStatus;
            }

            public int getClazsType() {
                return clazsType;
            }

            public void setClazsType(int clazsType) {
                this.clazsType = clazsType;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Object getTopicId() {
                return topicId;
            }

            public void setTopicId(String topicId) {
                this.topicId = topicId;
            }

            public String getManager() {
                return manager;
            }

            public void setManager(String manager) {
                this.manager = manager;
            }

            public String getMaster() {
                return master;
            }

            public void setMaster(String master) {
                this.master = master;
            }

            public String getClazsStatusName() {
                return clazsStatusName;
            }

            public void setClazsStatusName(String clazsStatusName) {
                this.clazsStatusName = clazsStatusName;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }
        }

    }
}
