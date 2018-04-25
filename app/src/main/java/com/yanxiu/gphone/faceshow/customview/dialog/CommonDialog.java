package com.yanxiu.gphone.faceshow.customview.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.test.yanxiu.common_base.utils.ScreenUtils;
import com.yanxiu.gphone.faceshow.R;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * 一般通用dialog
 * 2018年4月4日
 * create by dyf
 */
public class CommonDialog extends CustomBaseDialog {

    private Context mContext;

    private View mConfirm, mCancel;

    private TextView mTitleTextView, mContentTextView;

    public CommonDialog(Context context) {
        this(context, 0);
    }

    public CommonDialog(Context context, int theme) {
        super(context, 0);
        mContext = context;
        initView();
    }

    @Override
    public void initListener() {
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_commondialog_layout, null);
        setContentView(mRootView);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth(mContext) * (1 - 0.28));
        lp.topMargin = (ScreenUtils.getScreenHeight(mContext) - ScreenUtils.getStatusBarHeight(mContext)) / 2 - mContext.getResources().getDimensionPixelSize(R.dimen.cancelDialog_height) / 2;
        lp.gravity = CENTER_HORIZONTAL;
        mRootView.setLayoutParams(lp);
        mConfirm = mRootView.findViewById(R.id.baseDialog_confirm);
        mCancel = mRootView.findViewById(R.id.baseDialog_cancel);
        mContentTextView = mRootView.findViewById(R.id.baseDialog_content);
        mTitleTextView = mRootView.findViewById(R.id.baseDialog_title);
        initListener();
    }

    /**
     * 设置dialog的标题文字
     *
     * @param titleTextRes
     */
    public void setTitleText(@StringRes int titleTextRes) {
        mTitleTextView.setText(titleTextRes);
        mTitleTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置dialog的内容文字
     *
     * @param contentTextRes
     */
    public void setContentText(@StringRes int contentTextRes) {
        mContentTextView.setText(contentTextRes);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.baseDialog_confirm:
                dismiss();
                mCustomDialogOnClickListener.customDialogConfirm();
                break;
            case R.id.baseDialog_cancel:
                dismiss();
                mCustomDialogOnClickListener.customDialogCancel();
                break;
        }

    }
}