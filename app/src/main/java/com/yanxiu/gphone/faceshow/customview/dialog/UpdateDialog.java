package com.yanxiu.gphone.faceshow.customview.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.util.ScreenUtils;
import com.yanxiu.gphone.faceshow.util.update.SystemUpdataDialog;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * 升级的dialog
 * 2018年4月4日
 * create by dyf
 */
public class UpdateDialog extends CustomBaseDialog {

    private static final String UPDATETYPE_MANDATORY = "1";
    private static final String UPDATETYPE_UNMANDATORY = "2";

    private Context mContext;
    private UpdateDialogCallBack mCallBack;

    private View mProgressLayout;//
    private View mContentLayout;//
    private TextView mConfirm, mCancel;
    private TextView mTitleTextView, mContentTextView;
    private TextView mProgressTv;//显示文字进度--50%
    private ProgressBar mProgressBar;

    private String mUpdateType;

    public UpdateDialog(Context context, String updateType, UpdateDialogCallBack callBack) {
        super(context, 0);
        mContext = context;
        mUpdateType = updateType;
        mCallBack = callBack;
        initView();
    }

    @Override
    public void initListener() {
        mConfirm.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    private void initView() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_updatedialog_layout, null);
        setContentView(mRootView);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
        lp.width = (int) (ScreenUtils.getScreenWidth(mContext) * (1 - 0.28));
        lp.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        lp.topMargin = (ScreenUtils.getScreenHeight(mContext) - ScreenUtils.getStatusBarHeight(mContext)) / 2 - mContext.getResources().getDimensionPixelSize(R.dimen.cancelDialog_height) / 2;
        lp.gravity = CENTER_HORIZONTAL;
        mRootView.setLayoutParams(lp);

        mContentLayout = mRootView.findViewById(R.id.updateDialog_contentLayout);
        mProgressLayout = mRootView.findViewById(R.id.updateDialog_progressLayout);
        mConfirm = mRootView.findViewById(R.id.updateDialog_confirm);
        mCancel = mRootView.findViewById(R.id.updateDialog_cancel);
        mContentTextView = mRootView.findViewById(R.id.updateDialog_content);
        mTitleTextView = mRootView.findViewById(R.id.updateDialog_title);

        mProgressTv = mRootView.findViewById(R.id.updateDialog_progress_tv);
        mProgressBar = mRootView.findViewById(R.id.updateDialog_progressBar);
        mProgressBar.setMax(100);
        if (!TextUtils.isEmpty(mUpdateType)) {
            if (mUpdateType.equals(UPDATETYPE_MANDATORY)) { //强制升级
                mCancel.setText(R.string.app_update_exit);
                setCanceledOnTouchOutside(false);
                setCancelable(false);
            } else if (mUpdateType.equals(UPDATETYPE_UNMANDATORY)) { //非强制升级
                mCancel.setText(R.string.updata_after);
            } else {

            }
        }
        initListener();
    }

    /**
     * 设置dialog的标题文字
     */
    public void setTitleText(String titleText) {
        mTitleTextView.setText(titleText);
        mTitleTextView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置dialog的内容文字
     */
    public void setContentText(String contentText) {
        mContentTextView.setText(contentText);
    }

    /**
     * 跟新进度
     */
    public void setProgress(int progress) {
        mContentLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressTv.setText(progress + "%");
        mProgressBar.setProgress(progress);
        if (progress >= 100)
            dismiss();
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateDialog_confirm:
                if (mCallBack != null) {
                    mCallBack.update();
                }
                break;
            case R.id.updateDialog_cancel:
                if (mCallBack != null) {
                    switch (mUpdateType) {
                        case UPDATETYPE_MANDATORY:
                            mCallBack.exit();
                            break;
                        case UPDATETYPE_UNMANDATORY:
                            mCallBack.cancel();
                            break;
                    }
                }
                dismiss();
                break;
        }
    }

    /**
     * 升级dialog的点击回调
     */
    public interface UpdateDialogCallBack {
        void update();

        void cancel();

        void exit();
    }

}