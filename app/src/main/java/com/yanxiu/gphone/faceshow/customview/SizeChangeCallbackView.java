package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 17:21.
 * Function :
 */
public class SizeChangeCallbackView extends View {

    private onViewSizeChangedCallback mViewSizeChangedCallback;

    public interface onViewSizeChangedCallback{
        void sizeChanged(int visibility);
    }

    public SizeChangeCallbackView(Context context) {
        super(context);
    }

    public SizeChangeCallbackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeChangeCallbackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewSizeChangedCallback(onViewSizeChangedCallback viewSizeChangedCallback){
        this.mViewSizeChangedCallback=viewSizeChangedCallback;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int visibility;
        if (h>oldh){
            visibility=INVISIBLE;
        }else {
            visibility=VISIBLE;
        }
        if (mViewSizeChangedCallback!=null){
            mViewSizeChangedCallback.sizeChanged(visibility);
        }
    }
}
