package com.yanxiu.gphone.faceshow.qrsignup.recycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.customview.recyclerview.BaseRecyclerViewAdapter;

/**
 * Created by srt on 2018/3/1.
 * 扫码注册 的最后一步中  用户信息列表的 内容适配器
 */

public class ProfileRecyclerAdapter extends BaseRecyclerViewAdapter {


    private String[] itemTitle;
    public ProfileRecyclerAdapter(Context context) {
        itemTitle=context.getResources().getStringArray(R.array.qrsignup_profile_title);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_drawer_list_normal_layout,parent,false);

        return new ViewHoler(view);
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemTitle==null?0:itemTitle.length;
    }


    public static class ViewHoler extends RecyclerView.ViewHolder{
        protected TextView textView;
        public ViewHoler(View itemView) {
            super(itemView);
        }
    }
}
