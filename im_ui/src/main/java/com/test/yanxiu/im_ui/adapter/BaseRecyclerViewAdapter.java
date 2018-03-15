package com.test.yanxiu.im_ui.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * recyclerview的adapter基类（暂时这么写，需要扩充）
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {
    protected RecyclerViewItemClickListener mRecyclerViewItemClickListener;

    public void addItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        mRecyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public interface RecyclerViewItemClickListener {

        /**
         * recyclerview的item点击回调
         * @param position
         */
        void onItemClick(int position);
    }

}
