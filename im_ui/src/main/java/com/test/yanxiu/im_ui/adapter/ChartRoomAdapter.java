package com.test.yanxiu.im_ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.yanxiu.im_ui.R;
import com.test.yanxiu.im_ui.bean.ImMessageBean;

import java.util.ArrayList;

import static com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_LOADING;
import static com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_MYSELF;
import static com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_OTHER_PEOPLE;
import static com.test.yanxiu.im_ui.ImConstants.MESSAGE_TYPE_TIME;


/**
 * Created by 戴延枫
 * 聊天室的adapter
 */

public class ChartRoomAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;

    private ArrayList<ImMessageBean.MessageBean> mList;

    public ChartRoomAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<ImMessageBean.MessageBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        ImMessageBean.MessageBean bean = mList.get(position);
//        switch (bean.getType()) {
//            case MESSAGE_TYPE_MYSELF:
//                return MESSAGE_TYPE_MYSELF;
//            case MESSAGE_TYPE_OTHER_PEOPLE:
//                return MESSAGE_TYPE_OTHER_PEOPLE;
//            case MESSAGE_TYPE_TIME:
//                return MESSAGE_TYPE_TIME;
//        }
        return bean.getType();
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case MESSAGE_TYPE_MYSELF:
                View myselfView = inflater.inflate(R.layout.message_myself_item, parent, false);
                viewHolder = new MyselfViewHolder(myselfView);
                break;
            case MESSAGE_TYPE_OTHER_PEOPLE:
                View otherPeopleView = inflater.inflate(R.layout.message_otherpeople_item, parent, false);
                viewHolder = new OtherPeopleViewHolder(otherPeopleView);
                break;
            case MESSAGE_TYPE_TIME:
                View timeView = inflater.inflate(R.layout.message_time_item, parent, false);
                viewHolder = new TimeViewHolder(timeView);
                break;
            case MESSAGE_TYPE_LOADING:
                View loadingView = inflater.inflate(R.layout.message_loading_item, parent, false);
                viewHolder = new LoadingViewHolder(loadingView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImMessageBean.MessageBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case MESSAGE_TYPE_MYSELF:
                MyselfViewHolder myselfViewHolder = (MyselfViewHolder) holder;
                myselfViewHolder.setData(position, data);
                break;
            case MESSAGE_TYPE_OTHER_PEOPLE:
                OtherPeopleViewHolder otherPeopleViewHolder = (OtherPeopleViewHolder) holder;
                otherPeopleViewHolder.setData(position, data);
                break;
            case MESSAGE_TYPE_TIME:
                TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
                timeViewHolder.setData(position, data);
                break;
            case MESSAGE_TYPE_LOADING:
                LoadingViewHolder LoadingViewHolder = (LoadingViewHolder) holder;
//                LoadingViewHolder.setData(position, data);
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 我的消息
     */
    private class MyselfViewHolder extends RecyclerView.ViewHolder {
        private TextView myselfMsgContent;
        private ImageView myselfUserAvatar;

        public MyselfViewHolder(View itemView) {
            super(itemView);
            myselfMsgContent = itemView.findViewById(R.id.myselfMsgContent);
            myselfUserAvatar = itemView.findViewById(R.id.myselfUserAvatar);
        }

        public void setData(final int position, ImMessageBean.MessageBean data) {
            myselfMsgContent.setText(data.getMsgContent());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecyclerViewItemClickListener != null) {
                        mRecyclerViewItemClickListener.onItemClick(position);
                    }
                }
            });

        }

    }

    /**
     * 其他人的消息
     */
    private class OtherPeopleViewHolder extends RecyclerView.ViewHolder {
        private TextView otherPeopleMsgContent;
        private ImageView otherPeopleUserAvatar;

        public OtherPeopleViewHolder(View itemView) {
            super(itemView);
            otherPeopleMsgContent = itemView.findViewById(R.id.otherPeopleMsgContent);
            otherPeopleUserAvatar = itemView.findViewById(R.id.otherPeopleUserAvatar);
        }

        public void setData(final int position, ImMessageBean.MessageBean data) {
            otherPeopleMsgContent.setText(data.getMsgContent());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRecyclerViewItemClickListener != null) {
                        mRecyclerViewItemClickListener.onItemClick(position);
                    }
                }
            });

        }

    }

    /**
     * 时间
     */
    private class TimeViewHolder extends RecyclerView.ViewHolder {
        private TextView timeMsgTextView;

        public TimeViewHolder(View itemView) {
            super(itemView);
            timeMsgTextView = itemView.findViewById(R.id.timeMsgTextView);
        }

        public void setData(final int position, ImMessageBean.MessageBean data) {
            timeMsgTextView.setText(data.getMsgTime());
        }

    }

    /**
     * 加载更多loadingview
     */
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private TextView timeMsgTextView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            timeMsgTextView = itemView.findViewById(R.id.timeMsgTextView);
        }

        public void setData(final int position, ImMessageBean.MessageBean data) {
//            timeMsgTextView.setText(data.getMsgTime());
        }

    }

    /**
     * 获取更多数据
     */
    public void loadMoreData(RecyclerView recyclerView) {
        ImMessageBean.MessageBean bean = new ImMessageBean.MessageBean();
        bean.setType(MESSAGE_TYPE_LOADING);
        mList.add(0, bean);
        notifyDataSetChanged();
        recyclerView.scrollToPosition(0);//滚动到顶部
    }

    /**
     * 结束获取更多数据
     */
    public void finishLoadMoreData() {
        mList.remove(0);
        notifyDataSetChanged();
    }

}


