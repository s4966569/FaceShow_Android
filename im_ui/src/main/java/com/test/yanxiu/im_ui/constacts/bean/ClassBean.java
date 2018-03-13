package com.test.yanxiu.im_ui.constacts.bean;

import com.test.yanxiu.im_ui.constacts.DatabaseFramework.annotation.DbTable;

/**
 * Created by frc on 2018/3/13.
 */
@DbTable("yxClass")
public class ClassBean {

    private Integer classId;

    private String className;

    public ClassBean(Integer classId, String className) {
        this.classId = classId;
        this.className = className;
    }

    public ClassBean() {
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
