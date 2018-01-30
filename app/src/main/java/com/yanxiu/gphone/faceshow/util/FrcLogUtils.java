package com.yanxiu.gphone.faceshow.util;

import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * @author frc on 2018/1/27.
 */

public class FrcLogUtils {
    private static final String TAG_NAME = "FRC";
    private static final boolean SHOW_THREAD_INFO = true;
    private static Gson sGson = new Gson();

    private FrcLogUtils() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(SHOW_THREAD_INFO)
                .tag(TAG_NAME)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }

            @Override
            public void log(int priority, String tag, String message) {
                super.log(priority, tag, message);
            }
        });
    }

    public static void init() {
        new FrcLogUtils();
    }

    public static void logBean(Object object) {
        Logger.e(sGson.toJson(object));
    }

    public static void logD(Object object) {
        Logger.d(object);
    }

    public static void logW(String string) {
        Logger.w(string);
    }

    public static void logE(String string) {
        Logger.e(string);
    }

    public static void logI(String string) {
        Logger.i(string);
    }

    public static void logJson(String json) {
        Logger.json(json);
    }


}
