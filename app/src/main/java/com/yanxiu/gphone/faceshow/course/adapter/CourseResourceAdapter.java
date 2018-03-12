package com.yanxiu.gphone.faceshow.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.AttachmentInfosBean;
import com.yanxiu.gphone.faceshow.customview.recyclerview.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-9.
 */

public class CourseResourceAdapter extends BaseRecyclerViewAdapter {
    private List<AttachmentInfosBean> data = new ArrayList<>();

    public CourseResourceAdapter(List<AttachmentInfosBean> elements) {
        this.data = elements;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.coursedetail_resource_layout, parent, false));

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
        @BindView(R.id.resource_img)
        ImageView mImgTaskIcon;
        @BindView(R.id.resource_name)
        TextView mTvTaskName;
//        @BindView(R.id.tv_task_statue)
//        TextView mTvTaskStatue;
//        @BindView(R.id.resource_time)
//        TextView mTvResourceTime;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(AttachmentInfosBean element) {
//            mTvResourceTime.setVisibility(View.GONE);
            mTvTaskName.setText(element.getResName());
            // TODO: 2018/3/12  设置资源上传时间 当前使用的数据结构中不包含 上传时间 字段
//            mTvResourceTime.setText(element.);
//            mTvTaskStatue.setText(Html.fromHtml(itemView.getContext().getString(R.string.have_read_person_number,
                   /*资源使用情况 阅读数量等 */
//                    0, 0)));
            switch (element.getResType()) {
                case "word":
                case "doc":
                case "docx":
                    mImgTaskIcon.setImageResource(R.drawable.word);
                    break;
                case "xls":
                case "xlsx":
                case "excel":
                    mImgTaskIcon.setImageResource(R.drawable.excel);
                    break;
                case "ppt":
                case "pptx":
                    mImgTaskIcon.setImageResource(R.drawable.ppt);
                    break;
                case "pdf":
                    mImgTaskIcon.setImageResource(R.drawable.pdf);
                    break;
                case "text":
                case "txt":
                    mImgTaskIcon.setImageResource(R.drawable.txt);
                    break;
                case "video":
                case "mp4":
                case "m3u8":
                    mImgTaskIcon.setImageResource(R.drawable.mp4);
                    break;
                case "audio":
                case "mp3":
                    mImgTaskIcon.setImageResource(R.drawable.mp3);
                    break;
                case "image":
                case "jpg":
                case "jpeg":
                case "gif":
                case "png":
                    mImgTaskIcon.setImageResource(R.drawable.jpg);
                    break;
                case "html":
                    mImgTaskIcon.setImageResource(R.drawable.html);
                    break;
                default:
                    mImgTaskIcon.setImageResource(R.drawable.weizhi);
                    break;
            }
//            if ("1".equals(element.getResType())) {
//                mImgTaskIcon.setImageResource(R.drawable.html);
//            }
        }
    }
}
