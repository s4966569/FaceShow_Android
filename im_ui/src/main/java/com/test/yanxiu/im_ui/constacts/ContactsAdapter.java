package com.test.yanxiu.im_ui.constacts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frc on 2018/3/13.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {


    private OnItemClickListener onItemClickListener;

    private List<ContactsPlayerBean> data = new ArrayList<>();

    public void addItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refreah(List<ContactsPlayerBean> playerList) {
        this.data = playerList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void itemClick(View view, int position);
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder {

        public ContactsViewHolder(View itemView) {
            super(itemView);
        }
    }

}
