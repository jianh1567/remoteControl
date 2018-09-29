package com.wind.control.activity.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.util.ByteUtil;
import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.util.EspUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者：Created by luow on 2018/9/12
 * 注释：
 */
public class ConfirmWifiActivity extends BaseActivity {

    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.tv_wifi_name)
    TextView mTvWifiName;
    @BindView(R.id.et_wifi_pwd)
    EditText mEtWifiPwd;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    private byte[] mSsid;
    private String mBssid;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                    onWifiChanged(wifiInfo);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_wifi);
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
    }

    @OnClick({R.id.ll_left, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(mEtWifiPwd.getText().toString())) {
                    showToast("请输入WiFi密码");
                    return;
                }
                if ((Boolean) mBtnConfirm.getTag()) {
                    Toast.makeText(this, R.string.wifi_5g_message, Toast.LENGTH_SHORT).show();
                    return;
                }

                mSsid = mTvWifiName.getTag() == null ? ByteUtil.getBytesByString(mTvWifiName.getText().toString())
                        : (byte[]) mTvWifiName.getTag();

           //     Log.d("lw", mSsid.toString());
                Log.d("lw", mEtWifiPwd.getText().toString());

                Intent it = new Intent();
                it.putExtra("bssid", mBssid);
                it.putExtra("ssid", mSsid);
                it.putExtra("pwd", mEtWifiPwd.getText().toString());
                openActivity(StartDmsActivity.class, it);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void onWifiChanged(WifiInfo info) {
        if (info == null) {
            mTvWifiName.setText("");
            mBtnConfirm.setEnabled(false);
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            mTvWifiName.setText("WiFi名称: " + ssid);
            mTvWifiName.setTag(ByteUtil.getBytesByString(ssid));
            byte[] ssidOriginalData = EspUtils.getOriginalSsidBytes(info);
            mTvWifiName.setTag(ssidOriginalData);

            mBtnConfirm.setEnabled(true);
            mBtnConfirm.setTag(Boolean.FALSE);

            mBssid = info.getBSSID();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int frequence = info.getFrequency();
                if (frequence > 4900 && frequence < 5900) {
                    // Connected 5G wifi. Device does not support 5G
                    mBtnConfirm.setTag(Boolean.TRUE);
                }
            }
        }
    }

}
