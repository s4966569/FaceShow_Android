package com.yanxiu.gphone.faceshow.classcircle.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.yanxiu.gphone.faceshow.R;

/**
 * Created by Canghaixiao.
 * Time : 2017/9/15 12:15.
 * Function :
 */
public class ClassCirclePopupWindow {

    private PopupWindow mPopupWindow;


    public ClassCirclePopupWindow creat(Context context){
        return new ClassCirclePopupWindow(context);
    }

    private ClassCirclePopupWindow(Context context){
        mPopupWindow=new PopupWindow(context);
        View view= LayoutInflater.from(context).inflate(R.layout.popupwindow_classcircle,null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setOutsideTouchable(true);
    }
}
