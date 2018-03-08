package com.yanxiu.gphone.faceshow.qrsignup.response;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;
import com.yanxiu.gphone.faceshow.qrsignup.SysUserBean;

/**
 * Created by srt on 2018/3/5.
 * 扫码注册界面 检查手机号用户类型
 */

public class CheckPhoneNumResponse extends FaceShowBaseResponse {


    /**
     * data : {"msg":"您之前已经是研修宝用户，用此手机号登录后即可查看到该班级。","hasRegistUser":2,"clazsInfo":{"id":10,"platId":1,"projectId":28,"clazsName":"第一次面授培训（3天）","clazsStatus":1,"clazsType":1,"startTime":"2017-09-25 00:00:00","endTime":"2017-09-27 00:00:00","description":"第一次面授","topicId":null,"manager":null,"master":null,"clazsStatusName":null,"projectName":null},"sysUser":{"id":497,"userId":23246747,"realName":"丰荣成","mobilePhone":"15655248880","email":null,"stage":1203,"subject":1105,"userStatus":1,"ucnterId":null,"sex":1,"school":"技术产品中心","avatar":"http://orz.yanxiu.com/easygo/file/2018/3/2/1519977556639l17852_300-300.jpg","stageName":null,"imTokenInfo":null,"subjectName":null,"sexName":null}}
     * currentUser :
     * currentTime : 1520489197358
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



    public static class DataBean {
        /**
         * msg : 您之前已经是研修宝用户，用此手机号登录后即可查看到该班级。
         * hasRegistUser : 2
         * clazsInfo : {"id":10,"platId":1,"projectId":28,"clazsName":"第一次面授培训（3天）","clazsStatus":1,"clazsType":1,"startTime":"2017-09-25 00:00:00","endTime":"2017-09-27 00:00:00","description":"第一次面授","topicId":null,"manager":null,"master":null,"clazsStatusName":null,"projectName":null}
         * sysUser : {"id":497,"userId":23246747,"realName":"丰荣成","mobilePhone":"15655248880","email":null,"stage":1203,"subject":1105,"userStatus":1,"ucnterId":null,"sex":1,"school":"技术产品中心","avatar":"http://orz.yanxiu.com/easygo/file/2018/3/2/1519977556639l17852_300-300.jpg","stageName":null,"imTokenInfo":null,"subjectName":null,"sexName":null}
         */

        private String msg;
        private int hasRegistUser;
        private ClazsInfoBean clazsInfo;
        private SysUserBean sysUser;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getHasRegistUser() {
            return hasRegistUser;
        }

        public void setHasRegistUser(int hasRegistUser) {
            this.hasRegistUser = hasRegistUser;
        }

        public ClazsInfoBean getClazsInfo() {
            return clazsInfo;
        }

        public void setClazsInfo(ClazsInfoBean clazsInfo) {
            this.clazsInfo = clazsInfo;
        }

        public SysUserBean getSysUser() {
            return sysUser;
        }

        public void setSysUser(SysUserBean sysUser) {
            this.sysUser = sysUser;
        }

        public static class ClazsInfoBean {
            /**
             * id : 10
             * platId : 1
             * projectId : 28
             * clazsName : 第一次面授培训（3天）
             * clazsStatus : 1
             * clazsType : 1
             * startTime : 2017-09-25 00:00:00
             * endTime : 2017-09-27 00:00:00
             * description : 第一次面授
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
            private Object topicId;
            private Object manager;
            private Object master;
            private Object clazsStatusName;
            private Object projectName;

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

            public void setTopicId(Object topicId) {
                this.topicId = topicId;
            }

            public Object getManager() {
                return manager;
            }

            public void setManager(Object manager) {
                this.manager = manager;
            }

            public Object getMaster() {
                return master;
            }

            public void setMaster(Object master) {
                this.master = master;
            }

            public Object getClazsStatusName() {
                return clazsStatusName;
            }

            public void setClazsStatusName(Object clazsStatusName) {
                this.clazsStatusName = clazsStatusName;
            }

            public Object getProjectName() {
                return projectName;
            }

            public void setProjectName(Object projectName) {
                this.projectName = projectName;
            }
        }


    }
}
