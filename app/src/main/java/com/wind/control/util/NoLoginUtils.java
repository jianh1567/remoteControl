package com.wind.control.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wind.control.activity.load.LoginActivity;
import com.wind.control.view.IOSAlertDialog;

/**
 * 作者: Created by luow on 2018/1/4.
 * 注释：用户是否登录
 */
public class NoLoginUtils {

    public static void isLogin(final Context context) {
        IOSAlertDialog iosAlertDialog = new IOSAlertDialog(context);
        iosAlertDialog.builder().setCancelable(false)
                .setTitle("温馨提示").setMessage("您当前尚未登录")
                .setPositiveButton("去登陆", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(context, LoginActivity.class);
                        context.startActivity(it);
                    }

                }).setNegativeButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        }).show();
    }
}