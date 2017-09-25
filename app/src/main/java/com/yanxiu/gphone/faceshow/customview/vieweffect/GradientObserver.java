package com.yanxiu.gphone.faceshow.customview.vieweffect;

import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

/**
 * 渐变监听器
 * Created by dyf on 2017/7/27.
 */
public class GradientObserver {
    /*是否完成初始化*/
    private boolean isInitial = false;
    /*滑动容器*/
    private View container = null;
    /*渐变效果集合*/
    private ArrayList<GradientEffect> effects = new ArrayList<GradientEffect>();

    public GradientObserver(View container) {
        this.container = container;
    }

    /**
     * 添加渐变效果
     *
     * @param e 渐变效果实现类
     */
    public void addGradientEffect(GradientEffect e) {
        if (!effects.contains(e))
            effects.add(e);
        if (isInitial || container == null || effects.isEmpty())
            return;
        init();
    }

    public void removeGradientEffect(GradientEffect e) {
        if (effects != null && effects.contains(e)) {
            effects.remove(e);
        }
    }

    /**
     * 如果添加了渐变效果则初始化监听器
     */
    private void init() {
        isInitial = true;
        ViewTreeObserver viewTreeObserver = container.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onInitData();
            }
        });
        viewTreeObserver.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                generateRatio();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void onInitData() {
        for (GradientEffect e : effects) {
            if (e == null) continue;
            e.onInitData();
        }
    }

    /**
     * 生成渐变系数
     */
    private void generateRatio() {
        for (GradientEffect e : effects) {
            if (e == null) continue;
            e.generateRatio();
        }
    }
}
