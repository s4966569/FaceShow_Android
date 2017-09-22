package com.yanxiu.gphone.faceshow.http.resource;

import com.google.gson.annotations.SerializedName;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by lufengqing on 2017/9/21.
 */

public class ScheduleResponse extends FaceShowBaseResponse{
    /**
     * data : {"schedules":{"elements":[{"id":6,"clazsId":1,"startTime":"2017-09-21 02:20:29","endTime":"2017-09-21 02:20:29","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"},{"id":7,"clazsId":1,"startTime":"2017-09-21 02:20:34","endTime":"2017-09-21 02:20:34","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"}],"pageSize":10,"pageNum":null,"offset":0,"totalElements":2,"lastPageNumber":null}}
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
         * schedules : {"elements":[{"id":6,"clazsId":1,"startTime":"2017-09-21 02:20:29","endTime":"2017-09-21 02:20:29","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"},{"id":7,"clazsId":1,"startTime":"2017-09-21 02:20:34","endTime":"2017-09-21 02:20:34","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"}],"pageSize":10,"pageNum":null,"offset":0,"totalElements":2,"lastPageNumber":null}
         */

        private SchedulesBean schedules;

        public SchedulesBean getSchedules() {
            return schedules;
        }

        public void setSchedules(SchedulesBean schedules) {
            this.schedules = schedules;
        }

        public static class SchedulesBean {
            /**
             * elements : [{"id":6,"clazsId":1,"startTime":"2017-09-21 02:20:29","endTime":"2017-09-21 02:20:29","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"},{"id":7,"clazsId":1,"startTime":"2017-09-21 02:20:34","endTime":"2017-09-21 02:20:34","subject":"2121","remark":null,"status":0,"imageUrl":"http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599"}]
             * pageSize : 10
             * pageNum : null
             * offset : 0
             * totalElements : 2
             * lastPageNumber : null
             */

            private int pageSize;
            private String pageNum;
            private int offset;
            private int totalElements;
            private String lastPageNumber;
            private List<ElementsBean> elements;

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public String getPageNum() {
                return pageNum;
            }

            public void setPageNum(String pageNum) {
                this.pageNum = pageNum;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public int getTotalElements() {
                return totalElements;
            }

            public void setTotalElements(int totalElements) {
                this.totalElements = totalElements;
            }

            public String getLastPageNumber() {
                return lastPageNumber;
            }

            public void setLastPageNumber(String lastPageNumber) {
                this.lastPageNumber = lastPageNumber;
            }

            public List<ElementsBean> getElements() {
                return elements;
            }

            public void setElements(List<ElementsBean> elements) {
                this.elements = elements;
            }

            public static class ElementsBean {
                /**
                 * id : 6
                 * clazsId : 1
                 * startTime : 2017-09-21 02:20:29
                 * endTime : 2017-09-21 02:20:29
                 * subject : 2121
                 * remark : null
                 * status : 0
                 * imageUrl : http://upload.ugc.yanxiu.com/img/557147819851b009fe43b8e505f7b894.gif?from=22&amp;resId=30921599
                 */

                private int id;
                private int clazsId;
                private String startTime;
                private String endTime;
                private String subject;
                private Object remark;
                private int status;
                private String imageUrl;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getClazsId() {
                    return clazsId;
                }

                public void setClazsId(int clazsId) {
                    this.clazsId = clazsId;
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

                public String getSubject() {
                    return subject;
                }

                public void setSubject(String subject) {
                    this.subject = subject;
                }

                public Object getRemark() {
                    return remark;
                }

                public void setRemark(Object remark) {
                    this.remark = remark;
                }

                public int getStatus() {
                    return status;
                }

                public void setStatus(int status) {
                    this.status = status;
                }

                public String getImageUrl() {
                    return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                    this.imageUrl = imageUrl;
                }
            }
        }
    }
}
