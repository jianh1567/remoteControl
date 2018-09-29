package com.wind.control.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.SceneActionInfo;
import com.wind.control.util.Constants;
import com.wind.control.widget.SupportMultipleScreensUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 1003373 on 2018/8/23.
 */

public class SceneAircControlActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {
    @BindView(R.id.tv_airc_status)
    TextView mTvAircStatus;
    @BindView(R.id.tv_style_switch)
    TextView mTvModleSwitch;
    @BindView(R.id.tv_speed_switch)
    TextView mTvSpeedSwitch;

    private static final String TAG = "SceneAircActivity";
    @BindView(R.id.tv_temp_switch)
    TextView mTvTempSwitch;
    @BindView(R.id.tv_save_setting)
    TextView mTvSaveSetting;
    @BindView(R.id.tv_activity_title)
    TextView mTvActivityTitle;

    private boolean isUpdateInfo = false;
    private int mSwStatus;
    private int id;
    private String name;
    private String type;

    private String[] numbers = {"16℃", "17℃", "18℃", "19℃", "20℃", "21℃", "22℃", "23℃",
            "24℃", "25℃", "26℃", "27℃", "28℃", "29℃", "30℃"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airc_control);
        ButterKnife.bind(this);

        setActivityTitle(mTvActivityTitle, R.string.airc_control);
        initData();
        initListner();
    }

    private void initData(){
        Intent intent = getIntent();

        SceneActionInfo sceneActionInfo = (SceneActionInfo) intent.getSerializableExtra("actionInfo");
        if(sceneActionInfo != null){
            initAircData(sceneActionInfo);
        }else {
            id = intent.getIntExtra("id", 0);
            name = intent.getStringExtra("name");
            type = intent.getStringExtra("type");
        }
    }

    private void initAircData(SceneActionInfo sceneActionInfo){
        isUpdateInfo = true;
        int aircStatus = sceneActionInfo.getStatus();
        String aricModle = sceneActionInfo.getModle();
        String aricTemp = sceneActionInfo.getTemp();
        String aircSpeed = sceneActionInfo.getSpeed();

        if(aircStatus == 1){
            mTvAircStatus.setText(R.string.open);
        }else {
            mTvAircStatus.setText(R.string.close);
        }

        mTvModleSwitch.setText(aricModle);
        mTvTempSwitch.setText(aricTemp);
        mTvSpeedSwitch.setText(aircSpeed);
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
                mIntent.putExtra("aircStatus",mSwStatus);
                mIntent.putExtra("aircModel", mTvModleSwitch.getText().toString());
                mIntent.putExtra("aircTemp", mTvTempSwitch.getText().toString());
                mIntent.putExtra("aircSpeed",mTvSpeedSwitch.getText().toString());
                setResult(Constants.ARICCODE, mIntent);
                SceneAircControlActivity.this.finish();
            }
        });
    }

    public void onClickAicSwitch(View view) {
        lightoff();
        showAircSwitchPopup();
    }

    public void onClickAircStyle(View view) {
        lightoff();
        showAircStylePopup();
    }

    public void onClickAircTemp(View view) {
        lightoff();
        showAircTempPopup();
    }

    public void onClickAircSpeed(View view) {
        lightoff();
        showAircSpeedPopup();
    }

    private void showAircSwitchPopup() {
        View popupView = View.inflate(this, R.layout.popup_light_control, null);
        SupportMultipleScreensUtil.scale(popupView);
        final PopupWindow popupWindow = popupWindow(popupView);

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
                mTvAircStatus.setText(R.string.open);
                mSwStatus = 1;
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_light_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvAircStatus.setText(R.string.close);
                mSwStatus = 0;
                lighton();
            }
        });

        popupView.startAnimation(popupAnimation());
    }

    private void showAircStylePopup() {
        View popupView = View.inflate(this, R.layout.popup_airc_style, null);
        SupportMultipleScreensUtil.scale(popupView);
        final PopupWindow popupWindow = popupWindow(popupView);

        popupView.findViewById(R.id.tv_style_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                lighton();
            }
        });
        popupView.findViewById(R.id.tv_style_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mTvModleSwitch.setText(R.string.auto);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_style_cool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvModleSwitch.setText(R.string.cool);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_style_dehumidified).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvModleSwitch.setText(R.string.dehumidified);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_style_ventilation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvModleSwitch.setText(R.string.ventilation);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_style_heat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvModleSwitch.setText(R.string.heat);
                lighton();
            }
        });

        popupView.startAnimation(popupAnimation());
    }

    private void showAircSpeedPopup() {
        View popupView = View.inflate(this, R.layout.popup_airc_speed, null);
        SupportMultipleScreensUtil.scale(popupView);
        final PopupWindow popupWindow = popupWindow(popupView);

        popupView.findViewById(R.id.tv_speed_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                lighton();
            }
        });
        popupView.findViewById(R.id.tv_speed_auto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mTvSpeedSwitch.setText(R.string.auto);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_speed_low).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvSpeedSwitch.setText(R.string.low);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_speed_middle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvSpeedSwitch.setText(R.string.middle);
                lighton();
            }
        });

        popupView.findViewById(R.id.tv_speed_high).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvSpeedSwitch.setText(R.string.high);
                lighton();
            }
        });

        popupView.startAnimation(popupAnimation());
    }

    private void showAircTempPopup() {
        View popupView = View.inflate(this, R.layout.popup_airc_temp, null);
        final PopupWindow popupWindow = popupWindow(popupView);

        final NumberPicker numberPicker = (NumberPicker) popupView.findViewById(R.id.temppicker);

        numberPicker.setOnValueChangedListener(this);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(16);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(numbers);
        numberPicker.setValue(16);
        popupView.startAnimation(popupAnimation());

        popupView.findViewById(R.id.tv_temp_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupView.findViewById(R.id.tv_temp_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvTempSwitch.setText(numberPicker.getValue() + "℃");
            }
        });
    }

    private PopupWindow popupWindow(View view) {
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
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

        // 在点击之后设置popupwindow的销毁
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            lighton();
        }

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return popupWindow;
    }

    private TranslateAnimation popupAnimation() {
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);

        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);
        return animation;
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

    @Override
    public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
        Log.i(TAG, "onValueChange " + oldVal + "  newVal = " + newVal);
    }
}
