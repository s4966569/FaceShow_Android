package com.test.yanxiu.im_ui.constacts.view;

import android.view.View;

import com.test.yanxiu.im_ui.constacts.ContactsAdapter;
import com.test.yanxiu.im_ui.constacts.bean.ClassBean;
import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;

import java.util.List;

/**
 * Created by frc on 2018/3/13.
 */

public interface IContactsView {

    void addCallBack();

//    void showChangeClassPopupWindow(List<ClassBean> list);

//    void changeClassContacts(ChangeClassPopupWindow.OnClassChangedListener onClassChangedListener);

//    void itemClick(ContactsAdapter.OnItemClickListener onItemClickListener);

    void showItemClickResult(View view, int position);

    void showQueryResultList(List<ContactsPlayerBean> data);

    void showContractsList(List<ContactsPlayerBean> data);

    void showChangeClassWindow(List<ClassBean> data);

    void hideChangeClassWindow();

}
