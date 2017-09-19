package com.yanxiu.gphone.faceshow.course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 课程详情adapter
 */

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int HEADER = 1;//头部
    private final int ITEM = 2;//列表item

    private ArrayList<CourseDetailBean.CourseDetailBeanItem> mList;

    private OnRecyclerViewItemClickListener mListener;

    public CourseDetailAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<CourseDetailBean.CourseDetailBeanItem> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        CourseDetailBean.CourseDetailBeanItem bean = mList.get(position);
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
                View header = inflater.inflate(R.layout.course_detail_header, parent, false);
                viewHolder = new CourseHeaderViewHolder(header);
                break;
            case 2:
                View item = inflater.inflate(R.layout.course_detail_item, parent, false);
                viewHolder = new CourseItemViewHolder(item);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CourseDetailBean.CourseDetailBeanItem data = mList.get(position);
        switch (getItemViewType(position)) {
            case 1:
                CourseHeaderViewHolder holder1 = (CourseHeaderViewHolder) holder;
                holder1.course_detail_time.setText(data.getTime());
                holder1.course_name.setText(data.getCourseName());
                holder1.course_detail_location.setText(data.getLocation());
                holder1.course_detail_teacher.setText(data.getTeacher());
                holder1.course_detail_txt.setText(data.getCourse_detail());
                break;
            case 2:
                CourseItemViewHolder holder2 = (CourseItemViewHolder) holder;
                holder2.course_detail_item_txt.setText(data.getCourse_detail_item_txt());
                Glide.with(mContext).load(data.getImgUrl()).asBitmap().into(holder2.course_detail_item_icon);
                holder2.course_detail_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(position, data);
                        }
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
     * 课程详情头部
     */
    class CourseHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView course_name;
        private TextView course_detail_time;
        private TextView course_detail_teacher;
        private TextView course_detail_location;
        private TextView course_detail_txt;

        public CourseHeaderViewHolder(View itemView) {
            super(itemView);
            course_name = (TextView) itemView.findViewById(R.id.course_detaile_name);
            course_detail_time = (TextView) itemView.findViewById(R.id.course_detaile_name);
            course_detail_teacher = (TextView) itemView.findViewById(R.id.course_detail_teacher);
            course_detail_location = (TextView) itemView.findViewById(R.id.course_detail_location);
            course_detail_txt = (TextView) itemView.findViewById(R.id.course_detail_txt);
        }
    }

    /**
     * 课程item
     */
    class CourseItemViewHolder extends RecyclerView.ViewHolder {
        private View course_detail_item_layout;
        private TextView course_detail_item_txt;
        private ImageView course_detail_item_icon;

        public CourseItemViewHolder(View itemView) {
            super(itemView);
            course_detail_item_layout = itemView.findViewById(R.id.course_detail_item_layout);
            course_detail_item_txt = (TextView) itemView.findViewById(R.id.course_detail_item_txt);
            course_detail_item_icon = (ImageView) itemView.findViewById(R.id.course_detail_item_icon);
//            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }
    }
}


