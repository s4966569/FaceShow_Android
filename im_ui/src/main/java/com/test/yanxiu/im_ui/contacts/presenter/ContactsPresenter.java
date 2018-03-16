package com.test.yanxiu.im_ui.contacts.presenter;

import android.text.TextUtils;

import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.model.ContactsModel;
import com.test.yanxiu.im_ui.contacts.view.IContactsView;


/**
 * Created by frc on 2018/3/13.
 */

public class ContactsPresenter {

    private IContactsView view;
    private ContactsModel model = new ContactsModel();

    public ContactsPresenter(IContactsView view) {
        this.view = view;

    }

    public void changeContactsListByClass(ClassBean classData) {
        view.showContractsList(model.getPlayersDataByClass(classData));

    }

    public void getContactsList(ClassBean classBean) {
        view.showContractsList(model.getPlayersDataByClass(classBean));
    }

    public void queryPlayer(String key) {
        if (TextUtils.isEmpty(key)) {
            model.clearQueryKey();
            view.showQueryResultList(model.getCurrentClassPlayerList());
        } else {
            view.showQueryResultList(model.getQueryResult(key));
        }
    }

    public void getCurrentClassContactsList() {
        view.showContractsList(model.getCurrentClassPlayerList());
    }

    public void showChangeClassPopupWindow() {
        view.showChangeClassWindow(model.getClassListData(), model.getmCurrentClassPosition());
    }

    public void changeClass(int position) {
        model.setmCurrentClassPosition(position);
        ClassBean currentClass = model.getClassListData().get(position);
        view.changeCurrentClassName(currentClass.getClassName());
        view.showContractsList(model.getPlayersDataByClass(currentClass));
    }


}