package com.test.yanxiu.im_ui.constacts.presenter;

import com.test.yanxiu.im_ui.constacts.bean.ClassBean;
import com.test.yanxiu.im_ui.constacts.model.ContactsModel;
import com.test.yanxiu.im_ui.constacts.view.FSSearchView;
import com.test.yanxiu.im_ui.constacts.view.IContactsView;


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

    void getContactsList(ClassBean classBean) {
        view.showContractsList(model.getPlayersDataByClass(classBean));
    }

    public void queryPlayer(String key) {
        view.showQueryResultList(model.getQueryResult(""));
    }


    public void showChangeClassPopupWindow() {
        view.showChangeClassPopupWindow(model.getClassListData());
    }


}
