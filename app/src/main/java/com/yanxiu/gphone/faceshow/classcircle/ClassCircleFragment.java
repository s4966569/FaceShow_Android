package com.yanxiu.gphone.faceshow.classcircle;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.yanxiu.gphone.faceshow.util.ClassCircleTimeUtils;
import com.yanxiu.gphone.faceshow.util.Logger;

/**
 * 首页 “班级圈”Fragment
 */
public class ClassCircleFragment extends FaceShowBaseFragment implements LoadMoreRecyclerView.LoadMoreListener, View.OnClickListener, ClassCircleAdapter.onCommentClickListener, View.OnLongClickListener {

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;
    private RelativeLayout mCommentLayout;
    private EditText mCommentView;
    private SizeChangeCallbackView mAdjustPanView;
    private ImageView mFunctionView;
    private TextView mTitleView;
    private View mTopView;
    private int mVisibility=-1;
    private int mHeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PublicLoadLayout rootView = new PublicLoadLayout(getContext());
        rootView.setContentView(R.layout.fragment_classcircle);
        initView(rootView);
        listener();
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        mTopView = rootView.findViewById(R.id.il_title);
        ImageView mBackView = (ImageView) rootView.findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.INVISIBLE);
        mTitleView = (TextView) rootView.findViewById(R.id.title_layout_title);
        mFunctionView = (ImageView) rootView.findViewById(R.id.title_layout_right_img);
        mFunctionView.setVisibility(View.VISIBLE);

        mCommentLayout = (RelativeLayout) rootView.findViewById(R.id.ll_edit);
        mCommentView = (EditText) rootView.findViewById(R.id.ed_comment);
        mAdjustPanView = (SizeChangeCallbackView) rootView.findViewById(R.id.sc_adjustpan);
        mClassCircleRecycleView = (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mClassCircleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassCircleAdapter = new ClassCircleAdapter(getContext());
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);
    }

    private void listener() {
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
        mClassCircleAdapter.setCommentClickListener(ClassCircleFragment.this);
        mFunctionView.setOnClickListener(ClassCircleFragment.this);
        mFunctionView.setOnLongClickListener(ClassCircleFragment.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.parseColor("#e6ffffff"));
        mTitleView.setText(R.string.classcircle);
        mFunctionView.setBackgroundResource(R.mipmap.ic_launcher);

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
        switch (v.getId()) {
            case R.id.title_layout_right_img:
                break;
        }

    }

    @Override
    public void commentClick(final int position, ClassCircleMock mock, ClassCircleMock.Comment comment, boolean isCommentMaster) {
        Toast.makeText(getContext(), mock.userId, Toast.LENGTH_SHORT).show();

        Logger.d("onSizeChanged","commentClick");

        mCommentLayout.setVisibility(View.VISIBLE);
        mCommentView.setFocusable(true);
        mCommentView.clearFocus();
        mCommentView.requestFocus();
        if (mVisibility==View.VISIBLE){
            setScroll(position,mHeight);
        }
        mAdjustPanView.setViewSizeChangedCallback(new SizeChangeCallbackView.onViewSizeChangedCallback() {
            @Override
            public void sizeChanged(int visibility, int height) {

                Logger.d("onSizeChanged","visibility  "+visibility);
                mVisibility=visibility;
                if (visibility == View.VISIBLE) {
                    mHeight=height;
                    ((MainActivity) getActivity()).setBottomVisibility(View.GONE);
                    setScroll(position,height);
                } else {
                    ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mCommentView,0);
    }

    private void setScroll(final int position, final int height){
        Logger.d("mClassCircleRecycleView","adapter  position  "+position);
        mClassCircleRecycleView.scrollToPosition(position);
//        ((LinearLayoutManager)mClassCircleRecycleView.getLayoutManager()).scrollToPositionWithOffset(position,0);
        ClassCircleTimeUtils.creat().start(new ClassCircleTimeUtils.onTimeUplistener() {
            @Override
            public void onTimeUp() {
                setSrcollBy(position, height);
            }
        });
    }

    private void setSrcollBy(final int position, final int height){
        int visibleIndex=((LinearLayoutManager)mClassCircleRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        int n = position - visibleIndex;
        Logger.d("mClassCircleRecycleView","visibile position  "+visibleIndex);
        Logger.d("mClassCircleRecycleView","position  "+n);
        if ( 0 <= n && n < mClassCircleRecycleView.getChildCount()) {
            int top = mClassCircleRecycleView.getChildAt(n).getTop();
            int bottom=mClassCircleRecycleView.getChildAt(n).getBottom();

            Logger.d("mClassCircleRecycleView","top "+top);
            Logger.d("mClassCircleRecycleView","bottom "+bottom);
            Logger.d("mClassCircleRecycleView","height "+height);

            final int heightMove=bottom-height;
            if (heightMove!=0&&bottom!=height) {
                Logger.d("mClassCircleRecycleView","heightMoves "+heightMove);
                mClassCircleRecycleView.post(new Runnable() {
                    @Override
                    public void run() {
                        mClassCircleRecycleView.scrollBy(0, heightMove);
                    }
                });
            }
        }
        Logger.d("mClassCircleRecycleView"," ");
    }

    @Override
    public void commentFinish() {
        mVisibility=View.INVISIBLE;
        mAdjustPanView.setViewSizeChangedCallback(null);
        mCommentLayout.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
        ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

}
