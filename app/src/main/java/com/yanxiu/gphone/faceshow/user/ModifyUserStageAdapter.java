package com.yanxiu.gphone.faceshow.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 2017/12/19.
 */

public class ModifyUserStageAdapter extends RecyclerView.Adapter<ModifyUserStageAdapter.StageViewHolder> {
    private List<StageSubjectModel.DataBean> data;
    private IRecyclerViewItemClick mIRecyclerViewItemClick;

    public ModifyUserStageAdapter(List<StageSubjectModel.DataBean> data, IRecyclerViewItemClick iRecyclerViewItemClick) {
        if (data == null) {
            data = new ArrayList<>();
        } else {
            this.data = data;
        }
        this.mIRecyclerViewItemClick = iRecyclerViewItemClick;
    }

    @Override
    public StageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modify_subject_layout, parent, false);
        return new StageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StageViewHolder holder, int position) {
        if (position==getItemCount()-1){
            holder.mLineDivider.setVisibility(View.GONE);
        }else {
            holder.mLineDivider.setVisibility(View.VISIBLE);
        }
        holder.mTvStageName.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRecyclerViewItemClick != null) {
                    mIRecyclerViewItemClick.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class StageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_stage_name)
        TextView mTvStageName;
        @BindView(R.id.sex_enter)
        ImageView mSexEnter;
        @BindView(R.id.line_divder)
        View mLineDivider;

        StageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
