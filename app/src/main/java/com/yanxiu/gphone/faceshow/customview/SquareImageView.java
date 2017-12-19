package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Canghaixiao.
 * Time : 2017/6/23 10:12.
 * Function :
 */
public class SquareImageView extends android.support.v7.widget.AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w!=h){
            getLayoutParams().width=w;
            getLayoutParams().height=w;
            setLayoutParams(getLayoutParams());
        }
    }
}
