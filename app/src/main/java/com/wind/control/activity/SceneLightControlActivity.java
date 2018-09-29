package com.wind.control.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.util.Constants;
import com.wind.control.widget.SupportMultipleScreensUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 1003373 on 2018/8/23.
 */

public class SceneLightControlActivity extends BaseActivity {
    @BindView(R.id.tv_save_setting)
    TextView mTvSaveSetting;
    @BindView(R.id.tv_activity_title)
    TextView mTvActivityTitle;
    // 声明PopupWindow
    private PopupWindow popupWindow;
    // 声明PopupWindow对应的视图
    private View popupView;

    // 声明平移动画
    private TranslateAnimation animation;

    @BindView(R.id.rl_light_control)
    RelativeLayout mRlLightControl;
    @BindView(R.id.tv_light_status)
    TextView mTvLightStatus;

    private int mSwStatus;
    private int id;
    private String name;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);
        ButterKnife.bind(this);

        setActivityTitle(mTvActivityTitle, R.string.light_control);
        mRlLightControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
                lightoff();
            }
        });
        initData();
        initListner();
    }

    private void initData(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        type = intent.getStringExtra("type");
    }

    private void initListner() {
        mTvSaveSetting.setVisibility(View.VISIBLE);
        mTvSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent();
                mIntent.putExtra("actionName", name);
                mIntent.putExtra("id", id);
                mIntent.putExtra("type", type);
                mIntent.putExtra("lightStatus", mSwStatus);
                setResult(Constants.LIGHTCODE, mIntent);
                SceneLightControlActivity.this.finish();
            }
        });
    }

    private void showPopup() {
        if (popupWindow == null) {
            popupView = View.inflate(this, R.layout.popup_light_control, null);
            SupportMultipleScreensUtil.scale(popupView);
            // 参数2,3：指明popupwindow的宽度和高度
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lighton();
                }
            });

            // 设置背景图片， 必须设置，不然动画没作用
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);

            // 设置点击popupwindow外屏幕其它地方消失
            popupWindow.setOutsideTouchable(true);

            // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(200);

            popupView.findViewById(R.id.tv_light_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    lighton();
                }
            });
            popupView.findViewById(R.id.tv_light_open).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    mTvLightStatus.setText(R.string.open);
                    mSwStatus = 1;
                    lighton();
                }
            });

            popupView.findViewById(R.id.tv_light_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mTvLightStatus.setText(R.string.close);
                    mSwStatus = 0;
                    lighton();
                }
            });
        }

        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupView.startAnimation(animation);
    }

    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }

    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }
}
