package com.yanxiu.gphone.faceshow.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.yanxiu.gphone.faceshow.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/26 17:40.
 * Function :
 */
public class SystemUpdataDialog {

    interface UpdateDialogCallBack {
        void update();

        void cancel();

        void exit();
    }

    private static final String UPDATETYPE_MANDATORY = "1";
    private static final String UPDATETYPE_UNMANDATORY = "2";

    private Context mContext;

    private String mUpdateType;
    private UpdateDialogCallBack mCallBack;

    private final AlertDialog.Builder dialog;
    private final AlertDialog alertDialog;

    public SystemUpdataDialog(@NonNull Context context, String title,String content, String updateType, UpdateDialogCallBack callBack) {

        this.mContext = context;
        this.mUpdateType = updateType;
        this.mCallBack = callBack;

        dialog = new AlertDialog.Builder(context);
        dialog.setMessage(content);
        dialog.setTitle(title);
        dialog.setCancelable(false);
        setListener();
        alertDialog = dialog.create();
        alertDialog.setOwnerActivity((Activity) mContext);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

    }

    private void setListener() {
        if (mUpdateType.equals(UPDATETYPE_MANDATORY)) {
            dialog.setPositiveButton(mContext.getResources().getText(R.string.app_update_exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack != null) {
                        mCallBack.exit();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton(mContext.getResources().getText(R.string.updata_now), null);
        } else if (mUpdateType.equals(UPDATETYPE_UNMANDATORY)) {
            dialog.setPositiveButton(mContext.getResources().getText(R.string.updata_after), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack != null) {
                        mCallBack.cancel();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton(mContext.getResources().getText(R.string.updata_now), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mCallBack != null) {
                        ToastUtil.showToast(mContext, "正在下载,请等待");
//                        mPbLoadApkView.setVisibility(View.VISIBLE);
                        mCallBack.update();
                    }
                    dismiss();
                }
            });
        }
    }

    public void setTitles(String title, String version) {
    }

    public void setContent(String content) {
//        dialog.setMessage(content);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public void show() {
        alertDialog.show();
        if (mUpdateType.equals(UPDATETYPE_MANDATORY)) {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.showToast(view.getContext(), "正在下载,请等待");
                    if (mCallBack != null) {
//                        mPbLoadApkView.setVisibility(View.VISIBLE);
                        mCallBack.update();
                    }
                }
            });
        }
    }

    public void setProgress(int progress) {
//        if (mPbLoadApkView!=null) {
//            mPbLoadApkView.setProgress(progress);
//        }
    }


}
