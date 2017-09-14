package com.yanxiu.gphone.faceshow.homepage;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.classcircle.adapter.ClassCircleAdapter;
import com.yanxiu.gphone.faceshow.view.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.view.PublicLoadLayout;


/**
 * 首页 “班级圈”Fragment
 */
public class ClassCircleFragment extends FaceShowBaseFragment implements LoadMoreRecyclerView.LoadMoreListener ,View.OnClickListener{

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PublicLoadLayout rootView=new PublicLoadLayout(getContext());
        rootView.setContentView(R.layout.fragment_3);
        initView(rootView);
        listener();
        initData();
        return rootView;
    }

    private void initView(View rootView){
        mClassCircleRecycleView= (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mClassCircleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassCircleAdapter=new ClassCircleAdapter();
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);
    }

    private void listener(){
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
    }

    private void initData(){

    }

    @Override
    public void onLoadMore(LoadMoreRecyclerView refreshLayout) {

    }

    @Override
    public void onLoadmoreComplte() {

    }

    @Override
    public void onClick(View v) {

    }
}
