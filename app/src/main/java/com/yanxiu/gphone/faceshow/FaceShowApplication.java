package com.yanxiu.gphone.faceshow;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.yanxiu.gphone.faceshow.constant.Constants;
import com.yanxiu.gphone.faceshow.getui.FaceShowGeTuiIntentService;
import com.yanxiu.gphone.faceshow.getui.FaceShowGeTuiService;
import com.yanxiu.gphone.faceshow.http.envconfig.EnvConfigBean;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlBean;
import com.yanxiu.gphone.faceshow.http.envconfig.UrlRepository;
import com.yanxiu.gphone.faceshow.util.CrashHandler;
import com.yanxiu.gphone.faceshow.util.FileUtil;

import org.litepal.LitePalApplication;

public class FaceShowApplication extends LitePalApplication {
    private static FaceShowApplication instance;

    public static FaceShowApplication getInstance() {
        return instance;
    }


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error error) {
            error.printStackTrace();
        }

        instance = this;
        initUrlServer();
        Stetho.initializeWithDefaults(this);
        CrashHandler.getInstance().init(this);
        /*个推初始化*/
        PushManager.getInstance().initialize(getApplicationContext(), FaceShowGeTuiService.class);
        // FaceShowGeTuiIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), FaceShowGeTuiIntentService.class);
    }

    private void initUrlServer() {
        UrlBean urlBean;
        Gson gson = new Gson();
        String urlJson = FileUtil.getDataFromAssets(this, Constants.URL_SERVER_FILE_NAME);
        if (urlJson.contains(Constants.MULTICONFIG)) {
            EnvConfigBean envConfigBean = gson.fromJson(urlJson, EnvConfigBean.class);
            urlBean = envConfigBean.getData().get(envConfigBean.getCurrentIndex());
        } else {
            urlBean = gson.fromJson(urlJson, UrlBean.class);
        }
        UrlRepository.getInstance().setUrlBean(urlBean);
    }

}
