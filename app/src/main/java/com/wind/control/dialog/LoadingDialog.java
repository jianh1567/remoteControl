package com.wind.control.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.widget.SupportMultipleScreensUtil;

/**
 * 作者: Created by luow on 2017/12/6.
 * 注释：网络加载框
 */
public class LoadingDialog extends Dialog {
    private boolean isCancel = false;
    private String text = "登录中...";
    private Context context;
    private TextView loading_text;

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme, String text) {
        super(context, theme);
        this.context = context;
        this.text = text;
    }

    public LoadingDialog(Context context, int theme, String text, boolean isCancel) {
        super(context, theme);
        this.context = context;
        this.text = text;
        this.isCancel = isCancel;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loading_dialog_layout);
        View rootView = findViewById(android.R.id.content);
        SupportMultipleScreensUtil.scale(rootView);
        setCanceledOnTouchOutside(false);
        if (isCancel) {
            setCancelable(false);
        }

        loading_text = (TextView) findViewById(R.id.loading_text);
        if (text.equals("")) {
            loading_text.setVisibility(View.GONE);
        } else {
            loading_text.setVisibility(View.VISIBLE);
            loading_text.setText(text);
        }
    }

    public void setLoadingText(String text) {
        loading_text.setText(text);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
    }
}
