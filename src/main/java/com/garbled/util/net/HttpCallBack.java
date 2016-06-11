package com.garbled.util.net;

/**
 * 联网监听的封装
 * Created by Garbled on 2016/4/10.
 */
public abstract class HttpCallBack<T>  {

    public static final int TYPE_STRING = 0;
    public static final int TYPE_INPUTSTREAM = 1;
    public static final int TYPE_READER = 2;
    public static final int TYPE_OBJECT = 3;
    private int type;

    public HttpCallBack(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public abstract void onSuccess(T result);

    public abstract void onError(String message);

}
