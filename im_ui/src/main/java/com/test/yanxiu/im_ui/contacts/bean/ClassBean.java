package com.test.yanxiu.im_ui.contacts.bean;

import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbPrimaryKey;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbTable;

/**
 * Created by frc on 2018/3/13.
 */
@DbTable("yxClass")
public class ClassBean {
    @DbPrimaryKey
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
