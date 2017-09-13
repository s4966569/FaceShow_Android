package com.yanxiu.gphone.faceshow.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by sunpeng on 2017/5/25.
 */

public class FileUtil {

    public static String getDataFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
