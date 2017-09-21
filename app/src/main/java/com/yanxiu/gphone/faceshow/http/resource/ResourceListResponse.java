package com.yanxiu.gphone.faceshow.http.resource;

import com.yanxiu.gphone.faceshow.base.BaseBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

import java.util.List;

/**
 * Created by lufengqing on 2017/9/20.
 */

public class ResourceListResponse extends FaceShowBaseResponse {

    /**
     * data : {"elements":[{"id":1,"resName":"testname","type":"0","pulisherId":0,"publisherName":"多隆","viewNum":0,"downNum":0,"state":1,"createTime":"2017-09-18 19:22:57","resId":73,"suffix":"pdf","url":null,"clazzId":1,"createTimeStr":"2017-09-18"}],"pageSize":10,"pageNum":null,"offset":0,"totalElements":1,"lastPageNumber":null}
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
         * elements : [{"id":1,"resName":"testname","type":"0","pulisherId":0,"publisherName":"多隆","viewNum":0,"downNum":0,"state":1,"createTime":"2017-09-18 19:22:57","resId":73,"suffix":"pdf","url":null,"clazzId":1,"createTimeStr":"2017-09-18"}]
         * pageSize : 10
         * pageNum : null
         * offset : 0
         * totalElements : 1
         * lastPageNumber : null
         */

        private int pageSize;
        private Object pageNum;
        private int offset;
        private int totalElements;
        private Object lastPageNumber;
        private List<ElementsBean> elements;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public Object getPageNum() {
            return pageNum;
        }

        public void setPageNum(Object pageNum) {
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

        public Object getLastPageNumber() {
            return lastPageNumber;
        }

        public void setLastPageNumber(Object lastPageNumber) {
            this.lastPageNumber = lastPageNumber;
        }

        public List<ElementsBean> getElements() {
            return elements;
        }

        public void setElements(List<ElementsBean> elements) {
            this.elements = elements;
        }

        public static class ElementsBean extends BaseBean{
            /**
             * id : 1
             * resName : testname
             * type : 0
             * pulisherId : 0
             * publisherName : 多隆
             * viewNum : 0
             * downNum : 0
             * state : 1
             * createTime : 2017-09-18 19:22:57
             * resId : 73
             * suffix : pdf
             * url : null
             * clazzId : 1
             * createTimeStr : 2017-09-18
             */

            private int id;
            private String resName;
            private String type;
            private int pulisherId;
            private String publisherName;
            private int viewNum;
            private int downNum;
            private int state;
            private String createTime;
            private String resId;
            private String suffix;
            private Object url;
            private int clazzId;
            private String createTimeStr;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getResName() {
                return resName;
            }

            public void setResName(String resName) {
                this.resName = resName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getPulisherId() {
                return pulisherId;
            }

            public void setPulisherId(int pulisherId) {
                this.pulisherId = pulisherId;
            }

            public String getPublisherName() {
                return publisherName;
            }

            public void setPublisherName(String publisherName) {
                this.publisherName = publisherName;
            }

            public int getViewNum() {
                return viewNum;
            }

            public void setViewNum(int viewNum) {
                this.viewNum = viewNum;
            }

            public int getDownNum() {
                return downNum;
            }

            public void setDownNum(int downNum) {
                this.downNum = downNum;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getResId() {
                return resId;
            }

            public void setResId(String resId) {
                this.resId = resId;
            }

            public String getSuffix() {
                return suffix;
            }

            public void setSuffix(String suffix) {
                this.suffix = suffix;
            }

            public Object getUrl() {
                return url;
            }

            public void setUrl(Object url) {
                this.url = url;
            }

            public int getClazzId() {
                return clazzId;
            }

            public void setClazzId(int clazzId) {
                this.clazzId = clazzId;
            }

            public String getCreateTimeStr() {
                return createTimeStr;
            }

            public void setCreateTimeStr(String createTimeStr) {
                this.createTimeStr = createTimeStr;
            }
        }
    }
}
