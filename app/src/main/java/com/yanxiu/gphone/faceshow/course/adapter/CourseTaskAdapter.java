package com.yanxiu.gphone.faceshow.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

    public CourseTaskAdapter(List<InteractStepsBean> interactSteps) {
        this.data = interactSteps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_task_adapter_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((ViewHolder) holder).setData(data.get(position));
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
        @BindView(R.id.img_task_icon)
        ImageView mImgTaskIcon;
        @BindView(R.id.tv_task_name)
        TextView mTvTaskName;
        @BindView(R.id.tv_task_statue)
        TextView mTvTaskStatue;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(InteractStepsBean interactStep) {
            mTvTaskName.setText(interactStep.getInteractName());



            /*由于 管理端与 学员端 type 类型不同 这里复制 过来 对学员端string进行了类型转换*/
            switch (Integer.valueOf(interactStep.getInteractType())) {
                case 6:
                    mImgTaskIcon.setImageResource(R.drawable.ic_sign_in);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number,
                            Integer.valueOf(interactStep.getStepFinished()),
//                            Integer.valueOf(interactStep.getTotalStudentNum()))));
                           0)));
                    break;
                case 5:
                    mImgTaskIcon.setImageResource(R.drawable.icon_questionnaire);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number,
                            /*学员端没有 学生人数 信息*/
                            0,0)));
//                            interactStep.getFinishedStudentNum(),
//                            interactStep.getTotalStudentNum())));
                    break;
                case 4:
                    int openType = 1;
                    if (openType == Integer.valueOf(interactStep.getStepStatus())) {
                        mTvTaskStatue.setText("已开启");
                    } else {
                        mTvTaskStatue.setText("未开启");
                    }
                    mImgTaskIcon.setImageResource(R.drawable.ic_comment);
                    break;
                case 3:
                    mImgTaskIcon.setImageResource(R.drawable.icon_vote);
                    mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_finish_number,
                           /*学员端没有 学生人数 信息*/
                            0,0)));
//                            interactStep.getFinishedStudentNum(),
//                            interactStep.getTotalStudentNum())));
                    break;
                default:
                    mImgTaskIcon.setImageResource(R.drawable.ic_do_not_know);
            }

        }
    }
}
