package com.wind.control.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;

public class ConfirmDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView tv_title;
    private Button btn_neg;
    private Button btn_pos;
    private Display display;
    private EditText et_money;
    private RelativeLayout rl_success;
    private ImageView img_line;

    public ConfirmDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @SuppressWarnings("deprecation")
    public ConfirmDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.item_confirm_dialog, null);
        SupportMultipleScreensUtil.scale(view);
        // 获取自定义Dialog布局中的控件
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        rl_success = (RelativeLayout) view.findViewById(R.id.rl_success);
        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        et_money = (EditText) view.findViewById(R.id.et_money);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.75),
                LayoutParams.WRAP_CONTENT));

        return this;
    }

    public ConfirmDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ConfirmDialog setVisible() {
        img_line.setVisibility(View.GONE);
        btn_neg.setVisibility(View.GONE);
        rl_success.setVisibility(View.VISIBLE);
        return this;
    }

    public void hideInputMethod() {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(et_money.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public String getMoney() {
        return et_money.getText().toString();
    }


    public ConfirmDialog setTitle(String title) {
        if ("".equals(title) || title == null) {
            tv_title.setText("标题");
        } else {
            tv_title.setText(title);
        }
        return this;
    }

    public ConfirmDialog setPositiveButton(String text, final OnClickListener listener) {
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    // 重载函数 增加对ResId资源的支持
    public ConfirmDialog setPositiveButton(int textId, final OnClickListener listener) {
        String text = context.getResources().getString(textId);
        if ("".equals(text)) {
            btn_pos.setText("确定");
        } else {
            btn_pos.setText(text);
        }
        btn_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public ConfirmDialog setNegativeButton(String text, final OnClickListener listener) {
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    // 重载函数 增加对ResId资源的支持
    public ConfirmDialog setNegativeButton(int textId, final OnClickListener listener) {
        String text = context.getResources().getString(textId);
        if ("".equals(text)) {
            btn_neg.setText("取消");
        } else {
            btn_neg.setText(text);
        }
        btn_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }

}