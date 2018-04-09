package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.util.Utils;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;
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
    private final String TAG=getClass().getSimpleName();
    private Context mContext;
    private List<DbTopic> mDatas;
    private OnRecyclerViewItemClickCallback mOnItemClickCallback;

    public void setmOnItemClickCallback(OnRecyclerViewItemClickCallback mOnItemClickCallback) {
        this.mOnItemClickCallback = mOnItemClickCallback;
    }

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
    public void onBindViewHolder(TopicViewHolder holder, final int position) {
        final DbTopic topic = mDatas.get(position);
        // 被提了bug
//        if (topic.latestMsgId == 0){
//            setVisibile(false,holder.itemView);
//        }else {
//            setVisibile(true,holder.itemView);
//        }
        setVisibile(true,holder.itemView);
        holder.setData(topic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickCallback != null) {
                    mOnItemClickCallback.onItemClick(position,topic);
                }
            }
        });
    }

    public void setVisibile(boolean isVisible,View view) {
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (isVisible) {
            param.height = Utils.dp2px(mContext,71);// 这里注意使用自己布局的根布局类型
            param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        view.setLayoutParams(param);
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

        private ImageView mAvatarImageView;
        private TextView mSenderTextView;
        private TextView mTimeTextView;
        private TextView mMsgTextView;
        private CircleView mRedDotCircleView;

        public TopicViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;

            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mSenderTextView = itemView.findViewById(R.id.sender_textview);
            mTimeTextView = itemView.findViewById(R.id.time_textView);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mRedDotCircleView = itemView.findViewById(R.id.reddot_circleview);
        }

        public void setData(final DbTopic topic) {
            // 默认
            mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
            mSenderTextView.setText("");
            mTimeTextView.setText("");
            mMsgTextView.setText("");

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
                    if (member.getImId() != Constants.imId) {
                        // 1, 显示对方头像
                        Glide.with(mContext)
                                .load(member.getAvatar())
                                .placeholder(R.drawable.icon_chat_unknown)
                                .error(R.drawable.icon_chat_unknown)
                                .into(mAvatarImageView);
                        // 2, 显示对方昵称(班级名)
                        //mSenderTextView.setText(member.getName() + "(" + topic.getGroup() + ")");
                        // 私聊不显示（班级）
                        Log.i(TAG, "setData: "+member.getName());
                        mSenderTextView.setText(member.getName());
                        break;
                    }
                }

                if (latestMsg != null) {
                    // 3, 显示消息时间
                    mTimeTextView.setText(timeStr(latestMsg.getSendTime()));

                    // 判断是否是 图片 消息
                    boolean isImage=!TextUtils.isEmpty(latestMsg.getViewUrl())||TextUtils.isEmpty(latestMsg.getLocalViewUrl())
                            &&TextUtils.equals("qiniu",latestMsg.getMsg());

                    // 4, 显示消息内容
                    if (isImage) {
                        mMsgTextView.setText("[图片]");
                    }else {
                        mMsgTextView.setText(latestMsg.getMsg());
                    }
                }
            }

            if (topic.getType().equals("2")) { // 群聊
                // 1, 显示班级默认图片
                mAvatarImageView.setImageResource(R.drawable.icon_chat_class);
                // 2, 显示班级群聊(班级名) 在消息判断外侧，保证列表 显示群组名称
                mSenderTextView.setText("班级群聊" + "(" + topic.getGroup() + ")");
                if (latestMsg != null) {
                    // 3, 显示消息时间
                    mTimeTextView.setText(timeStr(latestMsg.getSendTime()));
                    // 4, 显示消息内容 这里 如果有本地数据库没有的 已经被删除的sender 会不显示最新消息

                    //判断是否有sender 信息 先检查 topic member 列表
                    StringBuilder stringBuilder=new StringBuilder();
                    DbMember senderInfo=null;
                    for (DbMember member : topic.getMembers()) {
                        if (member.getImId() == latestMsg.getSenderId()) {
                            senderInfo=member;
                            Log.i(TAG, "setData:  member  in list "+member.getName());
                            break;
                        }
                    }
                    //如果member列表中没有（用户已被踢出topic） 查询本地数据库是否含有
                    if (senderInfo==null) {
                        senderInfo= DatabaseDealer.getMemberById(latestMsg.getSenderId());
                        Log.i(TAG, "setData: sender try to find in db "+(senderInfo!=null));
                    }
                    //将 找到的 sender 名字加入显示 string中
                    stringBuilder.append(senderInfo==null?" :":senderInfo.getName()+":");
                    //判断最后一条消息的类型是不是图片类
                    boolean isImage=!TextUtils.isEmpty(latestMsg.getViewUrl())||TextUtils.isEmpty(latestMsg.getLocalViewUrl())
                            &&TextUtils.equals("qiniu",latestMsg.getMsg());

                    if (isImage) {
                        //如果是图片类型 显示消息内容为[图片]
                        stringBuilder.append("[图片]");
                    }else {
                        //如果是文字类型 直接显示文字内容
                        stringBuilder.append(latestMsg.getMsg());
                    }
                    mMsgTextView.setText(stringBuilder.toString());
                }
            }

            mRedDotCircleView.setVisibility(View.INVISIBLE);
            if (topic.isShowDot()) {
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

            // 星期三 周三->星期三
            SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE ", Locale.CHINA);
            ret = formatter2.format(date);
            return ret;
        }
    }
}
