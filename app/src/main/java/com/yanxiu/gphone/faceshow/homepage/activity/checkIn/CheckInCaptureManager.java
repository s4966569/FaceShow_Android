package com.yanxiu.gphone.faceshow.homepage.activity.checkIn;

import android.app.Activity;

import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by frc on 17-9-27.
 */

public class CheckInCaptureManager extends CaptureManager {
    public CheckInCaptureManager(Activity activity, DecoratedBarcodeView barcodeView) {
        super(activity, barcodeView);
    }

    private CodeCallBack codeCallBack;

    @Override
    protected void returnResultTimeout() {
//        super.returnResultTimeout();
    }

    @Override
    protected void returnResult(BarcodeResult rawResult) {
        if (codeCallBack!=null)
            codeCallBack.callBack(rawResult.getResult().getText().toString());
//        super.returnResult(rawResult);


    }

    public interface CodeCallBack {
        void callBack(String result);
    }

    public void setCodeCallBack(CodeCallBack codeCallBack) {
        this.codeCallBack = codeCallBack;
    }
}
