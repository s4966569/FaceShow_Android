package com.test.yanxiu.im_ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cailei on 09/03/2018.
 */

public class RoundCornerImageView extends AppCompatImageView {
    private float width,height;

    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > 28 && height > 28) {
            Path path = new Path();
            path.moveTo(60, 0);
            path.lineTo(width - 60, 0);
            path.quadTo(width, 0, width, 60);
            path.lineTo(width, height - 60);
            path.quadTo(width, height, width - 60, height);
            path.lineTo(60, height);
            path.quadTo(0, height, 0, height - 60);
            path.lineTo(0, 60);
            path.quadTo(0, 0, 60, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
