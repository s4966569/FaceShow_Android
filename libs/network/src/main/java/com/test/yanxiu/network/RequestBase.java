package com.test.yanxiu.network;

import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by cailei on 08/11/2016.
 */

public abstract class RequestBase {
    public transient ResponseBodyDealer bodyDealer;
    public static final Gson gson = new GsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }

    protected static OkHttpClient client = null;

    protected static final Handler handler = new Handler(Looper.getMainLooper());

    protected transient Call call = null;

    public Call getCall() {
        return call;
    }

    public enum HttpType {
        GET,
        POST
    }

    protected abstract boolean shouldLog();

    protected HttpType httpType() {
        return HttpType.GET;
    }

    protected abstract String urlServer();

    protected abstract String urlPath();

    protected String fullUrl() throws NullPointerException, IllegalAccessException, IllegalArgumentException {
        String server = urlServer();
        String path = urlPath();

        if (server == null) {
            throw new NullPointerException();
        }

        server = omitSlash(server);
        path = omitSlash(path);

        if (!urlServer().substring(0, 4).equals("http")) {
            server = "http://" + urlServer();
        }

        String fullUrl = server;
        if (path != null) {
            fullUrl = fullUrl + "/" + path;
        }

        HttpUrl.Builder urlBuilder = HttpUrl.parse(fullUrl).newBuilder();

        Map<String, Object> params = urlParams();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                Object value = entry.getValue();
                if (!(value instanceof String)) {
                    value = gson.toJson(entry.getValue());
                }
                urlBuilder.addEncodedQueryParameter(entry.getKey(), (String) value);
            } catch (Exception e) {
            }

        }
        fullUrl = urlBuilder.build().toString();
        Log.e("requestUrl", fullUrl.toString());
        return fullUrl;
    }

    public <T> UUID startRequest(final Class<T> clazz, final HttpCallback<T> callback) {
        UUID uuid = UUID.randomUUID();
        Request request = null;
        try {
            request = generateRequest(uuid);
            if (shouldLog()) {

            }
        } catch (Exception e) {
        }
        if (request == null) {
            callback.onFail(RequestBase.this, new Error("request start error"));
            return null;
        }
        client = setClient();
        call = client.newCall(request);
        Log.d("cwq", request.url().toString());
        final long start = System.currentTimeMillis();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (call.isCanceled()) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFail(RequestBase.this, new Error("网络异常，请稍后重试"));
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        return;
                    }
                } catch (Exception e) {
                }
                String bodyString = response.body().string();
                if (bodyDealer != null) {
                    // 如易学易练项目，需要Des解密
                    bodyString = bodyDealer.dealWithBody(bodyString);
                }
                final String retStr = Html.fromHtml(bodyString).toString();
                try {
                    Log.e("http", retStr);
                } catch (Exception e) {
                    Log.e("Gson error: ", e.getMessage());
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!response.isSuccessful()) {
                            callback.onFail(RequestBase.this, new Error("服务器数据异常"));
                            return;
                        }
                        T ret;
                        try {
                            ret = RequestBase.gson.fromJson(jsonString(retStr), clazz);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.onFail(RequestBase.this, new Error("服务器返回格式错误"));
                            return;
                        }
                        callback.onSuccess(RequestBase.this, ret);
                    }
                });

            }
        });

        return uuid;
    }

    protected OkHttpClient setClient() {
        return OkHttpClientManager.getInstance();
    }

    public void cancelRequest() {
        if (call != null) {
            call.cancel();
        }
        call = null;
    }

    public static void cancelRequestWithUUID(UUID uuid) {
        for (Call call : client.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(uuid))
                call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (call.request().tag().equals(uuid))
                call.cancel();
        }
    }

    // 去除Post Body中的参数后，剩余的应加入Url里的参数
    protected Map<String, Object> urlParams() throws IllegalAccessException, IllegalArgumentException {
        String json = gson.toJson(this);
        Object o = gson.fromJson(json, this.getClass());
        Field[] fields = o.getClass().getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
                continue;
            }

            if (f.isAnnotationPresent(RequestParamType.class)) {
                RequestParamType annotation = (RequestParamType) f.getAnnotation(RequestParamType.class);
                RequestParamType.Type type = annotation.value();
                if (type == RequestParamType.Type.POST) {
                    f.set(o, null);
                }
            } else {
                if (httpType() == HttpType.GET) {
                }
                if (httpType() == HttpType.POST) {
                    f.set(o, null);
                }
            }
        }
        Map<String, Object> params = new HashMap<>();
        String oJson = gson.toJson(o);
        params = gson.fromJson(oJson, params.getClass());

        return params;
    }

    // 应该加入Post Body中的参数
    protected Map<String, Object> bodyParams() throws IllegalAccessException, IllegalArgumentException {
        String json = gson.toJson(this);
        Object o = gson.fromJson(json, this.getClass());
        Field[] fields = o.getClass().getFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(RequestParamType.class)) {
                RequestParamType annotation = (RequestParamType) f.getAnnotation(RequestParamType.class);
                RequestParamType.Type type = annotation.value();
                if (type == RequestParamType.Type.GET) {
                    f.set(o, null);
                }
            } else {
                if (httpType() == HttpType.GET) {
                    f.set(o, null);
                }
                if (httpType() == HttpType.POST) {
                }
            }
        }
        Map<String, Object> params = new HashMap<>();
        String oJson = gson.toJson(o);
        params = gson.fromJson(oJson, params.getClass());

        return params;
    }

    private String omitSlash(String org) {
        if (org == null) {
            return null;
        }

        String ret = org;
        // 掐头
        String t = ret.substring(0, 1);

        if ("/".equals(ret.substring(0, 1))) {
            ret = ret.substring(1, ret.length());
        }
        // 去尾
        if ("/".equals(ret.substring(ret.length() - 1, ret.length()))) {
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    protected void runOnUiThread(Runnable task) {
        handler.post(task);
    }

    protected Request generateRequest(UUID uuid) throws NullPointerException, IllegalAccessException, IllegalArgumentException {
        Request.Builder builder = new Request.Builder()
                .tag(uuid)
                .url(fullUrl());
        if (httpType() == HttpType.POST) {
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            Map<String, Object> params = bodyParams();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (!(value instanceof String)) {
                    value = gson.toJson(entry.getValue());
                }
                bodyBuilder.add(entry.getKey(), (String) value);
            }
            builder.post(bodyBuilder.build());
        }

        Request request = builder.build();
        return request;
    }

    private String jsonString(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                int count = 0;
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        count++;
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            if (count/2==1) {
                                temp[j] = '”';
                            }else {
                                temp[j] = '“';
                            }
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
}
