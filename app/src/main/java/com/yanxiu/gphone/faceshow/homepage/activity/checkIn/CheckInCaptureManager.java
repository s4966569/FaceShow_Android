package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yanxiu.gphone.faceshow.FaceShowApplication;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;

/**
 * Created by frc on 17-9-27.
 */

public class CheckInCaptureManager extends CaptureManager {

    private Activity activity;

    public CheckInCaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        super(activity, barcodeView);
        this.activity = activity;
    }

    private CodeCallBack codeCallBack;

    @Override
    protected void returnResultTimeout() {
//        super.returnResultTimeout();
    }

    @Override
    protected void returnResult(BarcodeResult rawResult) {
        if (codeCallBack != null)
            codeCallBack.callBack(rawResult.getResult().getText().toString());
//        super.returnResult(rawResult);


    }

    public interface CodeCallBack {
        void callBack(String result);
    }

    public void setCodeCallBack(CodeCallBack codeCallBack) {
        this.codeCallBack = codeCallBack;
    }

    @Override
    protected void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("权限异常");
        builder.setMessage("请前往设置开启照相机权限！");
        builder.setPositiveButton(com.google.zxing.client.android.R.string.zxing_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                activity.finish();
            }
        });
        builder.show();
    }
}
