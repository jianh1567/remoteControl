package com.wind.control.util;

import android.util.Log;

/**
 * 作者：Created by luow on 2018/5/9
 * 注释：Log统一管理类
 */
public class LogUtils {

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (Constants.IS_DEBUG)
            Log.i(Constants.DEBUG_TAG, msg);
    }

    public static void d(String msg) {
        if (Constants.IS_DEBUG)
            Log.d(Constants.DEBUG_TAG, msg);
    }

    public static void e(String msg) {
        if (Constants.IS_DEBUG)
            Log.e(Constants.DEBUG_TAG, msg);
    }

    public static void v(String msg) {
        if (Constants.IS_DEBUG)
            Log.v(Constants.DEBUG_TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (Constants.IS_DEBUG)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (Constants.IS_DEBUG)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (Constants.IS_DEBUG)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (Constants.IS_DEBUG)
            Log.v(tag, msg);
    }
}