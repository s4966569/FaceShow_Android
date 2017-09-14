package com.yanxiu.gphone.faceshow.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanxiu.gphone.faceshow.FaceShowApplication;

/**
 * Created by frc on 17-7-5.
 */

public class SharePreferenceManager {
    private static Context context;
    private static final String PEIXUNTONG = "pei_xun_tong";
    /*用户是否已经登录成功*/
    private static final String IS_LOGINED = "is_login";
    /**
     * 第一次启动
     */
    private static final String FRIST_START_UP = "frist_start_up";

    private static SharePreferenceManager instance;
    private static SharedPreferences sp;

    private SharePreferenceManager(Context context) {
        this.context = context;
    }

    public static SharePreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharePreferenceManager(context);
            sp = context.getSharedPreferences(PEIXUNTONG, Context.MODE_PRIVATE);
        }
        return instance;
    }

    /**
     * 是否已经登录
     *
     * @return false:未登录   true :登录
     */
    public boolean isLogined() {
        return sp.getBoolean(IS_LOGINED, false);
    }


    /**
     * 设置为登录状态
     */
    public void haveSignIn() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_LOGINED, true);
        editor.commit();
    }


    public static void setFristStartUp(boolean isFristStartUp) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(FRIST_START_UP, isFristStartUp);
        editor.commit();
    }

    /**
     * 是否第一次启动
     *
     * @return true ： 第一次
     */
    public static boolean isFristStartUp() {
        return sp.getBoolean(FRIST_START_UP, true);
    }

}
