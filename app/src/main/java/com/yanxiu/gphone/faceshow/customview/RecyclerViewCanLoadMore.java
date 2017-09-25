package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 给RecyclerView添加了加载更多的功能
 *
 * @author frc
 *         created at 17-1-16.
 */

public class RecyclerViewCanLoadMore extends RecyclerView {
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerViewCanLoadMore(Context context) {
        this(context, null);
    }

    public RecyclerViewCanLoadMore(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    int lastVisibleItem;

    public RecyclerViewCanLoadMore(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == recyclerView.getAdapter().getItemCount()) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore(recyclerView, newState, lastVisibleItem);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            }
        });
    }


    public void addLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public interface OnLoadMoreListener

    {
        void onLoadMore(RecyclerView recyclerView, int newState, int lastVisibleItem);
    }

}
