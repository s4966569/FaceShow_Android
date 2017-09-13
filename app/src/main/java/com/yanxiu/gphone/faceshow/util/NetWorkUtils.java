package com.yanxiu.gphone.faceshow.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.yanxiu.gphone.faceshow.FaceShowApplication;

/**
 * Created by Canghaixiao.
 * Time : 2017/5/17 14:58.
 * Function :
 */
public class NetWorkUtils {
    /**
     * 判断网络是否是可用
     * @return
     */
    public static boolean isNetAvailable() {
        return NetWorkUtils.getAvailableNetWorkInfo() != null;
    }

    public static NetworkInfo getAvailableNetWorkInfo() {
        NetworkInfo activeNetInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) FaceShowApplication.getInstance().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            activeNetInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (activeNetInfo != null && activeNetInfo.isAvailable()) {
            return activeNetInfo;
        } else {
            return null;
        }
    }

    public static String getNetWorkType() {

        String netWorkType = "2";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo();

        if (netWorkInfo != null) {
            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "1";
            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = "0";
            }
        }
        return netWorkType;
    }

    /**
     * user_event
     * */
    public static String getNetType() {
        String netWorkType = "5";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo();
        if (netWorkInfo != null) {
            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netWorkType = "0";
            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (netWorkInfo.getSubtype()){
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        netWorkType = "4";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        netWorkType = "3";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netWorkType = "2";
                        break;
                    default:
                        netWorkType = "5";
                        break;
                }
            }
        }
        return netWorkType;
    }

}
