package com.yanxiu.gphone.faceshow.view;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/14 11:46.
 * Function :
 */
public abstract class ssss<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final int DEFAULT = 0x000;
    private static final int LOAD_MORE = 0x001;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE) {
//            return new LoadMoreView();
        }
        return ssscreatViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        sssbindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return getCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getCount()) {
            return LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    public abstract T ssscreatViewHolder(ViewGroup parent, int viewType);

    public abstract void sssbindViewHolder(T holder, int position);

    public abstract int getCount();

}
