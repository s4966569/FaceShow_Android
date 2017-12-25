package com.yanxiu.gphone.faceshow.course.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.activity.CourseIntroductionActivity;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean;
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.customview.MaxLineTextView;
import com.yanxiu.gphone.faceshow.util.StringUtils;
import com.yanxiu.gphone.faceshow.util.talkingdata.EventUpdate;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.attachment;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.interact;
import static com.yanxiu.gphone.faceshow.course.bean.CourseDetailItemBean.lecturer;


/**
 * Created by 戴延枫
 * 课程详情adapter
 */

public class CourseDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int HEADER = 1;//头部
    private final int ITEM = 2;//列表item

    private ArrayList<CourseDetailItemBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public CourseDetailAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<CourseDetailItemBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        CourseDetailItemBean bean = mList.get(position);
        if (bean.getMyDataType() == CourseDetailItemBean.header) {
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
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CourseDetailItemBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 1:
                CourseHeaderViewHolder holder1 = (CourseHeaderViewHolder) holder;
                holder1.course_detail_time.setText(StringUtils.getCourseTime(data.getStartTime()) + " 至 " + StringUtils.getCourseTime(data.getEndTime()));
                holder1.course_name.setText(data.getCourseName());
                holder1.course_detail_location.setText(TextUtils.isEmpty(data.getSite()) ? "待定" : data.getSite());
                holder1.course_detail_teacher.setText(data.getLecturer());
                holder1.course_detail_txt.setText(TextUtils.isEmpty(data.getBriefing()) ? "暂无" : data.getBriefing());
                holder1.course_detail_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventUpdate.onCourseDetailButton(holder.itemView.getContext());
                        CourseIntroductionActivity.invoke(mContext, data.getBriefing());
                    }
                });
                break;
            case 2:
                CourseItemViewHolder holder2 = (CourseItemViewHolder) holder;
                String text;
                switch (data.getMyDataType()) {
                    case lecturer:
                        text = ((LecturerInfosBean) data).getLecturerName();
                        holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_lecturer);
                        break;
                    case attachment:
                        text = ((AttachmentInfosBean) data).getResName();
                        AttachmentInfosBean attachmentInfosBean = (AttachmentInfosBean) data;

                        switch (attachmentInfosBean.getResType()) {

                            case AttachmentInfosBean.EXCEL:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_excel);
                                break;
                            case AttachmentInfosBean.PDF:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_pdf);
                                break;
                            case AttachmentInfosBean.PPT:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_ppt);
                                break;
                            case AttachmentInfosBean.TEXT:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_txt);
                                break;
                            case AttachmentInfosBean.WORD:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_word);
                                break;
                            default:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_link);
                                break;

                        }
                        break;
                    case interact:
                        text = ((InteractStepsBean) data).getInteractName();
                        InteractStepsBean interactStepsBean = (InteractStepsBean) data;
                        switch (interactStepsBean.getInteractType()) {
                            case InteractStepsBean.VOTE:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_vote);
                                break;
                            case InteractStepsBean.DISCUSS:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_discuss);
                                break;
                            case InteractStepsBean.QUESTIONNAIRES:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_questionnaires);
                                break;
                            case InteractStepsBean.CHECK_IN:
                                holder2.course_detail_item_icon.setImageResource(R.drawable.coursedetail_vote);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        text = "";
                }
                holder2.course_detail_item_txt.setText(text);
                holder2.course_detail_item_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onItemClick(position, data);
                        }
                    }
                });
                break;
            default:
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
        private final TextView course_detail_all;
        private TextView course_name;
        private TextView course_detail_time;
        private TextView course_detail_teacher;
        private TextView course_detail_location;
        private MaxLineTextView course_detail_txt;

        public CourseHeaderViewHolder(View itemView) {
            super(itemView);
            course_name = (TextView) itemView.findViewById(R.id.course_detaile_name);
            course_detail_time = (TextView) itemView.findViewById(R.id.course_detail_time);
            course_detail_teacher = (TextView) itemView.findViewById(R.id.course_detail_teacher);
            course_detail_location = (TextView) itemView.findViewById(R.id.course_detail_location);
            course_detail_txt = (MaxLineTextView) itemView.findViewById(R.id.course_detail_txt);
            course_detail_all = (TextView) itemView.findViewById(R.id.course_detail_all);
            course_detail_txt.setOnLinesChangedListener(new MaxLineTextView.onLinesChangedListener() {
                @Override
                public void onLinesChanged(int lines) {
                    if (lines > 5) {
                        course_detail_all.setVisibility(VISIBLE);
                    } else {
                        course_detail_all.setVisibility(GONE);
                    }
                }
            });
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


