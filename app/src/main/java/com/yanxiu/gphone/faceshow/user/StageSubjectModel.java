package com.yanxiu.gphone.faceshow.user;

import java.io.Serializable;
import java.util.List;

/**
 * @author  by frc on 2017/12/18.
 */

public class StageSubjectModel implements Serializable{

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean implements Serializable{
        private String id;
        private String name;
        private List<DataBean> sub;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DataBean> getSub() {
            return sub;
        }

        public void setSub(List<DataBean> sub) {
            this.sub = sub;
        }

    }

}
