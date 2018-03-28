package com.test.yanxiu.im_ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.yanxiu.im_core.db.DbMember;
import com.test.yanxiu.im_core.db.DbMsg;
import com.test.yanxiu.im_core.db.DbMyMsg;
import com.test.yanxiu.im_core.db.DbTopic;
import com.test.yanxiu.im_core.dealer.DatabaseDealer;
import com.test.yanxiu.im_ui.callback.OnRecyclerViewItemClickCallback;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cailei on 14/03/2018.
 */

public class MsgListAdapter extends RecyclerView.Adapter<MsgListAdapter.MsgListItemViewHolder> {
    private final String TAG=getClass().getSimpleName();

    enum ItemType {
        LOADING,
        DATETIME,
        DBMSG,
        DBMYMSG,

        BOTTOM
    }

    private Context mContext;
    private List<DbMsg> mDatas;
    private List<Item> mUiDatas;
    private DbTopic topic;

    public DbTopic getTopic() {
        return topic;
    }

    public void setTopic(DbTopic topic) {
        this.topic = topic;
    }

    public MsgListAdapter(Context context) {
        mContext = context;
    }

    private OnRecyclerViewItemClickCallback mOnItemClickCallback;

    public void setmOnItemClickCallback(OnRecyclerViewItemClickCallback mOnItemClickCallback) {
        this.mOnItemClickCallback = mOnItemClickCallback;
    }

    private boolean isLoading;
    private Item loadingItem = new Item(ItemType.LOADING);
    public void setIsLoading(boolean loading) {
        if (isLoading == loading) {
            return;
        }

        if (loading) {
            mUiDatas.add(0, loadingItem);
        } else {
            mUiDatas.remove(loadingItem);
        }

        isLoading = loading;
    }

    public void setmDatas(List<DbMsg> mDatas) {
        this.mDatas = mDatas;
        // 重新生成用于显示的mUiDatas
        generateUiDatas();
        //notifyDataSetChanged();
    }

    // 从现有的mDatas，生成mUiDatas
    private void generateUiDatas() {
        mUiDatas = new ArrayList<>();

//        for (int i = 0; i < mDatas.size(); i++) {
//            Log.i(TAG, "generateUiDatas: "+new Gson().toJson(mDatas.get(i)));
//        }
        for (int i = 0; i < mDatas.size(); i++) {
            DbMsg curDbMsg = mDatas.get(i);
            // 最后一条跟当前时间比较，其余的跟前一条时间比较
            long nextSendTime = Long.MAX_VALUE;
            if (i != (mDatas.size() - 1)) {
                DbMsg nextDbMsg = mDatas.get(i+1);
                nextSendTime = nextDbMsg.getSendTime();
            }

            // 当前msg入队
            Item msgItem = new Item();
            if (curDbMsg.getSenderId() == Constants.imId) {
                msgItem.setType(ItemType.DBMYMSG);
                msgItem.setMyMsg((DbMyMsg) curDbMsg);
            } else {
                msgItem.setType(ItemType.DBMSG);
                msgItem.setMsg(curDbMsg);
            }
            mUiDatas.add(0, msgItem);

            // 如果超过5分钟，则插入时间
            if ((curDbMsg.getSendTime() - nextSendTime) > 5*60*1000) {
                Item timeItem = new Item();
                timeItem.setType(ItemType.DATETIME);
                timeItem.setTimestamp(curDbMsg.getSendTime());
                mUiDatas.add(0, timeItem);
            }

        }

        // 最后一条消息插入时间
        if ((mDatas != null)  && (mDatas.size() > 0)) {
            Item timeItem = new Item();
            timeItem.setType(ItemType.DATETIME);
            timeItem.setTimestamp(mDatas.get(mDatas.size()-1).getSendTime());

            mUiDatas.add(0, timeItem);
        }

        // 底下留白
        Item bottomItem = new Item();
        bottomItem.setType(ItemType.BOTTOM);
        mUiDatas.add(bottomItem);
    }


    // 加载更多时需要滚动到相应的位置
    public int uiAddedNumberForMsg(DbMsg msg) {
        int position = 0;
        for (Item uiItem : mUiDatas) {
            if ((uiItem.getMsg() == msg) || (uiItem.getMyMsg() == msg)) {
                position = mUiDatas.indexOf(uiItem);
                break;
            }
        }


        return position == 1 ? 0: position; // 因为为最初的一个消息时，本来之前就插入了一个Datetime
    }


    @Override
    public int getItemViewType(int position) {
        Item item = mUiDatas.get(position);
        return item.type.ordinal();
    }

