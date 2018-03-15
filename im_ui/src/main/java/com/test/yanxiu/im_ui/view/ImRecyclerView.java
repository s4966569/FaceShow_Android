
package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

/**
 * 支持上拉加载更多的recyclerView
 *
 * @dyf 2017年9月14日11:13:44
 */
public class ImRecyclerView extends RecyclerView {

    /**
     * 上拉加载更多是否可用
     * true 可用
     * false 不可用
     */
    private boolean pullDownLoadMoreEnable = true;

    /**
     * 上拉加载更多是否可用
     * true 可用
     * false 不可用
     */
    public void setLoadMoreEnable(boolean pullDownLoadMoreEnable) {
        this.pullDownLoadMoreEnable = pullDownLoadMoreEnable;
    }

    /**
     * 加载更多监听器
     */
    public interface LoadMoreListener {
        /**
         * 加载更多中
         */
        void onLoadMore(ImRecyclerView refreshLayout);

        void onLoadmoreComplte();
    }

    private LoadMoreListener mListener;

    public void setLoadMoreListener(LoadMoreListener listener) {
        mListener = listener;
    }

    /**
     * loadMore结束
     */
    public void finishLoadMore() {
        pullDownLoadMoreEnable = true;
        if (mListener != null) {
            mListener.onLoadmoreComplte();
        }
    }

    public ImRecyclerView(Context context) {
        super(context);
        initView();
    }

    public ImRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ImRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOnScrollListener(listener);
    }

    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (pullDownLoadMoreEnable) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (manager.findLastVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) {
//                    if (mListener != null) {
//                        loadMoreEnable = false;
//                        mListener.onLoadMore(LoadMoreRecyclerView.this);
//                    }
//                }
                if (manager.findFirstVisibleItemPosition() == 0) {
                    if (mListener != null) {
                        pullDownLoadMoreEnable = false;
                        mListener.onLoadMore(ImRecyclerView.this);
//                        Toast.makeText(getContext(), "111", Toast.LENGTH_SHORT).show();
                        Log.e("111", "到顶");
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };


}