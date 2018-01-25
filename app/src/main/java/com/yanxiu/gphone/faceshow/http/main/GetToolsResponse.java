package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @author frc on 2018/1/23.
 */

public class GetToolsResponse extends FaceShowBaseResponse {



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
        private List<ToolsBean> tools;
        private List<?> notices;

        public List<ToolsBean> getTools() {
            return tools;
        }

        public void setTools(List<ToolsBean> tools) {
            this.tools = tools;
        }

        public List<?> getNotices() {
            return notices;
        }

        public void setNotices(List<?> notices) {
            this.notices = notices;
        }

        public static class ToolsBean implements Serializable {

            private int id;
            private int parentId;
            private int platId;
            private int projectId;
            private int clazsId;
            private String name;
            private String toolType;
            private String event;
            private EventObjBean eventObj;
            private int state;
            private int toolOrder;
            private int needToken;
            private int needScene;
            private Object description;
            private Object subTools;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
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

            public int getClazsId() {
                return clazsId;
            }

            public void setClazsId(int clazsId) {
                this.clazsId = clazsId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getToolType() {
                return toolType;
            }

            public void setToolType(String toolType) {
                this.toolType = toolType;
            }

            public String getEvent() {
                return event;
            }

            public void setEvent(String event) {
                this.event = event;
            }

            public EventObjBean getEventObj() {
                return eventObj;
            }

            public void setEventObj(EventObjBean eventObj) {
                this.eventObj = eventObj;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getToolOrder() {
                return toolOrder;
            }

            public void setToolOrder(int toolOrder) {
                this.toolOrder = toolOrder;
            }

            public int getNeedToken() {
                return needToken;
            }

            public void setNeedToken(int needToken) {
                this.needToken = needToken;
            }

            public int getNeedScene() {
                return needScene;
            }

            public void setNeedScene(int needScene) {
                this.needScene = needScene;
            }

            public Object getDescription() {
                return description;
            }

            public void setDescription(Object description) {
                this.description = description;
            }

            public Object getSubTools() {
                return subTools;
            }

            public void setSubTools(Object subTools) {
                this.subTools = subTools;
            }

            public static class EventObjBean implements Serializable {

                private String eventType;
                private String title;
                private String content;
                private Object vHtml;

                public String getEventType() {
                    return eventType;
                }

                public void setEventType(String eventType) {
                    this.eventType = eventType;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public Object getVHtml() {
                    return vHtml;
                }

                public void setVHtml(Object vHtml) {
                    this.vHtml = vHtml;
                }
            }
        }
    }
}
