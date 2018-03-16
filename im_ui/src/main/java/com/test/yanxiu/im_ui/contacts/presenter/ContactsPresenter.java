package com.test.yanxiu.im_ui.contacts.presenter;

import android.text.TextUtils;

import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.model.ContactsModel;
import com.test.yanxiu.im_ui.contacts.view.IContactsView;

import java.util.List;


/**
 * 针对通讯录的presenter层
 *
 * @author by frc on 2018/3/13.
 */

public class ContactsPresenter {

    private IContactsView view;
    private ContactsModel model = new ContactsModel();

    public ContactsPresenter(IContactsView view) {
        this.view = view;

    }

    /**
     * 根据班级获取对应的班级通讯录
     *
     * @param classBean 班级对象
     */
    public void getContactsList(ClassBean classBean) {
        if (null == classBean) {
            //初次加载数据  默认显示第一个班级的通讯录信息
            List<ClassBean> classBeans = model.getClassListData();
            ClassBean firstClass = classBeans.get(0);
            view.changeCurrentClassName(firstClass.getClassName());
            view.showContractsList(model.getPlayersDataByClass(firstClass));
        } else {
            view.showContractsList(model.getPlayersDataByClass(classBean));
        }
    }

    /**
     * 根据关键字进行模糊搜索
     *
     * @param key 关键字
     */
    public void queryPlayer(String key) {
        if (TextUtils.isEmpty(key)) {
            model.clearQueryKey();
            view.showQueryResultList(model.getCurrentClassPlayerList());
        } else {
            view.showQueryResultList(model.getQueryResult(key));
        }
    }

    /**
     * 显示切换班级列表
     */
    public void showChangeClassPopupWindow() {
        view.showChangeClassWindow(model.getClassListData(), model.getCurrentClassPosition());
    }

    /**
     * 切换班级
     *
     * @param position 班级index
     */
    public void changeClass(int position) {
        model.setCurrentClassPosition(position);
        ClassBean currentClass = model.getClassListData().get(position);
        view.changeCurrentClassName(currentClass.getClassName());
        view.showContractsList(model.getPlayersDataByClass(currentClass));
    }


}
