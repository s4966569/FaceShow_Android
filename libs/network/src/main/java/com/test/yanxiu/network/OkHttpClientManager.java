package com.test.yanxiu.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by sunpeng on 2017/5/9.
 */

public class OkHttpClientManager {
    private static OkHttpClient mInstance;

    public static OkHttpClient getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClient.Builder()
                            .readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .addNetworkInterceptor(new StethoInterceptor())
                            .build();
                }
            }
        }
        return mInstance;
    }
}
