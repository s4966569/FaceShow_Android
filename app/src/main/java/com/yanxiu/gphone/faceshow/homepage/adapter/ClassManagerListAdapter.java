package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.db.SpManager;
import com.yanxiu.gphone.faceshow.http.course.GetStudentClazsesResponse;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frc on 17-10-30.
 */

public class ClassManagerListAdapter extends RecyclerView.Adapter {

    private ArrayList<View> views = new ArrayList<>();

    private List<GetStudentClazsesResponse.ClazsInfosBean> data = new ArrayList<>();
    private IRecyclerViewItemClick mIRecyclerViewItemClick;

    private int mSelectedPosition = -1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manager_class_adapter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.classmanger_item_title.setText(data.get(position).getClazsName());
        String start = data.get(position).getStartTime().split(" ")[0];
        String end = data.get(position).getEndTime().split(" ")[0];
        viewHolder.classmanger_item_time.setText(start + " 至 " + end);
        viewHolder.classmanger_item_content.setText(data.get(position).getProjectName());
        if (!views.contains(holder.itemView)) {
            views.add(holder.itemView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < views.size(); i++) {
                    if (views.get(i) == view) {
                        views.get(i).setSelected(true);
                    } else {
                        views.get(i).setSelected(false);
                    }
                }
                holder.itemView.setSelected(true);
                if (mIRecyclerViewItemClick != null) {
                    mIRecyclerViewItemClick.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

        if (String.valueOf(data.get(position).getId()).equals( SpManager.getUserInfo().getClassId())) {
            holder.itemView.setSelected(true);
        }else {
            holder.itemView.setSelected(false);
        }
    }

    public void setIRecyclerViewItemClick(IRecyclerViewItemClick iRecyclerViewItemClick) {
        this.mIRecyclerViewItemClick = iRecyclerViewItemClick;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void update(List<GetStudentClazsesResponse.ClazsInfosBean> clazsInfos, int selcetPosition) {
        this.data = clazsInfos;
        this.mSelectedPosition = selcetPosition;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView classmanger_item_title, classmanger_item_time, classmanger_item_content;

        ViewHolder(View itemView) {
            super(itemView);
            classmanger_item_title = (TextView) itemView.findViewById(R.id.classmanger_item_title);
            classmanger_item_time = (TextView) itemView.findViewById(R.id.classmanger_item_time);
            classmanger_item_content = (TextView) itemView.findViewById(R.id.classmanger_item_content);
        }

    }

}
