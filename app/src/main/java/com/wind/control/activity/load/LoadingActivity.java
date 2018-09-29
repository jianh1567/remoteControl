package com.wind.control.activity.load;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.wind.control.MyApplication;
import com.wind.control.R;
import com.wind.control.activity.MainActivity;
import com.wind.control.base.BaseActivity;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.UserApp;

/**
 * 作者：Created by luow on 2018/9/10
 * 注释：
 */
public class LoadingActivity extends BaseActivity {

    private static final String TAG = "LoadingActivity";
    private MyApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mApp = (MyApplication) getApplication();
        // login with guest-admin account
        new Thread() {
            @Override
            public void run() {
                mApp.mWeAPIs.signIn(LoadingActivity.this, mSignInCallback);
            }
        }.start();
        setting();
    }

    private WebAPICallbacks.SignInCallback mSignInCallback = new WebAPICallbacks.SignInCallback() {
        @Override
        public void onSignInSuccess(UserApp userApp) {
            mApp.setUserApp(userApp);
            if (null != mApp.getUserApp()) {
                Log.d(TAG, "signIn response : " + mApp.getUserApp().getId() + ", " + mApp.getUserApp().getToken());
            } else {
                Log.w(TAG, "signIn failed");
            }
        }

        @Override
        public void onSignInFailed() {
            Log.w(TAG, "sign in failed");
        }

        @Override
        public void onSignInError() {
            Log.e(TAG, "sign in error");
        }
    };

    private void setting() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                turnToMain();
            }
        }, 500);
    }


    private void turnToMain() {
        openActivity(MainActivity.class);
        finish();
    }

}
