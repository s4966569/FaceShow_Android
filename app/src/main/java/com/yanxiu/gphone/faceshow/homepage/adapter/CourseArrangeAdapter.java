package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.bean.CourseBean;
import com.yanxiu.gphone.faceshow.util.StringUtils;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 课程安排adapter
 */

public class CourseArrangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int COURSE_DATE = 1;//课程日期
    private final int COURSE_COTENT = 2;//内容

    private ArrayList<CourseBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public CourseArrangeAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<CourseBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        CourseBean bean = mList.get(position);
        if (TextUtils.isEmpty(bean.getDate())) {
            return COURSE_COTENT;
        } else {
            //课程日期
            return COURSE_DATE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 1:
                View view1 = inflater.inflate(R.layout.course_arrange_item1, parent, false);
                viewHolder = new CourseDateViewHolder(view1);
                break;
            case 2:
                View view2 = inflater.inflate(R.layout.course_arrange_item2, parent, false);
                viewHolder = new CourseContentViewHolder(view2);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CourseBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 1:
                CourseDateViewHolder holder1 = (CourseDateViewHolder) holder;
                String date = data.getDate();
                if (data.isToday()) {
                    date += " 今日课程";
                }
                holder1.course_date.setText(date);
                break;
            case 2:
                CourseContentViewHolder holder2 = (CourseContentViewHolder) holder;
                holder2.course_name.setText(data.getCourseName());
                holder2.course_location.setText(TextUtils.isEmpty(data.getSite()) ? "待定" : data.getSite());
                holder2.course_teacher.setText(data.getLecturer());
                holder2.course_time.setText(StringUtils.getCourseTime(data.getStartTime()) + " 至 " + StringUtils.getCourseTime(data.getEndTime()));
                holder2.course_layout.setOnClickListener(new View.OnClickListener() {
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
     * 课程日期
     */
    class CourseDateViewHolder extends RecyclerView.ViewHolder {
        private TextView course_date;

        public CourseDateViewHolder(View itemView) {
            super(itemView);
            course_date = (TextView) itemView.findViewById(R.id.course_date);
        }
    }

    /**
     * 课程内容
     */
    class CourseContentViewHolder extends RecyclerView.ViewHolder {
        private View course_layout;
        private TextView course_name;
        private TextView course_time;
        private TextView course_teacher;
        private TextView course_location;

        public CourseContentViewHolder(View itemView) {
            super(itemView);
            course_layout = itemView.findViewById(R.id.course_layout);
            course_name = (TextView) itemView.findViewById(R.id.course_name);
            course_time = (TextView) itemView.findViewById(R.id.course_time);
            course_teacher = (TextView) itemView.findViewById(R.id.course_teacher);
            course_location = (TextView) itemView.findViewById(R.id.course_location);
//            TextTypefaceUtil.setViewTypeface(TextTypefaceUtil.TypefaceType.METRO_PLAY, mPrefixNumber, mPostfixNumber);
        }
    }
}


