package com.wind.control.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.wind.control.R;

public class SelectConnectDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private Display display;
    private LinearLayout mLlWifi;
    private LinearLayout mLlBlue;
    private LinearLayout mLlScan;

    public SelectConnectDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @SuppressWarnings("deprecation")
    public SelectConnectDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_connect_dialog, null);
        SupportMultipleScreensUtil.scale(view);
        // 获取自定义Dialog布局中的控件
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        mLlWifi = (LinearLayout) view.findViewById(R.id.ll_wifi);
        mLlBlue = (LinearLayout) view.findViewById(R.id.ll_blue);
        mLlScan = (LinearLayout) view.findViewById(R.id.ll_scan);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.7),
                LayoutParams.WRAP_CONTENT));

        return this;
    }

    public SelectConnectDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }

    public SelectConnectDialog setBlueConnect(final View.OnClickListener listener) {
        mLlBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


    public SelectConnectDialog setWifiConnect(final View.OnClickListener listener) {
        mLlWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


    public SelectConnectDialog setScanConnect(final View.OnClickListener listener) {
        mLlScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }


}