package com.wind.control.activity.device;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Created by luow on 2018/8/31
 * 注释：
 */
public class BindDeviceActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.btn_next)
    Button mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
    }

    @OnClick({R.id.iv_left, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.btn_next:
                openActivity(ConfirmWifiActivity.class);
                break;
        }
    }
}
