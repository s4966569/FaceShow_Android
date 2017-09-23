package com.yanxiu.gphone.faceshow.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.common.listener.OnRecyclerViewItemClickListener;
import com.yanxiu.gphone.faceshow.homepage.bean.ResourceBean;
import com.yanxiu.gphone.faceshow.http.resource.ResourceListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lufengqing on 2017/9/18.
 */

public class HomeResourcesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ResourceListResponse.DataBean.ElementsBean> mList = new ArrayList<>();
//    private ArrayList<ResourceBean> mList;

    private OnRecyclerViewItemClickListener mListener;

    public HomeResourcesAdapter(Context context, OnRecyclerViewItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

//    public void setData(ArrayList<ResourceBean> list) {
//        mList = list;
//    }

    public void updateData(List<ResourceListResponse.DataBean.ElementsBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
                View view1 = inflater.inflate(R.layout.resources_item, parent, false);
                viewHolder = new ResourceViewHolder(view1);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ResourceListResponse.DataBean.ElementsBean data = mList.get(position);
        switch (getItemViewType(position)) {
            case 0:
                ResourceViewHolder holder2 = (ResourceViewHolder) holder;
                holder2.resource_name.setText(data.getResName());
                holder2.resource_time.setText(data.getCreateTime());
                holder2.resource_img.setImageResource(R.drawable.coursedetail_pdf);
                holder2.resource_layout.setOnClickListener(new View.OnClickListener() {
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

    class ResourceViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.resource_name)
        TextView resource_name;
        @BindView(R.id.resource_time)
        TextView resource_time;
        @BindView(R.id.resource_img)
        ImageView resource_img;
        @BindView(R.id.resource_layout)
        RelativeLayout resource_layout;

        public ResourceViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


