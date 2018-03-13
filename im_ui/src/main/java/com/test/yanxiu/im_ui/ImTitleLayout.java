package com.test.yanxiu.im_ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by cailei on 08/03/2018.
 */

public class ImTitleLayout extends RelativeLayout {
    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setLeftView(View v) {
        makeViewIndependence(v);
        mLeftView.addView(v);
    }

    public void setRightView(View v) {
        makeViewIndependence(v);
        mRightView.addView(v);
    }

    private void makeViewIndependence(View v) {
        ViewGroup vg = (ViewGroup) v.getParent();
        if (vg != null) {
            vg.removeView(v);
            v.setVisibility(View.VISIBLE);
        }
    }

    private TextView mTitleTextView;
    private LinearLayout mLeftView;
    private LinearLayout mRightView;

    public ImTitleLayout(Context context) {
        super(context);
        setup(context);
    }

    public ImTitleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public ImTitleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    private void setup(Context context) {
        inflate(context, R.layout.layout_im_title, this);
        mTitleTextView = findViewById(R.id.title_textview);
        mLeftView = findViewById(R.id.left_view);
        mRightView = findViewById(R.id.right_view);
    }
}
