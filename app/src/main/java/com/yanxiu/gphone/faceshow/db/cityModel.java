package com.yanxiu.gphone.faceshow.db;

import java.util.List;

/**
 * @author frc on 2018/1/21.
 */

public class cityModel {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private String name;
        private int parentId;
        private List<DataBean> nextLevelData;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public List<DataBean> getNextLevelData() {
            return nextLevelData;
        }

        public void setNextLevelData(List<DataBean> nextLevelData) {
            this.nextLevelData = nextLevelData;
        }
    }
}
