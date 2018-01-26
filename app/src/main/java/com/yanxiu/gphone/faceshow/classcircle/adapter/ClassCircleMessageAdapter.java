package com.yanxiu.gphone.faceshow.classcircle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.classcircle.request.ClassCircleNewMessageResponse;
import com.yanxiu.gphone.faceshow.util.DateFormatUtil;
import com.yanxiu.gphone.faceshow.util.recyclerView.IRecyclerViewItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 2018/1/18.
 */

public class ClassCircleMessageAdapter extends RecyclerView.Adapter<ClassCircleMessageAdapter.ViewHolder> {


    private IRecyclerViewItemClick mIRecyclerViewItemClick;
    private List<ClassCircleNewMessageResponse.DataBean.MsgsBean> mMsgsBeanList;

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

        holder.mTvPublisherName.setText(mMsgsBeanList.get(position).getUserName());
        holder.mTvMessageContent.setText(mMsgsBeanList.get(position).getComment());
        holder.mTvPublishTime.setText(mMsgsBeanList.get(position).getCreateTime());
        Glide.with(holder.itemView.getContext()).load(mMsgsBeanList.get(position).getMomentSimple().getImage()).into(holder.mImgPic);
    }

    @Override
    public int getItemCount() {
        return mMsgsBeanList != null ? mMsgsBeanList.size() : 0;
    }

    public void update(List<ClassCircleNewMessageResponse.DataBean.MsgsBean> msgs) {
        mMsgsBeanList = msgs;
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
