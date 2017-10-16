package com.yanxiu.gphone.faceshow.http.main;

import com.yanxiu.gphone.faceshow.homepage.bean.main.MainBean;
import com.yanxiu.gphone.faceshow.http.base.FaceShowBaseResponse;

/**
 * 课程评价
 */

public class MainResponse extends FaceShowBaseResponse {

    private MainBean data = new MainBean();

    public MainBean getData() {
        return data;
    }

    public void setData(MainBean data) {
        this.data = data;
    }
    private long currentTime ;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}
