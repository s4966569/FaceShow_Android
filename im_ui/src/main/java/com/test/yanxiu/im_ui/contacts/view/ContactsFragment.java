package com.test.yanxiu.im_ui.contacts.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.yanxiu.common_base.ui.InputMethodUtil;
import com.test.yanxiu.faceshow_ui_base.FaceShowBaseFragment;
import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.contacts.ChangeClassAdapter;
import com.test.yanxiu.im_ui.contacts.ContactsAdapter;
import com.test.yanxiu.im_ui.contacts.bean.ClassBean;
import com.test.yanxiu.im_ui.contacts.bean.ContactsPlayerBean;
import com.test.yanxiu.im_ui.contacts.presenter.ContactsPresenter;

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
    SearchView mSearchView;
    TextView mTvCurrentClassName, mTvSureChangeClass;
    View mChangeClassBackView;

    LinearLayout ll_change_class;
    public ContactsAdapter mContactsAdapter;
    private ChangeClassAdapter mChangeClassAdapter;
    private ContactsPresenter mPresenter;

    private int mCurrentItemClassSelected;


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
        mTvSureChangeClass = rootView.findViewById(R.id.tv_sure_change_class);
        mSearchView = rootView.findViewById(R.id.searchView);


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
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.queryPlayer(newText);
                return false;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mPresenter.getCurrentClassContactsList();
                return false;
            }
        });
        mImgChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtil.closeInputMethod(ContactsFragment.this.getContext(), v);
                if (ll_change_class.getVisibility() == View.GONE) {
                    openChangeClassWindow();
                } else {
                    closeChangeClassWindow();
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

                closeChangeClassWindow();
            }
        });

        mChangeClassAdapter.addItemClickListener(new ChangeClassAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                mCurrentItemClassSelected = position;
            }
        });
        mTvSureChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.changeClass(mCurrentItemClassSelected);
            }
        });
    }


    private void openChangeClassWindow() {
        mImgChangeClass.setImageDrawable(ContextCompat.getDrawable(ContactsFragment.this.getContext(), R.drawable.selector_close_change_class_window));
        mPresenter.showChangeClassPopupWindow();
    }

    private void closeChangeClassWindow() {
        mImgChangeClass.setImageDrawable(ContextCompat.getDrawable(ContactsFragment.this.getContext(), R.drawable.selector_open_change_class_window));
        hideChangeClassWindow();
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
        closeChangeClassWindow();
        mContactsAdapter.refresh(data);
    }

    @Override
    public void showChangeClassWindow(List<ClassBean> data, int currentClassPosition) {
        if (data != null && data.size() > 0) {
            mChangeClassAdapter.refresh(data, currentClassPosition);
            ll_change_class.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void hideChangeClassWindow() {
        ll_change_class.setVisibility(View.GONE);
    }

    @Override
    public void changeCurrentClassName(String className) {
        mTvCurrentClassName.setText(className);
    }


    @Override
    public void onStop() {
        super.onStop();
        hideChangeClassWindow();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
