package com.yanxiu.gphone.faceshow.course.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.activity.CourseIntroductionActivity;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.course.bean.DiscussBean;
import com.yanxiu.gphone.faceshow.util.YXPictureManager;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 讨论adapter
 */

public class CourseDiscussAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int HEADER = 1;//头部
    private final int ITEM = 2;//列表item

    private ArrayList<DiscussBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public CourseDiscussAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<DiscussBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        DiscussBean bean = mList.get(position);
        if (bean.isHeader()) {
            return HEADER;
        } else {
            //课程日期
            return ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 1:
                View header = inflater.inflate(R.layout.course_discuss_header, parent, false);
                viewHolder = new DiscussHeaderViewHolder(header);
                break;
            case 2:
                View item = inflater.inflate(R.layout.course_discuss_item, parent, false);
                viewHolder = new DiscussItemViewHolder(item);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DiscussBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 1:
                DiscussHeaderViewHolder holder1 = (DiscussHeaderViewHolder) holder;
                holder1.discuss_title.setText(data.getTitle());
                holder1.discuss_feedback_count.setText("回复(" + data.getCount() + ")");
                break;
            case 2:
                final DiscussItemViewHolder holder2 = (DiscussItemViewHolder) holder;
                holder2.discuss_name.setText(data.getName());
                holder2.discuss_content.setText(data.getContent());
                holder2.discuss_time.setText(data.getTime());
                holder2.discuss_laud.setText(data.getLaudCount() == 0 ? "赞" : (data.getLaudCount() + ""));
                if(data.isHasLaud()){
                    holder2.discuss_laud.setTextColor(mContext.getResources().getColor(R.color.color_1da1f2));
                }else{
                    holder2.discuss_laud.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                }
                YXPictureManager.getInstance().showRoundPic(mContext, "http://scc.jsyxw.cn/answer/images/2017/0831/file_59a7d19f6cc53.jpg", holder2.discuss_img, 5, R.mipmap.ic_launcher);

                holder2.laud_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (mListener != null) {
//                            mListener.onItemClick(position, data);
//                        }
                        holder2.discuss_laud.setTextColor(mContext.getResources().getColor(R.color.color_1da1f2));
                        data.setHasLaud(true);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 讨论头部
     */
    class DiscussHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView discuss_title;
        private TextView discuss_feedback_count;

        public DiscussHeaderViewHolder(View itemView) {
            super(itemView);
            discuss_title = (TextView) itemView.findViewById(R.id.discuss_title);
            discuss_feedback_count = (TextView) itemView.findViewById(R.id.discuss_feedback_count);
        }
    }

    /**
     * 讨论item
     */
    class DiscussItemViewHolder extends RecyclerView.ViewHolder {
        private View laud_layout;
        private TextView discuss_name;
        private TextView discuss_content;
        private TextView discuss_time;
        private TextView discuss_laud;
        private ImageView discuss_img;

        public DiscussItemViewHolder(View itemView) {
            super(itemView);
            laud_layout = itemView.findViewById(R.id.laud_layout);
            discuss_name = (TextView) itemView.findViewById(R.id.discuss_name);
            discuss_content = (TextView) itemView.findViewById(R.id.discuss_content);
            discuss_time = (TextView) itemView.findViewById(R.id.discuss_time);
            discuss_laud = (TextView) itemView.findViewById(R.id.discuss_laud);
            discuss_img = (ImageView) itemView.findViewById(R.id.discuss_img);
        }
    }
}


