package com.wind.control.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by luow on 2017/12/15.
 */

public class AppInfoUtils {

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context.getApplicationContext()).versionName;
    }

    public static String getAppName(Context context) {
        return context.getApplicationContext().getString(getPackageInfo(context).applicationInfo.labelRes);
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context.getApplicationContext()).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
        }
        return pi;
    }
}
