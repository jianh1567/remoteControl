package com.wind.control;

import android.app.Application;

import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.log.LoggerInterceptor;
import com.wind.control.widget.SupportMultipleScreensUtil;

import net.irext.webapi.WebAPIs;
import net.irext.webapi.model.UserApp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by W010003373 on 2018/8/7.
 */

public class MyApplication extends Application{
    private static final String TAG = MyApplication.class.getSimpleName();

    private static final String ADDRESS = "https://irext.net";
    private static final String APP_NAME = "/irext-server";

    public WebAPIs mWeAPIs = WebAPIs.getInstance(ADDRESS, APP_NAME);

    private UserApp mUserApp;

    public UserApp getUserApp() {
        return mUserApp;
    }

    public void setUserApp(UserApp userApp) {
        mUserApp = userApp;
    }

    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SupportMultipleScreensUtil.init(this);

        //okHttp初始化
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("AgCarManager"))
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }
}
