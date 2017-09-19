package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.homepage.bean.ProjectTaskBean;
import com.yanxiu.gphone.faceshow.homepage.bean.ResourceBean;
import com.yanxiu.gphone.faceshow.listener.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lufengqing on 2017/9/18.
 */

public class ProjectTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ProjectTaskBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public ProjectTaskAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setData(ArrayList<ProjectTaskBean> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case 0:
                View view1 = inflater.inflate(R.layout.project_task_item, parent, false);
                viewHolder = new ProjectTaskViewHolder(view1);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ProjectTaskBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 0:
                ProjectTaskViewHolder holder2 = (ProjectTaskViewHolder) holder;
                holder2.project_task_name.setText(data.getName());
                holder2.project_task_time.setText(data.getTime());
//                holder2.project_task_img
                if(TextUtils.equals(data.getStatus(), "1")) {
                    holder2.project_task_status.setText(mContext.getResources().getString(R.string.has_completed));
                    holder2.project_task_status.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                }else {
                    holder2.project_task_status.setText(mContext.getResources().getString(R.string.has_uncompleted));
                    holder2.project_task_status.setTextColor(mContext.getResources().getColor(R.color.color_999999));
                }

                holder2.project_task_layout.setOnClickListener(new View.OnClickListener() {
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

    class ProjectTaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.project_task_name)
        TextView project_task_name;
        @BindView(R.id.project_task_time)
        TextView project_task_time;
        @BindView(R.id.project_task_status)
        TextView project_task_status;
        @BindView(R.id.project_task_img)
        ImageView project_task_img;
        @BindView(R.id.project_task_layout)
        RelativeLayout project_task_layout;

        public ProjectTaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
