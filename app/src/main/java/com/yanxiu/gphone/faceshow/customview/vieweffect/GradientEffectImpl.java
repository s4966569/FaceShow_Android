package com.yanxiu.gphone.faceshow.customview.vieweffect;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 可用于实现Title渐变效果或者吸顶效果
 * <p/>
 * 初始时，滑动视图必须在悬浮视图的下方
 * Created by dyf on 2017/7/27.
 */
public class GradientEffectImpl implements GradientEffect {
    /*渐变效果监听器*/
    private OnGradientEffectListener mOnGradientEffectListener;
    /*悬浮视图*/
    private View hoverView;
    /*滑动视图*/
    private View sliderView;
    /*悬浮视图初始Y坐标*/
    private int initialHoverViewY = -1;
    /*滑动视图初始Y坐标*/
    private int initialSliderViewY = -1;
    /*悬浮视图高度*/
    private int hoverViewHeight = -1;
    /*滑动视图高度*/
    private int sliderViewHeight = -1;
    /*初始差值，如果hoverView或sliderView高度改变，随之改变*/
    private float initialDValue = -1;
    /*渐变系数*/
    private float mRatio = -1;
    /*是否完成初始化*/
    private boolean isInitial = false;
    private GradientObserver gradientObserver;

    /**
     * 初始时，sliderView必须在hoverView的下方
     *
     * @param hoverView  悬浮视图
     * @param sliderView 滑动视图
     * @viewContainer sliderView 滑动容器，包裹着悬浮视图（hoverView）和滑动视图（sliderView）
     */
    public GradientEffectImpl(View hoverView, View sliderView, View viewContainer) {
        this.hoverView = hoverView;
        this.sliderView = sliderView;
        //添加容器监听器，用于对容器的滚动进行监听，获取generateRatio的回调
        gradientObserver = new GradientObserver(viewContainer);
        gradientObserver.addGradientEffect(this);
    }

    /**
     * 建议在滑动视图容器的onAttachedToWindow方法中调用，用于初始化数据
     */
    @Override
    public void onInitData() {
        if (isInitial) return;
        isInitial = true;
        hoverView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int tempHoverViewHeight = hoverView.getHeight();
                if (tempHoverViewHeight == hoverViewHeight) return;
                if (tempHoverViewHeight > 0 && initialHoverViewY == -1) {
                    initialHoverViewY = getScreenY(hoverView);
                }
                hoverViewHeight = tempHoverViewHeight;
                tryInitDValue();
            }
        });
        sliderView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int tempSliderViewHeight = sliderView.getHeight();
                if (tempSliderViewHeight == sliderViewHeight) return;
                if (tempSliderViewHeight > 0 && initialSliderViewY == -1) {
                    initialSliderViewY = getScreenY(sliderView);
                }
                sliderViewHeight = tempSliderViewHeight;
                tryInitDValue();
            }
        });

    }

    @Override
    public void generateRatio() {
        float ratio;
        float tempDValue = getCurrentDValue();
        if (tempDValue < 0) {//滑动视图基线在悬浮视图的上方
            ratio = 1;
        } else if (tempDValue > initialDValue) {//滑动视图基线在悬浮视图基线的下方，并且滑动视图越过原位置，继续被向下拉动
            ratio = 0;
        } else {//滑动视图基线在悬浮视图基线的下方,并且被向上拉动
            float offset = initialDValue - tempDValue;
            ratio = offset / initialDValue;
        }
        if (mOnGradientEffectListener != null && mRatio != ratio) {
            mRatio = ratio;
            mOnGradientEffectListener.onGrade(ratio);
        }
    }

    @Override
    public void setOnGradientEffectListener(OnGradientEffectListener l) {
        mOnGradientEffectListener = l;
    }

    /**
     * 尝试去计算初始差值
     */
    private void tryInitDValue() {
        if (hoverViewHeight == -1 || sliderViewHeight == -1 || initialHoverViewY == -1 || initialSliderViewY == -1)
            return;
        initialDValue = getDValue(hoverViewHeight, initialHoverViewY, sliderViewHeight, initialSliderViewY);
    }

    /**
     * 获取当前差值
     *
     * @return
     */
    private int getCurrentDValue() {
        int tempHoverViewY = getScreenY(hoverView);
        int tempSliderViewY = getScreenY(sliderView);
        return getDValue(hoverViewHeight, tempHoverViewY, sliderViewHeight, tempSliderViewY);
    }

    /**
     * 计算差值
     *
     * @param hoverViewHeight  悬浮视图的高度
     * @param hoverViewY       悬浮视图的y坐标
     * @param sliderViewHeight 滑动视图的高度
     * @param sliderViewY      滑动视图的y坐标
     * @return
     */
    private int getDValue(int hoverViewHeight, int hoverViewY, int sliderViewHeight, int sliderViewY) {
        int sliderViewBottom = sliderViewHeight + sliderViewY;//滑动视图当前基线
        int hoverViewBottom = hoverViewHeight + hoverViewY;//悬浮视图当前基线
        return sliderViewBottom - hoverViewBottom;
    }

    /**
     * 获取当前view在屏幕中的y坐标
     *
     * @param view
     * @return
     */
    private int getScreenY(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location[1];
    }

    /**
     * 释放监听，在页面关闭的时候调用
     */
    public void releaseObserver() {
        gradientObserver.removeGradientEffect(this);
        gradientObserver = null;
    }

}
