package com.test.yanxiu.im_ui.constacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.constacts.bean.ClassBean;
import com.test.yanxiu.im_ui.constacts.bean.ContactsPlayerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示当前班级成员的适配器
 *
 * @author frc on 2018/3/13.
 */

public class ChangeClassAdapter extends RecyclerView.Adapter<ChangeClassAdapter.ChangeClassViewHolder> {


    private OnItemClickListener onItemClickListener;

    private List<ClassBean> data = new ArrayList<>();

    public void addItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ChangeClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_chang_class_layout, parent, false);
        return new ChangeClassViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ChangeClassViewHolder holder, int position) {
        ClassBean bean = data.get(position);
        holder.tvClassName.setText(bean.getClassName());
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

    public void refresh(List<ClassBean> playerList) {
        this.data = playerList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void itemClick(View view, int position);
    }


    public class ChangeClassViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGo;
        TextView tvClassName;

        public ChangeClassViewHolder(View itemView) {
            super(itemView);
            imgGo = itemView.findViewById(R.id.img_go);
            tvClassName = itemView.findViewById(R.id.tv_class_name);

        }
    }

}
