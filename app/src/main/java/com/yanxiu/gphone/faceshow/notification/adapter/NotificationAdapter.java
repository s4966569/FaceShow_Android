package com.yanxiu.gphone.faceshow.notification.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.http.notificaion.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知列表适配器
 * Created by frc on 17-9-15.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationResponse.Notification> mData = new ArrayList<>();
    private ItemClickListener itemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setData(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.img_red_circle.getVisibility() == View.VISIBLE)
                    holder.img_red_circle.setVisibility(View.INVISIBLE);
                if (itemClickListener != null)
                    itemClickListener.itemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<NotificationResponse.Notification> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification_name, tv_notification_created_time;
        ImageView img_red_circle,img_notification_head;

        ViewHolder(View itemView) {
            super(itemView);
            tv_notification_name = (TextView) itemView.findViewById(R.id.tv_notification_name);
            tv_notification_created_time = (TextView) itemView.findViewById(R.id.tv_notification_created_time);
            img_red_circle = (ImageView) itemView.findViewById(R.id.img_red_circle);
            img_notification_head = (ImageView) itemView.findViewById(R.id.img_notification_head);
        }

        public void setData(NotificationResponse.Notification notification) {
            tv_notification_name.setText(notification.getTitle());
            tv_notification_created_time.setText(notification.getUpdateTime());
            if (!notification.isViewed()) {//消息未读
                img_red_circle.setVisibility(View.VISIBLE);
                img_notification_head.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_notification_un_viewed));
            } else {
                img_red_circle.setVisibility(View.INVISIBLE);
                img_notification_head.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.ic_notification_viewed));
            }

        }

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void itemClick(int position);
    }
}
