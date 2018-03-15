package com.test.yanxiu.im_ui.view;

import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;

/**
 * Created by cailei on 15/03/2018.
 */

public class RecyclerViewPullToRefreshHelper {
    private OnPullToRefreshCallback mCallback;


    private boolean isLoading = false;

    public RecyclerViewPullToRefreshHelper(RecyclerView v) {
        v.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (manager.findFirstVisibleItemPosition() == 0) {
                        if (mCallback != null) {
                            isLoading = true;
                            mCallback.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

        });
    }

    public void setmCallback(OnPullToRefreshCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void loadingComplete() {
        isLoading = false;
    }
}
