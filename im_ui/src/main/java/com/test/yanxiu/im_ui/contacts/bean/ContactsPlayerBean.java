package com.test.yanxiu.im_ui.contacts.bean;

import com.test.yanxiu.im_core.http.GetContactsResponse;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbPrimaryKey;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.annotation.DbTable;

/**
 * Created by frc on 2018/3/13.
 */
@DbTable("contacts")
public class ContactsPlayerBean {

    private Integer id;
    private Integer bizSource;
    private Integer memberType;
    @DbPrimaryKey
    private Integer userId;
    private String name;
    private String avatar;
    private Integer state;

    private Integer classId;
    private String className;

    public ContactsPlayerBean(GetContactsResponse.MemberInfoBean memberInfoBean, Integer classId,String className) {
        this.id = memberInfoBean.getId();
        this.bizSource = memberInfoBean.getBizSource();
        this.memberType = memberInfoBean.getMemberType();
        this.userId = memberInfoBean.getUserId();
        this.name = memberInfoBean.getMemberName();
        this.avatar = memberInfoBean.getAvatar();
        this.state = memberInfoBean.getState();
        this.classId = classId;
        this.className=className;
    }

    public ContactsPlayerBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBizSource() {
        return bizSource;
    }

    public void setBizSource(Integer bizSource) {
        this.bizSource = bizSource;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
