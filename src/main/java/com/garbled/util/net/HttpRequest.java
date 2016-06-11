package com.garbled.util.net;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 联网请求
 * Created by Garbled on 2016/5/14.
 */
public class HttpRequest {

    private static HttpRequest instance;
    private OkHttpClient okHttpClient;
    private Handler handler;

    private HttpRequest(){
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        handler = new Handler(Looper.getMainLooper());//获取主线程的Looper
    }

    public static HttpRequest getInstance(){
        if (instance == null){
            synchronized (HttpRequest.class){
                if (instance == null){
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }


    /**
     * 异步联网 封装okhttp
     *  @param url
     * @param params
     */
    public void getCommit(String url, Map<String, String> params, HttpCallBack callBack) {
        StringBuffer sb = new StringBuffer(url);
        if (params != null) {
            sb.append("?");
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                sb.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }

        Request request = new Request.Builder()
                .url(sb.toString())
                .build();
        connect(request, callBack);
    }

    private void connect(final Request request, final HttpCallBack callBack) {
        Call newCall = okHttpClient.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null){
                    String message = e.getMessage();
                    sendStringOnError(message,callBack);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();

                    switch (callBack.getType()) {
                        case HttpCallBack.TYPE_OBJECT:
                            //说明返回的是对象 获取对应的class
                            ParameterizedType type = (ParameterizedType) callBack.getClass().getGenericSuperclass();
                            Class aClass = (Class) type.getActualTypeArguments()[0];
                            Object obj = JSON.parseObject(body.string(), aClass);
                            sendObjectMain(obj, callBack);
                            break;
                        case HttpCallBack.TYPE_STRING:
                            String result = response.body().string();
                            sendStringMain(result, callBack);
                            break;
                        case HttpCallBack.TYPE_INPUTSTREAM:
                            InputStream inputStream = response.body().byteStream();
                            sendStreamMain(inputStream, callBack);
                            break;
                        case HttpCallBack.TYPE_READER:
                            Reader reader = response.body().charStream();
                            sendReaderMain(reader, callBack);
                            break;

                    }
                }else {
                    //说明不成功
                    sendStringOnError(response.code()+":错误",callBack);
                }
            }
        });
    }

    private void sendReaderMain(final Reader reader, final HttpCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(reader);
            }
        });

    }

    private void sendStringMain(final String result, final HttpCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(result);
            }
        });

    }

    private void sendStreamMain(final InputStream inputStream, final HttpCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(inputStream);
            }
        });
    }

    private void sendObjectMain(final Object obj, final HttpCallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(obj);
            }
        });
    }

    private void sendStringOnError(final String msg, final HttpCallBack callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onError(msg);
            }
        });
    }

    public void clear(){
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
