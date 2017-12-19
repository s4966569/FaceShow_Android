package com.yanxiu.gphone.faceshow.util.recyclerView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

/**
 * Created by frc on 2017/12/18.
 */

public class RecyclerViewHelper {
    private  RecyclerView mRecyclerView;
    private  RecyclerView.Adapter mAdapter;
    public  void recyclerView(Context context,RecyclerView recyclerView, RecyclerView.Adapter adapter){
        this.mRecyclerView=recyclerView;
        this.mAdapter =mAdapter;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }
}
