package com.yanxiu.gphone.faceshow.qrsignup.base;

import android.os.Bundle;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.customview.LoadingDialogView;
import com.yanxiu.gphone.faceshow.homepage.activity.checkIn.CheckInCaptureManager;

/**
 * 专职于 二维码扫描的界面
 * 由于 前版本的 扫码功能 集中于 扫码签到
 * 这里只完成扫码功能
 * */
public abstract class PublicQRScanActivity extends FaceShowBaseActivity {

    /**
     * 条形码扫描管理器
     */
    private CheckInCaptureManager mCaptureManager;
    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;
    private LoadingDialogView mLoadingDialogView;
    /**
     * 扫码 回调
     * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_layout);

    }



    /**
     * 抽象方法  检查扫描的QRcode 是否符合预期
     * 这里通过不同的qr规则来区分
     *
     * */
    protected abstract boolean checkQRCode(String code);
    /**
     * 抽象方法 回调扫码结果
     * 根据不同的使用情况 执行不同的操作流程
     *
     * */
    protected abstract void scanResult(String code,boolean success);
}
