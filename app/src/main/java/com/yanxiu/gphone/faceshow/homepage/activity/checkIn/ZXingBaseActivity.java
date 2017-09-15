package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.util.zxing.view.ViewfinderView;

public abstract class ZXingBaseActivity extends FaceShowBaseActivity {
    public abstract ViewfinderView getViewfinderView();

    public abstract Handler getHandler();

    public abstract void handleDecode(Result result, Bitmap barcode);

    public abstract void drawViewfinder();
}
