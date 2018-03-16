package com.test.yanxiu.im_ui.contacts.model;

import android.text.TextUtils;

import com.test.yanxiu.im_ui.contacts.DatabaseFramework.db.BaseDaoFactory;
import com.test.yanxiu.im_ui.contacts.db.ClassDao;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.contacts.db.ContactsDao;

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

        return classDao.query(new ClassBean());
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

}
