package com.test.yanxiu.im_core.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by cailei on 05/03/2018.
 */

public class MqttService extends Service {
    public class MqttBinder extends Binder {
        public void init() {
            doInit();
        }

        // host为null时，采用默认
        public void init(String host, String user, String pwd, String clientId) {
            MqttService.this.host = host;
            MqttService.this.userName = user;
            MqttService.this.passWord = pwd;
            MqttService.this.clientId = clientId;
            doInit();
        }

        public void connect() {
            doConnect();
        }

        public void disconnect() {
            doDisconnect();
        }

        public void subscribe(String topicId) {
            doSubscribe(topicId);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MqttBinder();
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

            MqttProtobufDealer.dealWithData(message.getPayload());
            // 有消息来了
            Log.d("Tag", "mqtt msg arrived : " + topic);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };

    private void doInit() {
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

    private void doConnect() {
        if (mClient != null) {
            try {
                mClient.connect(mMqttConnectOptions, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d("Tag", "mqtt connect successfully");

                        doSubscribe("16");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("Tag", "mqtt connect failed" + exception.toString());
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    private void doDisconnect() {
        if ((mClient != null) && mClient.isConnected()) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    private void doSubscribe(String topicId) {
        if ((mClient != null) && mClient.isConnected()) {
            try {
                mClient.subscribe("im/v1.0/topic/" + topicId, 1, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Log.d("Tag", "mqtt subscribe successfully");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        Log.d("Tag", "mqtt subscribe failed");
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
