package com.yanxiu.gphone.faceshow.classcircle;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.classcircle.adapter.ClassCircleAdapter;
import com.yanxiu.gphone.faceshow.classcircle.mock.MockUtil;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.SizeChangeCallbackView;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;

/**
 * 首页 “班级圈”Fragment
 */
public class ClassCircleFragment extends FaceShowBaseFragment implements LoadMoreRecyclerView.LoadMoreListener ,View.OnClickListener, ClassCircleAdapter.onCommentClickListener {

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;
    private EditText mCommentView;
    private SizeChangeCallbackView mAdjustPanView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PublicLoadLayout rootView=new PublicLoadLayout(getContext());
        rootView.setContentView(R.layout.fragment_classcircle);
        initView(rootView);
        listener();
        initData();
        return rootView;
    }

    private void initView(View rootView){
        mCommentView= (EditText) rootView.findViewById(R.id.ed_comment);
        mAdjustPanView= (SizeChangeCallbackView) rootView.findViewById(R.id.sc_adjustpan);
        mClassCircleRecycleView= (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mClassCircleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassCircleAdapter=new ClassCircleAdapter(getContext());
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);
    }

    private void listener(){
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
        mClassCircleAdapter.setCommentClickListener(ClassCircleFragment.this);
    }

    private void initData(){
        mClassCircleAdapter.setData(MockUtil.getClassCircleMockList());
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

    @Override
    public void commentClick(final int position, ClassCircleMock mock, ClassCircleMock.Comment comment, boolean isCommentMaster) {
        Toast.makeText(getContext(),mock.userId,Toast.LENGTH_SHORT).show();

        mCommentView.setVisibility(View.VISIBLE);
        mCommentView.setFocusable(true);
        mAdjustPanView.setViewSizeChangedCallback(new SizeChangeCallbackView.onViewSizeChangedCallback() {
            @Override
            public void sizeChanged(int visibility) {
                if (visibility==View.VISIBLE) {
                    ((MainActivity)getActivity()).setBottomVisibility(View.GONE);
                    mClassCircleRecycleView.scrollToPosition(0);
                    mClassCircleRecycleView.scrollToPosition(position);
                }else {
                    ((MainActivity)getActivity()).setBottomVisibility(View.VISIBLE);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void commentFinish() {
        mAdjustPanView.setViewSizeChangedCallback(null);
        mCommentView.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
    }
}
