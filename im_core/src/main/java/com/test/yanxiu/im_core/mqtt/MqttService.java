package com.test.yanxiu.im_core.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by cailei on 05/03/2018.
 */


public class MqttService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getStringExtra("command");
        if ("connect".equals(command)) {
            connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private String host = "tcp://orz.yanxiu.com:7914";
    private String userName = "admin";
    private String passWord = "public";
    private String clientId = "android";

    protected MqttAndroidClient mClient;
    protected MqttConnectOptions mMqttConnectOptions;

    // 用EventBus通知Topic, Msg界面
    protected MqttCallback mCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private void initMqtt() {
        if (TextUtils.isEmpty(host) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord) || TextUtils.isEmpty(clientId)) {
            throw new NullPointerException("please call method setClientMessage(String host, String userName, String passWord, String clientId) first");
        }
        mClient = new MqttAndroidClient(this, host, clientId);
        if (mMqttConnectOptions == null) {
            mMqttConnectOptions = new MqttConnectOptions();
            // 清除缓存
            mMqttConnectOptions.setCleanSession(true);
            // 设置超时时间，单位：秒
            mMqttConnectOptions.setConnectionTimeout(10);
            // 心跳包发送间隔，单位：秒
            mMqttConnectOptions.setKeepAliveInterval(60);
        }
        mMqttConnectOptions.setUserName(userName);
        mMqttConnectOptions.setPassword(passWord.toCharArray());
        mClient.setCallback(mCallback);


    }

    private void connect() {
        if (mClient != null) {
            try {
                mClient.connect(mMqttConnectOptions);
            } catch (Exception e) {
            }
        }
    }

    private void disconnect() {
        if ((mClient != null) && mClient.isConnected()) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
