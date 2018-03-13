package com.test.yanxiu.im_ui.constacts.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.constacts.ChangeClassAdapter;
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
    RecyclerView mContactsList, mChangeClassList;
    FSSearchView mSearchView;
    TextView mTvCurrentClassName;
    View mChangeClassBackView;

    LinearLayout ll_change_class;
    public ContactsAdapter mContactsAdapter;
    private ChangeClassAdapter mChangeClassAdapter;
    private ContactsPresenter mPresenter;
    private ChangeClassPopupWindow mPopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_layout, container, false);
        mImgBack = rootView.findViewById(R.id.img_back);
        mImgChangeClass = rootView.findViewById(R.id.img_change_class);
        mContactsList = rootView.findViewById(R.id.recyclerView);
        mTvCurrentClassName = rootView.findViewById(R.id.tv_current_class_name);
        ll_change_class = rootView.findViewById(R.id.ll_change_class);
        mChangeClassList = rootView.findViewById(R.id.recyclerView_change_class);
        mChangeClassBackView = rootView.findViewById(R.id.back_view);

        mContactsAdapter = new ContactsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsList.setLayoutManager(linearLayoutManager);
        mContactsList.setAdapter(mContactsAdapter);

        mChangeClassAdapter = new ChangeClassAdapter();
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mChangeClassList.setLayoutManager(linearLayoutManager1);
        mChangeClassList.setAdapter(mChangeClassAdapter);


        mPresenter = new ContactsPresenter(this);
        addCallBack();
        mTvCurrentClassName.setText("面授一班");
        mPresenter.getContactsList(new ClassBean(1, "面授一班"));

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
                if (ll_change_class.getVisibility() == View.GONE) {
                    mPresenter.showChangeClassPopupWindow();
                } else {
                    hideChangeClassWindow();
                }
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsFragment.this.getActivity().finish();
            }
        });
        mChangeClassBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideChangeClassWindow();
            }
        });
    }


    @Override
    public void showItemClickResult(View view, int position) {
        // TODO: 2018/3/13  item点击后的响应事件
        Toast.makeText(this.getContext(), "position:: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showQueryResultList(List<ContactsPlayerBean> data) {
        showContractsList(data);
    }


    @Override
    public void showContractsList(List<ContactsPlayerBean> data) {
        mContactsAdapter.refresh(data);
    }

    @Override
    public void showChangeClassWindow(List<ClassBean> data) {

        if (data == null || data.size() == 0) {

        } else {
            mChangeClassAdapter.refresh(data);
            ll_change_class.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideChangeClassWindow() {
        ll_change_class.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
