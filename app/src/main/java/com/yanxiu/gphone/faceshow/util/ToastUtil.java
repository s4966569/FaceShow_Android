package com.yanxiu.gphone.faceshow.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, int id) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }

    public static void showLongToast(Context context, int id) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), Toast.LENGTH_LONG);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }

    public static void showToast(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToast(Context context, int id, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, context.getString(id), duration);
        } else {
            toast.setText(context.getString(id));
        }
        toast.show();
    }
}
