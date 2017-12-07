package com.yanxiu.gphone.faceshow.util;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yanxiu.gphone.faceshow.login.LoginActivity;

/**
 * Created by frc on 2017/12/7.
 */

public class LBSManager {
    private static LocationClient mLocationClient;
    public static void init(Context context){
        // TODO: 2017/12/1 基本功能已经实现 缺乏6.0的动态权限判断
        LocationClientOption locationClientOption =new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        locationClientOption.setCoorType("bd09ll");
        locationClientOption.setIsNeedAddress(true);
        locationClientOption.setIsNeedLocationDescribe(true);
        locationClientOption.setIsNeedLocationPoiList(true);
        mLocationClient=new LocationClient(context);
        mLocationClient.setLocOption(locationClientOption);

    }

    public static LocationClient getLocationClient (){
        return mLocationClient;
    }
}
