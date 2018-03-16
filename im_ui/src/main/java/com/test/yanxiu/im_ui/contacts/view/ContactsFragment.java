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
    private ImageView mImgBack;
    private ImageView mImgChangeClass;
    private SearchView mSearchView;
    private TextView mTvCurrentClassName, mTvSureChangeClass;
    private View mChangeClassBackView;
    private LinearLayout mLlChangeClass;
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
        mTvCurrentClassName = rootView.findViewById(R.id.tv_current_class_name);
        mLlChangeClass = rootView.findViewById(R.id.ll_change_class);
        mChangeClassBackView = rootView.findViewById(R.id.back_view);
        mTvSureChangeClass = rootView.findViewById(R.id.tv_sure_change_class);
        mSearchView = rootView.findViewById(R.id.searchView);
        modifySearchViewStyle(mSearchView);
        /*初始化通讯录列表*/
        mContactsAdapter = new ContactsAdapter();
        RecyclerView mContactsList = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContactsList.setLayoutManager(linearLayoutManager);
        mContactsList.setAdapter(mContactsAdapter);
        /*初始化切换班级列表*/
        mChangeClassAdapter = new ChangeClassAdapter();
        RecyclerView mChangeClassList = rootView.findViewById(R.id.recyclerView_change_class);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this.getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mChangeClassList.setLayoutManager(linearLayoutManager1);
        mChangeClassList.setAdapter(mChangeClassAdapter);

        addCallBack();
        mPresenter = new ContactsPresenter(this);
        mPresenter.getContactsList(null);

        return rootView;
    }

    /**
     * V7包原生的SearchView在UI上有限不符  所以做了些修改
     * 可参考: https://tanjundang.github.io/2016/11/17/SearchView/
     *
     * @param searchView searchView
     */
    private void modifySearchViewStyle(SearchView searchView) {
        SearchView.SearchAutoComplete tv = searchView.findViewById(R.id.search_src_text);
        tv.setTextColor(ContextCompat.getColor(this.getContext(), R.color.color_333333));
        tv.setHintTextColor(ContextCompat.getColor(this.getContext(), R.color.color_999999));
        tv.setTextSize(14);

        ImageView imageView = searchView.findViewById(R.id.search_mag_icon);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = 51;
        layoutParams.height = 51;
        imageView.setLayoutParams(layoutParams);
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
        mImgChangeClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtil.closeInputMethod(ContactsFragment.this.getContext(), v);
                if (mLlChangeClass.getVisibility() == View.GONE) {
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
            mLlChangeClass.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void hideChangeClassWindow() {
        mLlChangeClass.setVisibility(View.GONE);
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
