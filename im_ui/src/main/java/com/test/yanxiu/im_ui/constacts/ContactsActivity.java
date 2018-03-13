package com.test.yanxiu.im_ui.constacts;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.constacts.view.ContactsFragment;

public class ContactsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constacts);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        ContactsFragment contactsFragment = new ContactsFragment();
        transaction.add(R.id.fragment_content,contactsFragment);
        transaction.commit();



//        String sqlDatabasePath = "/data/data/" + this.getPackageName() + "/databases/yx.db";
//        Log.e("frc", "path::: " + sqlDatabasePath);


//        ContactsDao contactsDao = BaseDaoFactory.getOutInstance(sqlDatabasePath).getBaseDao(ContactsDao.class, ContactsPlayerBean.class);
//        contactsDao.insert(new ContactsPlayerBean(1,"张一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(2,"张二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(3,"张三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(4,"张四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(5,"李一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(6,"李二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(7,"李三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(8,"李四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(9,"赵一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(10,"赵二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(11,"赵三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(12,"赵四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(13,"王一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(14,"王二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(15,"王三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//        contactsDao.insert(new ContactsPlayerBean(16,"王四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",1));
//
//
//        contactsDao.insert(new ContactsPlayerBean(17,"张一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(18,"张二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(19,"张三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(20,"张四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(21,"李四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(22,"赵二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(23,"赵三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(24,"赵四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(25,"王一","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(26,"王二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(27,"王三","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//        contactsDao.insert(new ContactsPlayerBean(28,"王四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",2));
//
//
//        contactsDao.insert(new ContactsPlayerBean(29,"张四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",3));
//        contactsDao.insert(new ContactsPlayerBean(30,"李四","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",3));
//        contactsDao.insert(new ContactsPlayerBean(31,"赵二","13300022223000","https://avatars3.githubusercontent.com/u/18319916?s=400&v=4",3));


//        ContactsModel contactsModel = new ContactsModel();
//
//        for (ContactsPlayerBean contactsPlayerBean : contactsModel.getQueryResult("张三")) {
//            Log.e("frc", "queryResult::  " + contactsPlayerBean.getName() + "   id: " + contactsPlayerBean.getId());
//        }
//        for (ClassBean classBean : contactsModel.getClassListData()) {
//            Log.e("frc", "className:" + classBean.getClassName());
//            for (ContactsPlayerBean contactsPlayerBean : contactsModel.getPlayersDataByClass(classBean)) {
//                Log.e("frc", "personName:  " + contactsPlayerBean.getName());
//            }
//            Log.e("frc", "---------------------------------------");
//        }


//        FrameLayout fragmentContent =findViewById(R.id.fragment_content);
//        ContactsFragment contactsFragment = new ContactsFragment();
    }
}
