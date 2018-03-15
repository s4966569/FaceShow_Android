package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.support.v4.view.ScrollingView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.yanxiu.common_base.utils.SrtLogger;
import com.test.yanxiu.im_ui.callback.OnPullToRefreshCallback;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by cailei on 15/03/2018.
 */

public class RecyclerViewPullToRefreshHelper {
    private OnPullToRefreshCallback mCallback;
    private boolean isLoading = false;

    public RecyclerViewPullToRefreshHelper(Context context, RecyclerView v) {
        RecyclerView.OnScrollListener listener;
        v.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // TBD:cailei 目前是松手后再判断，需要改进
                if ((!isLoading) && (newState == SCROLL_STATE_IDLE) && (!recyclerView.canScrollVertically(-1)))  {
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
