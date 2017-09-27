package com.yanxiu.gphone.faceshow.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/26 17:40.
 * Function :
 */
public class SystemUpdataDialog{

    interface UpdateDialogCallBack{
        void update();
        void cancel();
        void exit();
    }

    private static final String UPDATETYPE_MANDATORY="1";
    private static final String UPDATETYPE_UNMANDATORY="2";

    private Context mContext;

    private String mUpdateType;
    private UpdateDialogCallBack mCallBack;

    private final AlertDialog.Builder dialog;
    private final AlertDialog alertDialog;

    public SystemUpdataDialog(@NonNull Context context,String content,String updateType,UpdateDialogCallBack callBack) {
        this.mContext=context;
        this.mUpdateType=updateType;
        this.mCallBack=callBack;

        dialog=new AlertDialog.Builder(context);
        dialog.setMessage(content);
        setListener();
        alertDialog=dialog.create();
        alertDialog.setOwnerActivity((Activity) mContext);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
    }

    private void setListener(){
        if (mUpdateType.equals(UPDATETYPE_MANDATORY)) {
            dialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack!=null){
                        mCallBack.exit();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack!=null){
//                        mPbLoadApkView.setVisibility(View.VISIBLE);
                        mCallBack.update();
                    }
                }
            });
        }else if (mUpdateType.equals(UPDATETYPE_UNMANDATORY)){
            dialog.setPositiveButton("以后再说", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack!=null){
                        mCallBack.cancel();
                    }
                    dialog.dismiss();
                }
            });
            dialog.setNegativeButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallBack!=null){
//                        mPbLoadApkView.setVisibility(View.VISIBLE);
                        mCallBack.update();
                    }
                }
            });
        }
    }

    public void setTitles(String title,String version){
    }

    public void setContent(String content){
//        dialog.setMessage(content);
    }

    public void dismiss(){
        alertDialog.dismiss();
    }

    public void show(){
        alertDialog.show();
    }

    public void setProgress(int progress){
//        if (mPbLoadApkView!=null) {
//            mPbLoadApkView.setProgress(progress);
//        }
    }

}
