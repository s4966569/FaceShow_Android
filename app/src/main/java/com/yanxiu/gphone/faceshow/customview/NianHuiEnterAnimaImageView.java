package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yanxiu.gphone.faceshow.R;

/**
 * @author frc on 2018/1/31.
 */

public class NianHuiEnterAnimaImageView extends android.support.v7.widget.AppCompatImageView {
    public NianHuiEnterAnimaImageView(Context context) {
        this(context, null);
    }

    public NianHuiEnterAnimaImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NianHuiEnterAnimaImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setImageResource(R.drawable.anima_nian_hui_enter);
        ((Animatable) getDrawable()).start();
    }
}
