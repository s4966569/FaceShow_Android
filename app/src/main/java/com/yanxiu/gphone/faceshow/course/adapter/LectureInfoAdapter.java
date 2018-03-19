package com.yanxiu.gphone.faceshow.course.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.course.bean.LecturerInfosBean;
import com.yanxiu.gphone.faceshow.customview.recyclerview.BaseRecyclerViewAdapter;
import com.yanxiu.gphone.faceshow.util.CornersImageTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author frc on 17-11-9.
 */

public class LectureInfoAdapter extends BaseRecyclerViewAdapter {
    private final String TAG=getClass().getSimpleName();
    private List<LecturerInfosBean> data = new ArrayList<>();

    public LectureInfoAdapter(List<LecturerInfosBean> lecturerInfos) {
        this.data = lecturerInfos;
        Log.i(TAG, "LectureInfoAdapter: "+lecturerInfos.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.course_detail_item, parent, false));
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture_info_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).setData(data.get(position));

        if (recyclerViewItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewItemClickListener.onItemClick(holder.itemView,position);
                }
            });

        }
        if (position == getItemCount() - 1) {
            ((ViewHolder) holder).line.setVisibility(View.GONE);
        } else {
            ((ViewHolder) holder).line.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_head)
        ImageView mImgHead;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.rv_lecture_brief)
        TextView mRvLectureBrief;
        @BindView(R.id.line)
        View line;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(LecturerInfosBean lecturerInfosBean) {
            mTvName.setText(lecturerInfosBean.getLecturerName());
//            Log.i(getClass().getSimpleName(), "setData: "+lecturerInfosBean.getLecturerBriefing());
//            Log.i(getClass().getSimpleName(), "setData: "+Html.fromHtml(lecturerInfosBean.getLecturerBriefing()));
            mRvLectureBrief.setText(Html.fromHtml(lecturerInfosBean.getLecturerBriefing().replace("\n","<br />")));
            Glide.with(itemView.getContext()).load(lecturerInfosBean.getLecturerAvatar())
                    .asBitmap().placeholder(R.drawable.classcircle_headimg)
                    .fitCenter()
                    .into(new CornersImageTarget(itemView.getContext(), mImgHead, 10));
        }
    }
}
