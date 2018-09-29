package com.wind.control.widget;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wind.control.R;


public class SelectPicPopupWindow extends PopupWindow {

    private View rv_photo_paizhao, rv_photo_xcxz, rv_photo_quxiao;
    private View mMenuView;
    Activity mcontext;
    private LinearLayout rv_sex_boy;
    private LinearLayout rv_sex_gril;
    private LinearLayout rv_sex_cancle;
    private LinearLayout pop_layout;
    private LinearLayout pop_layout_sex;

    public SelectPicPopupWindow(Activity context, OnClickListener onClickListener, int select) {
        super(context);
        this.mcontext = context;
        mMenuView = LayoutInflater.from(context).inflate(R.layout.layout_pic_popup_window, null);
        SupportMultipleScreensUtil.scale(mMenuView);
     //   full(true);
        //拍照
        pop_layout = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);
        rv_photo_paizhao = mMenuView.findViewById(R.id.rv_photo_paizhao);
        rv_photo_xcxz = mMenuView.findViewById(R.id.rv_photo_xcxz);
        rv_photo_quxiao = mMenuView.findViewById(R.id.rv_photo_quxiao);

        //性别
        pop_layout_sex = (LinearLayout) mMenuView.findViewById(R.id.pop_layout_sex);
        rv_sex_boy = (LinearLayout) mMenuView.findViewById(R.id.rv_sex_boy);
        rv_sex_gril = (LinearLayout) mMenuView.findViewById(R.id.rv_sex_gril);
        rv_sex_cancle = (LinearLayout) mMenuView.findViewById(R.id.rv_sex_cancle);

        if (select == 1){//"1" 代表相机拍照
            pop_layout_sex.setVisibility(View.GONE);
            pop_layout.setVisibility(View.VISIBLE);
        }else if (select == 2){// 性别
            pop_layout.setVisibility(View.GONE);
            pop_layout_sex.setVisibility(View.VISIBLE);
        }


        //取消按钮
        rv_photo_quxiao.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                full(false);
                //销毁弹出框
                SelectPicPopupWindow.this.dismiss();
            }
        });

        rv_sex_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                full(false);
                //销毁弹出框
                SelectPicPopupWindow.this.dismiss();
            }
        });

        //设置按钮监听
        rv_photo_paizhao.setOnClickListener(onClickListener);
        rv_photo_xcxz.setOnClickListener(onClickListener);

        rv_sex_boy.setOnClickListener(onClickListener);
        rv_sex_gril.setOnClickListener(onClickListener);

        //设置半透明颜色
        ColorDrawable drawable = new ColorDrawable(0x70000000);
        setBackgroundDrawable(drawable);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setOutsideTouchable(false);
        this.setContentView(mMenuView);
    }

    private void full(boolean enable) {
        if (enable) {//隐藏状态栏
            WindowManager.LayoutParams lp = mcontext.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            mcontext.getWindow().setAttributes(lp);
        } else {//显示状态栏
            WindowManager.LayoutParams attr = mcontext.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mcontext.getWindow().setAttributes(attr);
        }
    }
}
