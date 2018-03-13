package com.test.yanxiu.im_ui.constacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示当前班级成员的适配器
 *
 * @author frc on 2018/3/13.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {


    private OnItemClickListener onItemClickListener;

    private List<ContactsPlayerBean> data = new ArrayList<>();

    public void addItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts_adapter_layout, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ContactsViewHolder holder, int position) {
        ContactsPlayerBean bean = data.get(position);
        holder.tvPhoneNumber.setText(bean.getPhoneName());
        holder.tvName.setText(bean.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.itemClick(v, holder.getAdapterPosition());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void refresh(List<ContactsPlayerBean> playerList) {
        this.data = playerList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void itemClick(View view, int position);
    }


    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHard;
        TextView tvName, tvPhoneNumber;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            imgHard = itemView.findViewById(R.id.img_hard);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.tv_phone_number);

        }
    }

}
