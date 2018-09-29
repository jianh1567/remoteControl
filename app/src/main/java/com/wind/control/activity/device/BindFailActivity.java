package com.wind.control.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wind.control.R;
import com.wind.control.activity.mine.FeedbackActivity;
import com.wind.control.base.BaseActivity;

import butterknife.OnClick;

/**
 * 作者：Created by luow on 2018/9/13
 * 注释：
 */
public class BindFailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_fail);
    }

    @OnClick({R.id.btn_confirm, R.id.btn_feed_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                Intent it = new Intent(this, BindDeviceActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.btn_feed_back:
                Intent itFeed = new Intent(this, FeedbackActivity.class);
                startActivity(itFeed);
                break;
        }
    }
}
