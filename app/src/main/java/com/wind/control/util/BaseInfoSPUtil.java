package com.wind.control.util;

import android.content.Context;

/**
 * 作者：Created by luow on 2018/5/10
 * 注释：基本信息数据存取工具
 */
public class BaseInfoSPUtil {
    private static BaseInfoSPUtil instance;
    private SharedPreferenceUtil spUtil;//SharedPreference操作工具
    /*SP文件名*/
    private static final String FILE_NAME = "BASE_INFO";
    /* key值 */
    public static final String KEY_CONNECTED_DEVICE_NAME = "KEY_CONNECTED_DEVICE_NAME";
    public static final String KEY_CONNECTED_SUCCESS = "KEY_CONNECTED_SUCCESS";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_USER_PHONE_NUM = "KEY_USER_USER_PHONE_NUM";//用户手机号码
    public static final String KEY_LOGIN_TOKEN = "KEY_LOGIN_TOKEN";//登录凭证

    /* 默认Value **/
    private static final String DEFAULT_CONNECTED_DEVICE_NAME = "";
    private static final String DEFAULT_CONNECTED_SUCCESS = "false";
    private static final String DEFAULT_NAME = "";
    private static final String DEFAULT_USER_PHONE_NUM = "";//用户手机号码
    private static final String DEFAULT_LOGIN_TOKEN = "";//默认登录凭证

    public void setConnectName(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_CONNECTED_DEVICE_NAME, token);
    }

    public String getConnectSuccess(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_CONNECTED_SUCCESS, DEFAULT_CONNECTED_SUCCESS);
    }

    public void setConnectSuccess(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_CONNECTED_SUCCESS, token);
    }

    public String getConnectName(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_CONNECTED_DEVICE_NAME, DEFAULT_CONNECTED_DEVICE_NAME);
    }

    public void setName(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_NAME, token);
    }

    public String getName(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_NAME, DEFAULT_NAME);
    }

    //用户手机号
    public void setUserPhoneNum(Context context, String value) {
        spUtil.setStringSP(context, FILE_NAME, KEY_USER_PHONE_NUM, value);
    }

    //用户手机号
    public String getUserPhoneNum(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_USER_PHONE_NUM, DEFAULT_USER_PHONE_NUM);
    }


    //存入登录Token
    public void setLoginToken(Context context, String token) {
        spUtil.setStringSP(context, FILE_NAME, KEY_LOGIN_TOKEN, token);
    }

    //获取用户登录Token
    public String getLoginToken(Context context) {
        return spUtil.getStringSP(context, FILE_NAME,
                KEY_LOGIN_TOKEN, DEFAULT_LOGIN_TOKEN);
    }

    /**
     * 根据key删除SharedPreferences某数据
     *
     * @param context
     * @param key
     */
    public void removeSpData(Context context, String key) {
        spUtil.removeDataSP(context, FILE_NAME, key);
    }

    public static BaseInfoSPUtil getInstance() {
        if (instance == null) {
            instance = new BaseInfoSPUtil();
        }
        return instance;
    }

    private BaseInfoSPUtil() {
        spUtil = new SharedPreferenceUtil();
    }
}