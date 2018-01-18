package com.yanxiu.gphone.faceshow.classcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleMessageAdapter extends RecyclerView.Adapter<ClassCircleMessageAdapter.ViewHolder> {


    IRecyclerViewItemClick mIRecyclerViewItemClick;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classcircle_message_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRecyclerViewItemClick != null) {
                    mIRecyclerViewItemClick.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

        holder.mTvPublisherName.setText("葛长龙");
        holder.mTvMessageContent.setText("咦  咋打成了葛长龙吗  不应该是孙长龙吗?");
        holder.mTvPublishTime.setText("10亿年前");
        // TODO: 2018/1/18 需要占位图
        Glide.with(holder.itemView.getContext()).load("").into(holder.mImgPic);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void update() {
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_pic)
        ImageView mImgPic;
        @BindView(R.id.tv_publisher_name)
        TextView mTvPublisherName;
        @BindView(R.id.tv_message_content)
        TextView mTvMessageContent;
        @BindView(R.id.tv_publish_time)
        TextView mTvPublishTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setIRecyclerViewItemClick(IRecyclerViewItemClick iRecyclerViewItemClick) {
        mIRecyclerViewItemClick = iRecyclerViewItemClick;
    }
}
