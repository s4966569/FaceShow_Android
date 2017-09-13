package com.yanxiu.gphone.faceshow.util;

import android.graphics.Typeface;
import android.widget.TextView;

import com.yanxiu.gphone.faceshow.FaceShowApplication;

/**
 * Created by 戴延枫 on 2017/5/26.
 */

public class TextTypefaceUtil {
    private static final String PATH_TYPEFACE = "fonts/";

    public enum TypefaceType {
        //        FANGZHENG(PATH_TYPEFACE + "metor_bold.OTF"),
//        METRO_BOLD(PATH_TYPEFACE + "metor_bold.OTF"),
//        ARAL_ROUNDED_BOLD(PATH_TYPEFACE + "arial_runded_bold.ttf"),
        METRO_PLAY(PATH_TYPEFACE + "metroplaytype.otf"),
        METRO_BOLDITALIC(PATH_TYPEFACE + "metrobolditalic.OTF"),
        METRO_MEDIUM_PLAY(PATH_TYPEFACE + "metromedium.OTF"),
        METRO_DEMI_BOLD(PATH_TYPEFACE + "metrodemibold.OTF");
        public String path;

        TypefaceType(String path) {
            this.path = path;
        }
    }

    /**
     * 设置字体
     */
    public static void setViewTypeface(TextTypefaceUtil.TypefaceType typefaceType, TextView... view) {
        Typeface tf = Typeface.createFromAsset(FaceShowApplication.getInstance().getAssets(),
                typefaceType.path);
        for (int i = 0; i < view.length; i++) {
            view[i].setTypeface(tf);
        }
    }
}