    @Override
    public MsgListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.LOADING.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_loading, parent, false);
            return new LoadingViewHolder(v);
        }

        if (viewType == ItemType.DATETIME.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_datetime, parent, false);
            return new DatetimeViewHolder(v);
        }

        if (viewType == ItemType.DBMSG.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_dbmsg, parent, false);
            return new MsgViewHolder(v);
        }

        if (viewType == ItemType.DBMYMSG.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_msg_dbmymsg, parent, false);
            return new MyMsgViewHolder(v);
        }

        if (viewType == ItemType.BOTTOM.ordinal()) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_padding_layout, parent, false);
            return new BottomViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MsgListItemViewHolder holder, final int position) {
        final Item item = mUiDatas.get(position);

        holder.setData(item);

        if (holder instanceof MyMsgViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickCallback != null) {
                        mOnItemClickCallback.onItemClick(position, item.getMyMsg());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mUiDatas.size();
    }

    public abstract class MsgListItemViewHolder extends RecyclerView.ViewHolder {
        public MsgListItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setData(Item item);
    }

    public class BottomViewHolder extends  MsgListItemViewHolder {
        public BottomViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Item item) {

        }
    }

    public class LoadingViewHolder extends MsgListItemViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Item item) {

        }
    }

    public class DatetimeViewHolder extends MsgListItemViewHolder {
        private TextView mDatetimeTextView;
        public DatetimeViewHolder(View itemView) {
            super(itemView);
            mDatetimeTextView = itemView.findViewById(R.id.datetime_textview);
        }

        @Override
        public void setData(Item item) {
            mDatetimeTextView.setText(timeStr(item.timestamp));
        }

        private String timeStr(long timestamp) {
            String ret = null;

            Date now = new Date();
            Date date = new Date(timestamp);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
            String nowStr = formatter.format(now);
            String dateStr = formatter.format(date);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("a hh:mm", Locale.CHINA);
            ret = timeFormatter.format(date);

            // 由于server time可能有误差，所有未来时间也当做今天
            if ((nowStr.equals(dateStr)) || (date.getTime() > now.getTime())) {
                // 在同一天，显示"上午 10:36"
                ret = timeFormatter.format(date);
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
                ret = "昨天 " + timeFormatter.format(date);
                return ret;
            }

            // 星期三 周三->星期三
            SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE", Locale.CHINA);
            ret = formatter2.format(date) + " " + timeFormatter.format(date);

            return ret;
        }
    }

    public class MsgViewHolder extends MsgListItemViewHolder {
        private ImageView mAvatarImageView;
        private TextView mNameTextView;
        private TextView mMsgTextView;

        public MsgViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mNameTextView = itemView.findViewById(R.id.name_textview);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mMsgTextView.setTextIsSelectable(true);
        }

        @Override
        public void setData(Item item) {
            DbMsg msg = item.getMsg();

            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mNameTextView.setText("");
            final DbMember sender = DatabaseDealer.getMemberById(msg.getSenderId());
            if (sender != null) {
                Glide.with(mContext)
                        .load(sender.getAvatar())
                        .into(mAvatarImageView);
                mNameTextView.setText(sender.getName());
            }

            mMsgTextView.setText(msg.getMsg());

            mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((topic != null) && (topic.getType().equals("2"))) {
                        // 群聊点击头像
                        EventBus.getDefault().post(sender);
                    }
                }
            });
        }
    }

    public class MyMsgViewHolder extends MsgListItemViewHolder {
        private ImageView mAvatarImageView;
        private TextView mMsgTextView;
        private ProgressBar mStateSendingProgressBar;
        private ImageView mStateFailedImageView;
        public MyMsgViewHolder(View itemView) {
            super(itemView);
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            mMsgTextView = itemView.findViewById(R.id.msg_textview);
            mMsgTextView.setTextIsSelectable(true);
            mStateSendingProgressBar = itemView.findViewById(R.id.state_sending_progressbar);
            mStateFailedImageView = itemView.findViewById(R.id.state_fail_imageview);
        }

        @Override
        public void setData(Item item) {
            DbMyMsg myMsg = item.getMyMsg();

            // 设置头像
            mAvatarImageView = itemView.findViewById(R.id.avatar_imageview);
            DbMember sender = DatabaseDealer.getMemberById(Constants.imId);
            if (sender != null) {
                Glide.with(mContext)
                        .load(sender.getAvatar())
                        .into(mAvatarImageView);
            }

            // 设置消息内容
            mMsgTextView.setText(myMsg.getMsg());

            // 设置状态
            mStateSendingProgressBar.setVisibility(View.GONE);
            mStateFailedImageView.setVisibility(View.GONE);

            if (myMsg.getState() == DbMyMsg.State.Sending.ordinal()) {
                mStateSendingProgressBar.setVisibility(View.VISIBLE);
            }

            if (myMsg.getState() == DbMyMsg.State.Failed.ordinal()) {
                mStateFailedImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    public class Item {
        private ItemType type;   // 0-loading, 1-datetime, 2-dbmsg, 3-dbmymsg
        private DbMsg msg;
        private DbMyMsg myMsg;
        private long timestamp;

        public Item() {

        }

        public Item(ItemType t) {
            type = t;
        }

        public ItemType getType() {
            return type;
        }

        public void setType(ItemType type) {
            this.type = type;
        }

        public DbMsg getMsg() {
            return msg;
        }

        public void setMsg(DbMsg msg) {
            this.msg = msg;
        }

        public DbMyMsg getMyMsg() {
            return myMsg;
        }

        public void setMyMsg(DbMyMsg myMsg) {
            this.myMsg = myMsg;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
