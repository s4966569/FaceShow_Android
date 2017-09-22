package com.yanxiu.gphone.faceshow.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.yanxiu.gphone.faceshow.R;


/**
 * Created by Administrator on 2016/7/13.
 */
public class LoadingDialogView {

    private final Animation animation;
    private ImageView image;
    private Dialog dialog;

    public LoadingDialogView(Context context) {
        dialog = new Dialog(context, R.style.Loading);
        View layout = LayoutInflater.from(context).inflate(R.layout.loading_layout, null);
        image = (ImageView) layout.findViewById(R.id.commonloadingView);
        animation = AnimationUtils.loadAnimation(context, R.anim.lodingview_progress);
        LinearInterpolator interpolator = new LinearInterpolator();
        animation.setInterpolator(interpolator);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = 160;
        lp.height = 160;
//        lp.alpha=1f;
        window.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        if (dialog != null)
            dialog.show();
        image.startAnimation(animation);
    }

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

}
