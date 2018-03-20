package com.test.yanxiu.im_ui.contacts.model;

import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.test.yanxiu.im_core.http.GetContactsResponse;
import com.test.yanxiu.im_ui.contacts.DatabaseFramework.db.BaseDaoFactory;
import com.test.yanxiu.im_ui.contacts.db.ClassDao;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.contacts.db.ContactsDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录对应的数据处理类
 *
 * @author frc on 2018/3/13.
 */

public class ContactsModel {
    private final String dbPath = "/data/data/com.yanxiu.gphone.faceshow/databases/yx.db";
    private ClassDao classDao;
    private ContactsDao contactsDao;
    private ClassBean mCurrentClass;
    private int mCurrentClassPosition;
    private String mCurrentQueryKey = "";

    private GetContactsResponse mData = null;


    public GetContactsResponse getData() {
        return mData;
    }


    public void setData(GetContactsResponse mData) {

        createTheDataWeNeed(mData);
    }

    private void createTheDataWeNeed(GetContactsResponse sData) {

        this.mData = sData;
        if (classDao == null) {
            classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
        }

        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        //存储班级信息
        for (GetContactsResponse.GroupsBean groupsBean : mData.getData().getContacts().getGroups()) {
            ClassBean classBean = new ClassBean(groupsBean.getGroupId(), groupsBean.getGroupName());
            classDao.insert(classBean);
            //存储班级下成员信息
            for (GetContactsResponse.ContactsBean contactsBean : groupsBean.getContacts()) {
                ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean(contactsBean.getMemberInfo(), groupsBean.getGroupId());
                contactsDao.insert(contactsPlayerBean);
            }
        }

    }

    public List<ContactsPlayerBean> getPlayersDataByClass(ClassBean classData) {

        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean();
        contactsPlayerBean.setClassId(classData.getClassId());
        mCurrentClass = classData;
        if (TextUtils.isEmpty(mCurrentQueryKey)) {
            return contactsDao.query(contactsPlayerBean);
        } else {
            contactsPlayerBean.setName(mCurrentQueryKey);
            return contactsDao.fuzzyQuery(contactsPlayerBean);
        }
    }


    public List<ClassBean> getClassListData() {
        if (classDao == null) {
            classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
        }

        List<ClassBean> classBeanList = classDao.query(new ClassBean());
        if (null == classBeanList || classBeanList.size() < 1) {
            return classDao.query(new ClassBean());
        } else {
            return classBeanList;
        }


    }

    public List<ContactsPlayerBean> getQueryResult(String queryContent) {
        mCurrentQueryKey = queryContent;
        if (contactsDao == null) {
            contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
        }
        ContactsPlayerBean contactsPlayerBean = new ContactsPlayerBean();
        contactsPlayerBean.setClassId(mCurrentClass.getClassId());
        contactsPlayerBean.setName(queryContent);
        return contactsDao.fuzzyQuery(contactsPlayerBean);
    }

    public List<ContactsPlayerBean> getCurrentClassPlayerList() {
        if (mCurrentClass == null) {
            return null;
        }
        return getPlayersDataByClass(mCurrentClass);
    }

    public int getCurrentClassPosition() {
        return mCurrentClassPosition;
    }

    public void setCurrentClassPosition(int mCurrentClassPosition) {
        this.mCurrentClassPosition = mCurrentClassPosition;
    }

    public void clearQueryKey() {
        mCurrentQueryKey = "";
    }


//    private void createMockData() {
//
//
//        ClassDao classDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ClassDao.class, ClassBean.class);
//        classDao.insert(new ClassBean(1, "面授一班"));
//        classDao.insert(new ClassBean(2, "面授二班"));
//        classDao.insert(new ClassBean(3, "面授三班"));
//        classDao.insert(new ClassBean(4, "面授四班"));
//
//
//        ContactsDao contactsDao = BaseDaoFactory.getOutInstance(dbPath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
//        contactsDao.insert(new ContactsPlayerBean(1, "张一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(2, "张二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(3, "张三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(4, "张四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(5, "李一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(6, "李二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(7, "李三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(8, "李四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(9, "赵一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(10, "赵二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(11, "赵三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(12, "赵四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(13, "王一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(14, "王二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(15, "王三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//        contactsDao.insert(new ContactsPlayerBean(16, "王四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 1));
//
//
//        contactsDao.insert(new ContactsPlayerBean(17, "张一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(18, "张二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(19, "张三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(20, "张四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(21, "李四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(22, "赵二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(23, "赵三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(24, "赵四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(25, "王一", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(26, "王二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(27, "王三", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//        contactsDao.insert(new ContactsPlayerBean(28, "王四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 2));
//
//
//        contactsDao.insert(new ContactsPlayerBean(29, "张四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 3));
//        contactsDao.insert(new ContactsPlayerBean(30, "李四", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 3));
//        contactsDao.insert(new ContactsPlayerBean(31, "赵二", "13300022223000", "https://avatars3.githubusercontent.com/u/18319916?s=400&v=4", 3));
//
//    }

}
