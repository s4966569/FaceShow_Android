package com.test.yanxiu.im_ui.contacts.bean;

import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbTable;

/**
 * Created by frc on 2018/3/13.
 */
@DbTable("contacts")
public class ContactsPlayerBean {

    private Integer id;
    private String name;
    private String phoneName;
    private String hardImgPath;

    private Integer classId;

    public ContactsPlayerBean(Integer id, String name, String phoneName, String hardImgPath, Integer classId) {
        this.id = id;
        this.name = name;
        this.phoneName = phoneName;
        this.hardImgPath = hardImgPath;
        this.classId = classId;
    }

    public ContactsPlayerBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getHardImgPath() {
        return hardImgPath;
    }

    public void setHardImgPath(String hardImgPath) {
        this.hardImgPath = hardImgPath;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }
}
