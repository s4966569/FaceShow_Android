package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_ui.view.CircleView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        return new TopicViewHolder(mContext, v);
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
        private Context mContext;

        private View mContainer;
        private ImageView mAvatarImageView;
        private TextView mSenderTextView;
        private TextView mTimeTextView;
        private TextView mMsgTextView;
        private CircleView mRedDotCircleView;

        public TopicViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;

            mContainer = itemView;
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mSenderTextView = itemView.findViewById(R.id.sender_textview);
            mTimeTextView = itemView.findViewById(R.id.time_textView);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mRedDotCircleView = itemView.findViewById(R.id.reddot_circleview);
        }

        public void setData(final DbTopic topic) {

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SrtLogger.log("im ui", "topic : %ll", topic.getTopicId());
                }
            });

            // 默认
            mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
            mSenderTextView.setText("未知");
            mTimeTextView.setText("未知");
            mMsgTextView.setText("未知");

            long imId = 9;  // TBD:cailei 需要替换

            List<DbMember> members = topic.getMembers();
            if ((members == null) || (members.size() == 0)) {
                // 尚且没有member信息，全部用默认
                return;
            }


            DbMsg latestMsg = null;
            if (topic.mergedMsgs.size() > 0) {
                latestMsg = topic.mergedMsgs.get(0);
            }

            if (topic.getType().equals("1")) { // 私聊
                for (DbMember member : topic.getMembers()) {
                    if (member.getImId() != imId) {
                        // 1, 显示对方头像
                        Glide.with(mContext)
                                .load(member.getAvatar())
                                .into(mAvatarImageView);
                        // 2, 显示对方昵称
                        mSenderTextView.setText(member.getName());
                        break;
                    }
                }

                if (latestMsg != null) {
                    // 3, 显示消息时间
                    mTimeTextView.setText(timeStr(latestMsg.getSendTime()));
                    // 4, 显示消息内容
                    mMsgTextView.setText(latestMsg.getMsg());
                }
            }

            if (topic.getType().equals("2")) { // 群聊
                // 1, 显示班级默认图片
                mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
                if (latestMsg != null) {
                    // 2, 显示最后消息者的昵称
                    for (DbMember member : topic.getMembers()) {
                        if (member.getImId() == latestMsg.getSenderId()) {
                            mSenderTextView.setText(member.getName());
                            break;
                        }
                    }

                    // 3, 显示消息时间
                    mTimeTextView.setText(timeStr(latestMsg.getSendTime()));
                    // 4, 显示消息内容
                    mMsgTextView.setText(latestMsg.getMsg());
                }
            }

            mRedDotCircleView.setVisibility(View.INVISIBLE);
            if (topic.showDot) {
                mRedDotCircleView.setVisibility(View.VISIBLE);
            }
        }

        private String timeStr(long timestamp) {
            String ret = null;

            Date now = new Date();
            Date date = new Date(timestamp);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String nowStr = formatter.format(now);
            String dateStr = formatter.format(date);

            // 由于server time可能有误差，所有未来时间也当做今天
            if ((nowStr.equals(dateStr)) || (date.getTime() > now.getTime())) {
                // 在同一天，显示"上午 10:36"
                SimpleDateFormat dateFormat = new SimpleDateFormat("a hh:mm", Locale.CHINA);
                ret = dateFormat.format(date);
                return ret;
            }

            Date nowZero = null; // 今天零点
            try {
                nowZero = formatter.parse(nowStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((nowZero.getTime() - date.getTime()) < 24 * 60 * 60 * 1000) {
                // 昨天
                ret = "昨天";
                return ret;
            }

            // 星期三
            SimpleDateFormat formatter2 = new SimpleDateFormat("EEE", Locale.CHINA);
            ret = formatter2.format(date);
            return ret;
        }
    }
}
