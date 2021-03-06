package com.yanxiu.gphone.faceshow.customview.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by frc on 17-10-27.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {
    protected RecyclerViewItemClickListener recyclerViewItemClickListener;
    protected RecyclerViewItemClickWithIntentListener mRecyclerViewItemClickWithIntentListener;

    public void addItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    public void addItemClickWithIntentListener(RecyclerViewItemClickWithIntentListener recyclerViewItemClickListener) {
        this.mRecyclerViewItemClickWithIntentListener = recyclerViewItemClickListener;
    }

}
