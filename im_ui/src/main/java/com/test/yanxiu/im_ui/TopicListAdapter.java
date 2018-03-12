package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.yanxiu.im_core.db.DbTopic;

import java.util.List;

/**
 * Created by cailei on 09/03/2018.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {
    private Context mContext;
    private List<DbTopic> mDatas;

    TopicListAdapter(Context context, List<DbTopic> topics) {
        mContext = context;
        mDatas = topics;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {
        DbTopic topic = mDatas.get(position);
        holder.setData(topic);
    }

    @Override
    public int getItemCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {
        private TextView mSenderTextView;
        private TextView mTimeTextView;
        private TextView mMsgTextView;

        public TopicViewHolder(View itemView) {
            super(itemView);
            mSenderTextView = itemView.findViewById(R.id.sender_textview);
            mTimeTextView = itemView.findViewById(R.id.time_textView);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
        }

        public void setData(DbTopic topic) {
            mSenderTextView.setText("asdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdg");
            mTimeTextView.setText("1天前");
            mMsgTextView.setText("asdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasdgasd");
        }
    }
}
