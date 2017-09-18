package com.yanxiu.gphone.faceshow.course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailBean;
import com.yanxiu.gphone.faceshow.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.bean.CourseArrangeBean;

import java.util.ArrayList;


/**
 * Created by 戴延枫
 * 课程详情adapter
 */

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int HEADER = 1;//头部
    private final int ITEM = 2;//列表item

    private ArrayList<CourseDetailBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public CourseDetailAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<CourseDetailBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        CourseDetailBean bean = mList.get(position);
        if (TextUtils.isEmpty(bean.getCourseDate())) {
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
        final CourseDetailBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 1:
                CourseDateViewHolder holder1 = (CourseDateViewHolder) holder;
                holder1.course_date.setText(data.getCourseDate());
                break;
            case 2:
                CourseContentViewHolder holder2 = (CourseContentViewHolder) holder;
                holder2.course_name.setText(data.getCourseName());
                holder2.course_location.setText(data.getLocation());
                holder2.course_teacher.setText(data.getTeacher());
                holder2.course_time.setText(data.getTime());
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


