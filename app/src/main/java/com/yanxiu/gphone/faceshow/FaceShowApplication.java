package com.yanxiu.gphone.faceshow;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.yanxiu.gphone.faceshow.constant.Constants;
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


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUrlServer();
        Stetho.initializeWithDefaults(this);
        CrashHandler.getInstance().init(this);
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
