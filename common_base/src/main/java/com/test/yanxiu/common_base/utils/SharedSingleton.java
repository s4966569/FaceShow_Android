package com.test.yanxiu.common_base.utils;

import java.util.HashMap;

/**
 * Created by cailei on 14/03/2018.
 */

public class SharedSingleton {
    private static SharedSingleton instance;
    private HashMap hashMap = new HashMap();

    private SharedSingleton() {}
    public static SharedSingleton getInstance() {
        if( instance == null ) {
            synchronized( SharedSingleton.class ) {
                if( instance == null ) {
                    instance = new SharedSingleton();
                }
            }

        }
        return instance;
    }

    public void destroyInstance() {
        hashMap.clear();
        hashMap = null;
        instance = null;
    }

    public synchronized <T> T get(String key) {
        return (T)hashMap.get(key);
    }

    public synchronized <T> void set(String key, T t) {
        hashMap.put(key, t);
    }
}
