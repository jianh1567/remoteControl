package com.wind.control.activity.remote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.wind.control.R;
import com.wind.control.dialog.LoadingDialog;
import com.wind.control.util.BleProtocol;
import com.wind.control.util.BluetoothLeService;
import com.wind.control.util.Constants;
import com.wind.control.util.LogUtils;
import com.wind.control.util.ToastUtils;

import java.util.List;
import java.util.UUID;

import static com.wind.control.util.ScanRecordUtil.bytesToHex;

/**
 * Created by wangtongming on 2017/11/25.
 * 注释：蓝牙操作通讯界面
 */

public class LightControl1Activity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "LightControlActivity";
    private static final int TOME_OUT = 6000;
    private static int curLightRank = 0;

    private ImageView mImageBack;
    private Button mNightStatus;
    private SeekBar mSeekBarLightness;
    private SeekBar mSeekBarColorTem;

    private boolean isConnect = false;
    private boolean isSelectColor = false;

    private BluetoothGattCharacteristic mWriteCharacteristic,mReadCharacteristic;
    private BluetoothLeService mBluetoothLeService;
    private LoadingDialog mLoadingDialog;
    private Handler mHandler = new Handler();
    private String mConnectName;
    private int curLightness = 0xFFFF;
    private int curColorTem = 0x4e20;
    private boolean isFirstInto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_fragment1);
        mConnectName = getIntent().getStringExtra("mac");
        Log.d("lw", mConnectName);

        mImageBack = (ImageView)findViewById(R.id.tv_back);
        mImageBack.setOnClickListener(this);

        mNightStatus = (Button) findViewById(R.id.fragment1_ble_light_status);
        mNightStatus.setOnClickListener(this);
        mNightStatus.setBackgroundResource(R.drawable.ble_light_off);

        mSeekBarLightness = (SeekBar)findViewById(R.id.seek_bar_brightness);
        mSeekBarLightness.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSeekBarLightness.setProgress(0);

        mSeekBarColorTem = (SeekBar)findViewById(R.id.seek_bar_color_tem);
        mSeekBarColorTem.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSeekBarColorTem.setProgress(0);

        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "", false);
        mLoadingDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
            }
        }, TOME_OUT);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        isFirstInto = true;
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!isSelectColor) {
                ToastUtils.showToast("开关关了");
                return;
            }
            switch (seekBar.getId()){
                case R.id.seek_bar_brightness:{
                    if (fromUser){
                        curLightness = (int)((progress*0xFFFF)/255);
                        lightControlSendOrder(BleProtocol.BleSendLightness(curLightness));
                    }
                    Log.i(TAG,"curLightness: " + curLightness + ",fromUser: " + fromUser);
                    break;
                }
                case R.id.seek_bar_color_tem:{
                    // 800 - 20000, 0x320 - 0x4E20
                    if (fromUser){
                        curColorTem = (int)((progress*0x4E20)/100);
                        if (curColorTem < 800){
                            curColorTem = 800;
                        }
                        lightControlSendOrder(BleProtocol.BleSendTemperature(curColorTem));
                    }
                    Log.i(TAG,"curColorTemProgress: " + curColorTem);
                    break;
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * 灯控制方法,发送蓝牙指令
     * @param byteData  蓝牙命令
     */
    private void lightControlSendOrder(byte[] byteData){
        if (isConnect){
            mBluetoothLeService.writeCharacteristic(mWriteCharacteristic, byteData);
        }else{
            ToastUtils.showToast("蓝牙断开了");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.fragment1_ble_light_status:
            {
                if (!isSelectColor) {
                    isSelectColor = true;
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_on);
                    int curProgress = (int)(curLightness*255/0xFFFF);
                    Log.i(TAG,"curProgress: " + curProgress + ",curLightness: " + curLightness);
                    mSeekBarLightness.setProgress(curProgress);
                    mSeekBarColorTem.setProgress((int)(curColorTem*100/20000));
                    lightControlSendOrder(BleProtocol.BleSendOnOff(true));
                } else {
                    isSelectColor = false;
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
                    mSeekBarLightness.setProgress(0);
                    lightControlSendOrder(BleProtocol.BleSendOnOff(false));

                }
                break;
            }
            default:
                break;
        }
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

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        for (BluetoothGattService bluetoothGattService : gattServices) {
            LogUtils.i("BluetoothLeService:" + bluetoothGattService.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            //bluetoothGattService.
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : characteristics) {
                LogUtils.i("BluetoothLeService:" + bluetoothGattCharacteristic.getUuid().toString());

                if (bluetoothGattCharacteristic.getUuid().equals(Constants.LED_LIGHT_WRITE_UUID)) {
                    mWriteCharacteristic = bluetoothGattCharacteristic;
                } else if (bluetoothGattCharacteristic.getUuid().equals(Constants.LED_LIGHT_READ_UUID)) {
                    mReadCharacteristic = bluetoothGattCharacteristic;
                    mBluetoothLeService.enableIndications(mReadCharacteristic);
                    //TODO 蓝牙接收数据
                    mBluetoothLeService.readCharacteristic(mReadCharacteristic);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService.disconnect();
        mBluetoothLeService = null;
    }

    Runnable delayCheckRunnable = new Runnable() {
        @Override
        public void run() {
            if (isConnect) {
                LogUtils.d(" 5s 之内，又重连上了，这是误报，不进行报警");
            } else {
                LogUtils.d("5s 之后。还是断线， 进行报警");
                isConnect = false;
            }
        }
    };

    //设置颜色,颜色变化来控制等颜色和亮度
    //蓝牙协议0x7,0x00,0x00,0x00,0x00,0x00 6位
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                ToastUtils.showToast("蓝牙已连接");
                isConnect = true;
                mLoadingDialog.dismiss();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                ToastUtils.showToast("蓝牙已断开");
                isConnect = false;
                mHandler.removeCallbacks(delayCheckRunnable);
                mHandler.postDelayed(delayCheckRunnable, TOME_OUT);
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.connect(mConnectName);
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //TODO 蓝牙收到的数据
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                int dataLength = data.length;
                if (dataLength == 11){
                    int getLightness = BleProtocol.getBLELedLightness(data);
                    curLightness = getLightness;
                    int getColorTem = BleProtocol.getBLELedTemperature(data);
                    curColorTem = getColorTem;
                    mSeekBarLightness.setProgress((int)(getLightness/255));
                    mSeekBarColorTem.setProgress((int)(getColorTem/200));
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_on);
                    isSelectColor = true;
                }else if (dataLength == 5){
                    isSelectColor = false;
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
                    mSeekBarLightness.setProgress(0);
                }

                Log.i(TAG,"receiver getLightness: " + (int)(curLightness/255)
                        + ",data: " + bytesToHex(data) + ",dataLength: " + dataLength);
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
}
