package com.garbled.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.garbled.util.net.HttpRequest;
import com.garbled.util.util.NetworkUtils;
import com.garbled.util.util.SPUtils;

/**
 * 总控制中心
 * Created by Garbled on 2016/5/14.
 */
public class Jonah {

    private static NetworkUtils networkUtils;

    private static Context context;
    public static void init(Context context){
        Jonah.context = context;
    }

    public static HttpRequest Http(){
        return HttpRequest.getInstance();
    }

    public static SPUtils SP(){
        return SPUtils.getInstance(context);
    }

    public static NetworkUtils netWork(){
        if (networkUtils == null){
            networkUtils = new NetworkUtils(context);
        }
        return networkUtils;
    }



    public static void onDestroy(){
        HttpRequest.getInstance().clear();
        Glide.with(context).onDestroy();
    }

    public static void onLowMemory(){
        Glide.with(context).onLowMemory();
    }
}
