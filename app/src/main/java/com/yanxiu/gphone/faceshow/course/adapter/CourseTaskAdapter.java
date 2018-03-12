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
import com.yanxiu.gphone.faceshow.course.bean.InteractStepsBean;
import com.yanxiu.gphone.faceshow.customview.recyclerview.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-8.
 */

public class CourseTaskAdapter extends BaseRecyclerViewAdapter {
    private List<InteractStepsBean> data = new ArrayList<>();

    private Context mContext;

    public CourseTaskAdapter(List<InteractStepsBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
    }

    public CourseTaskAdapter(List<InteractStepsBean> interactSteps) {
        this.data = interactSteps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.project_task_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder) holder).setData(mContext, data.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewItemClickListener != null) {
                    recyclerViewItemClickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.course_detail_item_icon)
        ImageView mImgTaskIcon;
        @BindView(R.id.project_task_name)
        TextView mTvTaskName;
        @BindView(R.id.project_task_status)
        TextView mTvTaskStatue;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(Context context, InteractStepsBean interactStep) {
            mTvTaskName.setText(interactStep.getInteractName());

            /*由于 管理端与 学员端 type 类型不同 这里复制 过来 对学员端string进行了类型转换*/
            switch (Integer.valueOf(interactStep.getInteractType())) {
                case 6:
                    /*签到*/
                    mImgTaskIcon.setImageResource(R.drawable.ic_sign_in);
                    setTaskStatue(context, interactStep);
//                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number, Integer.valueOf(interactStep.getStepFinished()),
////                            Integer.valueOf(interactStep.getTotalStudentNum()))));
//                           0)));
                    break;
                case 5:
                    /*问卷*/
                    mImgTaskIcon.setImageResource(R.drawable.icon_questionnaire);
                    setTaskStatue(context, interactStep);
//                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number,
//                            /*学员端没有 学生人数 信息*/
//                            0,0)));
//                            interactStep.getFinishedStudentNum(),
//                            interactStep.getTotalStudentNum())));
                    break;
                case 4:
                    /*讨论*/
                    int openType = 1;
                    if (openType == Integer.valueOf(interactStep.getStepStatus())) {
                        mTvTaskStatue.setText("已开启");
                    } else {
                        mTvTaskStatue.setText("未开启");
                    }
                    mImgTaskIcon.setImageResource(R.drawable.ic_comment);
                    break;
                case 3:
                    /*投票*/
                    mImgTaskIcon.setImageResource(R.drawable.icon_vote);
                    setTaskStatue(context, interactStep);
//                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number,
                           /*学员端没有 学生人数 信息*/
//                            0,0)));
//                            interactStep.getFinishedStudentNum(),
//                            interactStep.getTotalStudentNum())));
                    break;
                default:
                    /*其他 未知*/
                    mImgTaskIcon.setImageResource(R.drawable.ic_do_not_know);
                    setTaskStatue(context, interactStep);
            }


        }
        /**设置任务完成情况 */
        private void setTaskStatue(Context context, InteractStepsBean interactStep) {
            if (TextUtils.equals(interactStep.getStepFinished(), "1")) {
                mTvTaskStatue.setText("已完成");
                mTvTaskStatue.setTextColor(context.getResources().getColor(R.color.color_task_finished));
            } else {
                mTvTaskStatue.setText("未完成");
                mTvTaskStatue.setTextColor(context.getResources().getColor(R.color.color_task_unfinished));
            }
        }
    }
}
