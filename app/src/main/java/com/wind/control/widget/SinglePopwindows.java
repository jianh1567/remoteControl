package com.wind.control.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.wind.control.R;

public class SinglePopwindows extends PopupWindow implements OnClickListener {
    private Context mContext;
    private View view;
    private Button mBtnOkOne;
    private Button mBtnNoOk;
    private Button mBtnPrevious;
    private Button mBtnOk;
    private Button mBtnNext;
    private LinearLayout mLlOne;
    private LinearLayout mLlTwo;

    public SinglePopwindows(Context context) {
        super(context);
        this.mContext = context;
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(false);
        initView();
    }

    private void initView() {
        findView();
        setListener();
        this.setContentView(view);
    }

    private void findView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_single_list, null);
        mBtnOkOne = (Button) view.findViewById(R.id.btn_ok_one);
        mBtnNoOk = (Button) view.findViewById(R.id.btn_no_ok);
        mBtnPrevious = (Button) view.findViewById(R.id.btn_previous);
        mBtnOk = (Button) view.findViewById(R.id.btn_ok);
        mBtnNext = (Button) view.findViewById(R.id.btn_next);
        mLlOne = (LinearLayout) view.findViewById(R.id.ll_one);
        mLlTwo = (LinearLayout) view.findViewById(R.id.ll_two);
    }

    private void setListener() {
        mBtnOkOne.setOnClickListener(this);
        mBtnNoOk.setOnClickListener(this);
        mBtnPrevious.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_one:

                mListener.onOk();
                dismiss();

                break;
            case R.id.btn_no_ok:
                mLlOne.setVisibility(View.GONE);
                mLlTwo.setVisibility(View.VISIBLE);
                mListener.onNext();
                break;
            case R.id.btn_previous:
                mListener.onPrevious();
                break;
            case R.id.btn_ok:
                mListener.onOk();
                dismiss();
                break;
            case R.id.btn_next:
                mListener.onNext();
                break;
            default:
                break;
        }
    }

    private OnSingleChangeListener mListener;

    public void setOnSingleChangeListener(OnSingleChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnSingleChangeListener {
        void onNext();

        void onPrevious();

        void onOk();

    }

}