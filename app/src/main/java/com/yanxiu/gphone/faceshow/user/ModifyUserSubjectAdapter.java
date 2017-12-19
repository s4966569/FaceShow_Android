package com.yanxiu.gphone.faceshow.user;

import android.support.v4.content.ContextCompat;
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
 * @author  by frc on 2017/12/19.
 */

public class ModifyUserSubjectAdapter extends RecyclerView.Adapter<ModifyUserSubjectAdapter.SubjectViewHolder> {
        private List<StageSubjectModel.DataBean> mData;
        private IRecyclerViewItemClick mIRecyclerViewItemClick;
        private int mSelectedPosition =-1;


    public ModifyUserSubjectAdapter(List<StageSubjectModel.DataBean> data, IRecyclerViewItemClick IRecyclerViewItemClick) {
        if (data!=null){
            mData = data;
        }else {
            data = new ArrayList<>();
        }
        mIRecyclerViewItemClick = IRecyclerViewItemClick;
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_modify_subject_layout,parent,false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SubjectViewHolder holder, int position) {
        holder.mTvSubjectName.setText(mData.get(position).getName());
        if (position==getItemCount()-1){
            holder.mLineDivider.setVisibility(View.GONE);
        }else {
            holder.mLineDivider.setVisibility(View.VISIBLE);
        }
        if (position==mSelectedPosition){
            holder.mSexEnter.setVisibility(View.VISIBLE);
            holder.mTvSubjectName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_1da1f2));
        }else {
            holder.mSexEnter.setVisibility(View.INVISIBLE);
            holder.mTvSubjectName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(),R.color.color_333333));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRecyclerViewItemClick!=null){
                    mSelectedPosition =holder.getAdapterPosition();
                    mIRecyclerViewItemClick.onItemClick(view,holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_subject_name)
        TextView mTvSubjectName;
        @BindView(R.id.sex_enter)
        ImageView mSexEnter;
        @BindView(R.id.line_divder)
        View mLineDivider;

        public SubjectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
