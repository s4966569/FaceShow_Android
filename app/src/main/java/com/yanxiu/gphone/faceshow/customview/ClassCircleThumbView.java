package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleThumbView extends android.support.v7.widget.AppCompatTextView {
    public ClassCircleThumbView(Context context) {
        this(context,null);
    }

    public ClassCircleThumbView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassCircleThumbView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
    }

}
