package com.yanxiu.gphone.faceshow.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.yanxiu.gphone.faceshow.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 11:39.
 * Function :
 */
public class ClassCircleCommentLayout extends RelativeLayout {
    public ClassCircleCommentLayout(Context context) {
        this(context,null);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClassCircleCommentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.layout_class_circle_comment,this);
    }

}
