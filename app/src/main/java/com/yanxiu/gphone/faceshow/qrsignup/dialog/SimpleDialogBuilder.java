package com.yanxiu.gphone.faceshow.qrsignup.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by srt on 2018/3/2.
 *
 * 用于创建统一的dialog对话框
 * 目前只应用于 扫码注册界面
 */

public class SimpleDialogBuilder {
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public void SimpleDialogBuilder(Context context){
        builder=new AlertDialog.Builder(context);
        alertDialog=builder.create();
    }


    public void setContent(String content){
        alertDialog.setMessage(content);

    }

//    public void enablePositiveBtn(String text,){
//        builder.setPositiveButton(text,)
//    }

    public void setPositiveClickListener(){

    }







}
