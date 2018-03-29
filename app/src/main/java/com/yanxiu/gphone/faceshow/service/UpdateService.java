package com.yanxiu.gphone.faceshow.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.util.FrcLogUtils;
import com.yanxiu.gphone.faceshow.util.update.UpdateUtil;

import java.io.File;

/**
 * 升级的服务
 */
public class UpdateService extends Service {

    public static final String TAG = "UpdateService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand");
        if (intent != null) {
            UpdateUtil.Initialize(getApplicationContext(), false, new UpdateUtil.OnUpdateFinishCallBack() {
                @Override
                public void onUpdateFinish(String state) {
                    Log.e(TAG,"stopSelf = "+state);
                    stopSelf();
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy");
        super.onDestroy();
    }
}
