package com.wind.control.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * xml存取
 *
 * @author luow
 */
public class SharedPreferenceUtil {
    /**
     * 获取String数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringSP(Context context, String fileName, String key, String defaultValue) {
        String content = defaultValue;
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        // SharedPreferences.Editor ed = sp.edit();
        content = sp.getString(key, defaultValue);
        return content;
    }

    /**
     * 保存String数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public void setStringSP(Context context, String fileName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, value);
        ed.commit();
    }

    /**
     * 获取boolean数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanSP(Context context, String fileName, String key, boolean defaultValue) {
        boolean spValue = defaultValue;
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        spValue = sp.getBoolean(key, defaultValue);
        return spValue;
    }

    /**
     * 保存boolean数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public void setBooleanSP(Context context, String fileName, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    /**
     * 获取int数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntSP(Context context, String fileName, String key, int defaultValue) {
        int spValue = defaultValue;
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        spValue = sp.getInt(key, defaultValue);
        return spValue;
    }

    /**
     * 保存int数据
     *
     * @param context
     * @param fileName
     * @param key
     * @param value
     */
    public void setIntSP(Context context, String fileName, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key, value);
        ed.commit();
    }

    /**
     * 根据key删除SharedPreferences某数据
     *
     * @param context
     * @param fileName
     * @param key
     */
    public void removeDataSP(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(key);
        ed.commit();
    }
}
