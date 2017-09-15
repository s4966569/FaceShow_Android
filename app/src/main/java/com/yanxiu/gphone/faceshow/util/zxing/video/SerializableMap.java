package com.yanxiu.gphone.faceshow.util.zxing.video;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LHZ on 2016/6/27.
 */
public class SerializableMap implements Serializable {

    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
