package com.wind.control.appupdate;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wind.control.R;


public class AppUpdateDialog extends Dialog {

    private Context mContext;
    private String title;
    private String message;
    private String rightBtn;
    private String leftBtn;
    private int type;
    private BtnClickListener mListener;
    private ProgressBar mProgressBar;
    private TextView mTitle, mMessage, mConfirm, mCancel;
    private LinearLayout mBtnLay;
    private LinearLayout mLlBtn;
    private TextView mDlgNoDown;

    public interface BtnClickListener {
        public void doConfirm();

        public void doCancel();

        public void doNoDown();


    }

    public AppUpdateDialog(Context context, String title, String message, String rightBtn, String leftBtn, int type) {
        super(context, R.style.dialog_custom);
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.leftBtn = leftBtn;
        this.rightBtn = rightBtn;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dlg_yes_no, null);
        setContentView(view);
        mTitle = (TextView) view.findViewById(R.id.dlg_title);
        mMessage = (TextView) view.findViewById(R.id.dlg_message);
        mConfirm = (TextView) view.findViewById(R.id.dlg_confirm);
        mCancel = (TextView) view.findViewById(R.id.dlg_cancel);
        mProgressBar = (ProgressBar) view.findViewById(R.id.dlg_progress);
        mBtnLay = (LinearLayout) view.findViewById(R.id.dlg_btn_layout);
        mLlBtn = (LinearLayout) view.findViewById(R.id.ll_btn);
        mDlgNoDown = (TextView) view.findViewById(R.id.dlg_no_down);
        setCancelable(false);
        mConfirm.setOnClickListener(new clickListener());
        mCancel.setOnClickListener(new clickListener());
        mDlgNoDown.setOnClickListener(new clickListener());

        if (type == 1) {
            mLlBtn.setVisibility(View.VISIBLE);
            mDlgNoDown.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mTitle.setText(title);
            mMessage.setText(message);
            mConfirm.setText(rightBtn);
            mCancel.setText(leftBtn);
        } else if (type == 3) {
            mProgressBar.setVisibility(View.VISIBLE);
            mBtnLay.setVisibility(View.GONE);
            mProgressBar.setProgress(0);
            mTitle.setText(title);
            mMessage.setText(message);
        }

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

    }

    public void UpdateProgress(String title, String message, String rightBtn, String leftBtn, int point) {
        mLlBtn.setVisibility(View.GONE);
        mDlgNoDown.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        mMessage.setText(message);
        mConfirm.setText(rightBtn);
        mCancel.setText(leftBtn);
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(point);
    }

    public void setClickListener(BtnClickListener mListener) {
        this.mListener = mListener;
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dlg_confirm: {
                    mListener.doConfirm();
                    break;
                }
                case R.id.dlg_cancel: {
                    mListener.doCancel();
                    break;
                }
                case R.id.dlg_no_down: {
                    mListener.doNoDown();
                    break;
                }
            }
        }
    }
}
