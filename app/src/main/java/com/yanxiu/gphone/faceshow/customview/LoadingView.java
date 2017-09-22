package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.yanxiu.gphone.faceshow.R;


/**
 * loadigView
 * Created by 戴延枫 on 2017/8/8.
 */

public class  LoadingView extends FrameLayout {

    private Context mContext;
    private ImageView mLoadingView;// loadingView
    private Animation mLoadingAnim;//loadingView动画

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化view
     */
    private void initView(Context context) {
        mContext = context;
        inflate(context, R.layout.loading_layout, this);
        initLoadingView();
    }

    /**
     * 初始化加载动画布局
     */
    private void initLoadingView() {
        mLoadingView = (ImageView) findViewById(R.id.commonloadingView);
        mLoadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.lodingview_progress);
        LinearInterpolator lin = new LinearInterpolator();
        mLoadingAnim.setInterpolator(lin);
        setVisibility(GONE);
    }

    /**
     * 显示loadingView
     */
    public void showLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            setVisibility(View.VISIBLE);
            mLoadingView.clearAnimation();
            mLoadingView.startAnimation(mLoadingAnim);
        }
    }

    /**
     * 隐藏LoadingView
     */
    public void hiddenLoadingView() {
        if (mLoadingView != null && mLoadingAnim != null) {
            mLoadingView.clearAnimation();
            setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏异常界面和lodingView，展示正常界面
     */
    public void finish() {
        hiddenLoadingView();
    }

}
