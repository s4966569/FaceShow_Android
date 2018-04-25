package com.yanxiu.gphone.faceshow.customview.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanxiu.gphone.faceshow.R;


/**
 * 基类dialog
 * 2018/4/4
 * create by dyf
 */
public abstract class CustomBaseDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    protected View mRootView;//根view

    public CustomDialogOnClickListener mCustomDialogOnClickListener;

    public CustomBaseDialog(Context context) {
        this(context, R.style.BaseDialog);
    }

    public CustomBaseDialog(Context context, int theme) {
        super(context, R.style.BaseDialog);
        mContext = context;
    }

    private CustomBaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(int layoutResID) {
        mRootView = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view) {
        mRootView = view;
        super.setContentView(mRootView);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mRootView = view;
        super.setContentView(mRootView, params);
    }

    public void setOnClickListener(CustomDialogOnClickListener listener) {
        mCustomDialogOnClickListener = listener;
        initListener();
    }

    public abstract void initListener();

    public interface CustomDialogOnClickListener {
        /**
         * 确定
         */
        void customDialogConfirm();

        /**
         * 取消
         */
        void customDialogCancel();
    }

}