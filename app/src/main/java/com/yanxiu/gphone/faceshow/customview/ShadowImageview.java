package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/28 12:00.
 * Function :
 */
public class ShadowImageview extends ImageView {


    public ShadowImageview(Context context) {
        super(context);
    }

    public ShadowImageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadowImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Paint paint= new Paint();
//        paint.setAntiAlias(true);
//        paint.setShadowLayer(10f, 0, 10.0f, Color.parseColor("#cc000000")); //设置阴影层，这是关键。
//        Rect rect=new Rect();
//        canvas.getClipBounds(rect);
//        canvas.drawRect(rect,paint);
    }
}
