package com.test.yanxiu.im_ui.constacts.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_core.Constants;
import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.constacts.ContactsAdapter;
import com.test.yanxiu.im_ui.constacts.bean.ClassBean;
import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.constacts.presenter.ContactsPresenter;

import java.util.List;

/**
 * 显示通讯录的View
 *
 * @author by frc on 2018/3/13.
 */

public class ContactsFragment extends FaceShowBaseFragment implements IContactsView {
    ImageView mImgBack;
    ImageView mImgChangeClass;
    RecyclerView mContactsList;
    FSSearchView mSearchView;
    public ContactsAdapter mContactsAdapter;
    private ContactsPresenter mPresenter;
    private ChangeClassPopupWindow mPopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_layout, container, false);
        mImgBack = rootView.findViewById(R.id.img_back);
        mImgChangeClass = rootView.findViewById(R.id.img_change_class);
        mContactsList = rootView.findViewById(R.id.recyclerView);
        mContactsAdapter = new ContactsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsList.setLayoutManager(linearLayoutManager);
        mContactsList.setAdapter(mContactsAdapter);
        mPresenter = new ContactsPresenter(this);
        return rootView;
    }

    @Override
    public void addCallBack() {
        mContactsAdapter.addItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                showItemClickResult(view, position);
            }
        });
//        mSearchView.addQueryTextListener(new FSSearchView.OnQueryTextListener() {
//            @Override
//            public void queryTextChanged(String query) {
//                mPresenter.queryPlayer(query);
//            }
//        });
//        mPopupWindow.addClassChangListener(new ChangeClassPopupWindow.OnClassChangedListener() {
//            @Override
//            public void changed(ClassBean classData) {
//                mPresenter.changeContactsListByClass(classData);
//            }
//        });
        mImgChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.showChangeClassPopupWindow();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsFragment.this.getActivity().finish();
            }
        });
    }

    @Override
    public void showChangeClassPopupWindow(List<ClassBean> list) {
        //todo showPopupWindow
    }


    @Override
    public void showItemClickResult(View view, int position) {
        // TODO: 2018/3/13  item点击后的响应事件
    }

    @Override
    public void showQueryResultList(List<ContactsPlayerBean> data) {
        showContractsList(data);
    }


    @Override
    public void showContractsList(List<ContactsPlayerBean> data) {
        mContactsAdapter.refreah(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
