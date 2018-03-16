package com.test.yanxiu.im_ui.constacts.model;

import android.text.TextUtils;

import com.test.yanxiu.im_ui.constacts.DatabaseFramework.db.BaseDaoFactory;
import com.test.yanxiu.im_ui.constacts.db.ClassDao;
import com.test.yanxiu.im_ui.constacts.bean.ClassBean;
import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.constacts.db.ContactsDao;

import java.util.List;

/**
 * Created by frc on 2018/3/13.
 */

public class ContactsModel {
    final String dbPath = "/data/data/com.yanxiu.gphone.faceshow/databases/yx.db";
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

    public int getmCurrentClassPosition() {
        return mCurrentClassPosition;
    }

    public void setmCurrentClassPosition(int mCurrentClassPosition) {
        this.mCurrentClassPosition = mCurrentClassPosition;
    }

    public String clearQueryKey() {
        return mCurrentQueryKey = "";
    }

}
