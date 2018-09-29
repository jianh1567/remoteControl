package com.wind.control.activity.remote;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wind.control.R;
import com.wind.control.dialog.LoadingDialog;
import com.wind.control.util.BluetoothLeService;
import com.wind.control.util.LogUtils;
import com.wind.control.util.ToastUtils;
import com.wind.control.view.ColorLightView;
import com.wind.control.view.ColorPickerView;
import com.wind.control.view.ColorSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 作者：Created by luow on 2018/8/24
 * 注释：
 */
public class LightControlActivity extends AppCompatActivity implements View.OnClickListener,
        ColorSeekBar.OnStateChangeListener {

    private RelativeLayout mColorLayout;
    private Button mNightStatus;
    private ColorSeekBar mSeekBar1;
    private ColorSeekBar mSeekBar2;
    private ColorLightView mLightView;
    private static final int COLOR = 0xFFFF0000;
    private boolean isON = true;
    private boolean isConnect = false;
    private boolean isSelectColor = false;
    private int height, width;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    private final UUID ledValueUUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    private final UUID lightSensationUUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private BluetoothGattCharacteristic controlCharacteristicl;
    private BluetoothLeService mBluetoothLeService;
    private LoadingDialog mLoadingDialog;
    private String mConnectName;
    private ImageView mIvLeft;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //{0x7, (byte) 0x00, (byte) 0xFF, (byte) 0x00, 0x00, 0x00}
                if (!isSelectColor) {
                    ToastUtils.showToast("开关关了");
                    return;
                }
                String rgb = (String) msg.obj;
                String str[] = rgb.split(",");
                if (isConnect) {
                    mBluetoothLeService.writeCharacteristic(controlCharacteristicl, setValue(str));
                } else {
                    ToastUtils.showToast("蓝牙断开了");
                }
            }
//            else if (msg.what == 3) {
//                mLightView.setColor((int) msg.obj);
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_fragment);
        WindowManager manager = getWindow().getWindowManager();
        height = (int) (manager.getDefaultDisplay().getHeight() * 0.5f);
        width = (int) (manager.getDefaultDisplay().getWidth() * 0.7f);
        mConnectName = getIntent().getStringExtra("mac");
        mIvLeft = (ImageView) findViewById(R.id.iv_left);
        mIvLeft.setOnClickListener(this);
        mColorLayout = (RelativeLayout) findViewById(R.id.fragment1_color_control_layout);
        mNightStatus = (Button) findViewById(R.id.fragment1_ble_light_status);
        mNightStatus.setOnClickListener(this);
        mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
        ColorPickerView myView = new ColorPickerView(this, height, width, COLOR, mHandler);
        mColorLayout.addView(myView);
        mSeekBar1 = (ColorSeekBar) findViewById(R.id.fragment1_color_seek_bar1);
        mSeekBar2 = (ColorSeekBar) findViewById(R.id.fragment1_color_seek_bar2);
        mSeekBar2.setOnStateChangeListener(this);
        mSeekBar1.setColor(Color.RED, Color.BLUE, Color.WHITE, Color.YELLOW);
        mSeekBar2.setColor(Color.DKGRAY, Color.WHITE, Color.WHITE, Color.GRAY);
        mLightView = (ColorLightView) findViewById(R.id.fragment1_color_view_change);

        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "");
        mLoadingDialog.show();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment1_ble_light_status) {
            if (isON) {
                isSelectColor = true;
                mNightStatus.setBackgroundResource(R.drawable.ble_light_on);
                String s[] = {"ff", "ff", "ff"};
                byte[] b = setValue(s);
                if (isConnect) {
                    mBluetoothLeService.writeCharacteristic(controlCharacteristicl, b);
                }
                isON = false;
            } else {
                isSelectColor = false;
                mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
                String s[] = {"0", "0", "0"};
                byte[] b = setValue(s);
                if (isConnect) {
                    mBluetoothLeService.writeCharacteristic(controlCharacteristicl, b);
                }
                isON = true;
            }
        }else if(v.getId() == R.id.iv_left){
            onBackPressed();
        }
    }

    //--------------------------------------------ble蓝牙模块相关-------------------------------------
    //设置颜色
    private byte[] setValue(String[] str) {
        byte[] b = new byte[6];
        b[0] = 0x7;
        b[1] = (byte) Integer.parseInt(str[0], 16);
        b[2] = (byte) Integer.parseInt(str[1], 16);
        b[3] = (byte) Integer.parseInt(str[2], 16);
        b[4] = 0x00;
        b[5] = 0x00;
        return b;
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mConnectName);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    private Handler handler = new Handler();
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                isConnect = true;
                mLoadingDialog.dismiss();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //  ToastUtils.showToast("已断开");
                isConnect = false;
                handler.removeCallbacks(delayCheckRunnable);
                handler.postDelayed(delayCheckRunnable, 5 * 1000);
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.connect(mConnectName);
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            }

            int bleState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);

            if (BluetoothAdapter.STATE_OFF == bleState) {

            } else if (BluetoothAdapter.STATE_ON == bleState) {
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.connect(mConnectName);
                }
            }

        }
    };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        for (BluetoothGattService bluetoothGattService : gattServices) {
            LogUtils.d("service:" + bluetoothGattService.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            //bluetoothGattService.
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : characteristics) {
                LogUtils.d("Character:" + bluetoothGattCharacteristic.getUuid().toString());

                if (bluetoothGattCharacteristic.getUuid().equals(ledValueUUID)) {
                    controlCharacteristicl = bluetoothGattCharacteristic;

                } else if (bluetoothGattCharacteristic.getUuid().equals(lightSensationUUID)) {
//                    notifyCharacteristic = bluetoothGattCharacteristic;
//                    enableNotification(true, notifyCharacteristic);
                }
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        return intentFilter;
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    Runnable delayCheckRunnable = new Runnable() {

        @Override
        public void run() {

            if (isConnect) {
                //
                LogUtils.d(" 5s 之内，又重连上了，这是误报，不进行报警");
            } else {
                LogUtils.d("5s 之后。还是断线， 进行报警");
                isConnect = false;
            }

        }
    };

    @Override
    public void OnStateChangeListener(int progress) {
        if (!isSelectColor) {
            ToastUtils.showToast("开关关了");
            return;
        }
        String str[] = new String[3];
        str[0] = String.format("%02x", progress);
        str[1] = String.format("%02x", progress);
        str[2] = String.format("%02x", progress);
        if (isConnect) {
            mBluetoothLeService.writeCharacteristic(controlCharacteristicl, setValue(str));
        } else {
            ToastUtils.showToast("蓝牙断开了");
        }
    }

    @Override
    public void onStopTrackingTouch(int progress) {
        if (!isSelectColor) {
            ToastUtils.showToast("开关关了");
            return;
        }
        String str[] = new String[3];
        str[0] = String.format("%02x", progress);
        str[1] = String.format("%02x", progress);
        str[2] = String.format("%02x", progress);
        if (isConnect) {
            mBluetoothLeService.writeCharacteristic(controlCharacteristicl, setValue(str));
        } else {
            ToastUtils.showToast("蓝牙断开了");
        }
    }

}
