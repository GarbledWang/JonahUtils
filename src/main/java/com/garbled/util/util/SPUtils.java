package com.garbled.util.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Garbled on 2016/4/8.
 */
public class SPUtils {

    private static SPUtils ourInstance;
    private static SharedPreferences sp;

    public static SPUtils getInstance(Context context) {
        if (ourInstance == null){
            synchronized (SPUtils.class){
                if (ourInstance == null){
                    ourInstance = new SPUtils();
                    sp = context.getSharedPreferences("mTime", Context.MODE_PRIVATE);
                    return ourInstance;
                }
            }
        }
        return ourInstance;
    }

    private SPUtils() {
    }

    //存储方法
    public void add(String key, Object value) {
        if (value instanceof Integer) {
            sp.edit().putInt(key, (Integer) value).commit();
        }else if (value instanceof String) {
            sp.edit().putString(key, (String) value).commit();
        }else if (value instanceof Boolean) {
            sp.edit().putBoolean(key, (Boolean) value).commit();
        }else if (value instanceof Long) {
            sp.edit().putLong(key, (Long) value).commit();
        }else if (value instanceof Double) {
            sp.edit().putFloat(key, (Float) value).commit();
        }
    }

    //读取的方法
    public String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public Long getLong(String key,long defaultValue){
        return sp.getLong(key, defaultValue);
    }

    public float getfloat(String key, float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }
}
