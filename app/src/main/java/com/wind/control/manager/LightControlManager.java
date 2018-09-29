package com.wind.control.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.wind.control.R;
import com.wind.control.util.BleProtocol;
import com.wind.control.util.BluetoothLeService;
import com.wind.control.util.LogUtils;
import com.wind.control.util.ToastUtils;

import java.util.List;
import java.util.UUID;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.wind.control.util.ScanRecordUtil.bytesToHex;

/**
 * Created by wangtongming on 2017/11/25.
 * 注释：蓝牙操作通讯界面
 */

public class LightControlManager {
    public static final String TAG = "LightControlManager";
    private static final int TOME_OUT = 6000;
    private static int curLightRank = 0;

    private boolean mLightOn = false;
    private boolean isConnect = false;
    private boolean isSelectColor = false;
    public static final UUID ledValueUUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final UUID lightSensationUUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic mBTCharacteristic,mLightCharacteristic;
    private BluetoothLeService mBluetoothLeService;
    private Handler mHandler = new Handler();
    private String mConnectName;

    private Context mContext;

    public LightControlManager(Context context){
        mContext = context;
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void startLightControl(String name, boolean isOn){
        if (mBluetoothLeService != null) {
            mBluetoothLeService.connect(name);
        }

        mLightOn = isOn;
    }

    /**
     * 灯控制方法,发送蓝牙指令
     * @param byteData  蓝牙命令
     */
    private void lightControlSendOrder(byte[] byteData){
        if (isConnect){
            Log.i(TAG, "lightControlSendOrder isConnect = " + isConnect);
            if (mBTCharacteristic != null ){
                mBluetoothLeService.writeCharacteristic(mBTCharacteristic, byteData);
            }else{
                Log.i(TAG, "lightControlSendOrder mBTCharacteristic is null");
            }

        }else{
            ToastUtils.showToast("蓝牙断开了");
        }
    }

    private void controlLightOnOff(boolean isON){
        Log.i(TAG, "isON = " + isON);
        if (isON) {
            isSelectColor = true;
            String s[] = {"ff", "ff", "ff"};
            lightControlSendOrder(setValue(s));
        } else {
            isSelectColor = false;
            String s[] = {"0", "0", "0"};
            lightControlSendOrder(setValue(s));
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
//            mBluetoothLeService.connect(mConnectName);
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

                if (bluetoothGattCharacteristic.getUuid().equals(ledValueUUID)) {
                    LogUtils.i("BluetoothLeService:2111212");
                    mBTCharacteristic = bluetoothGattCharacteristic;
                    mBluetoothLeService.readCharacteristic(mBTCharacteristic);
                    controlLightOnOff(mLightOn);
                } else if (bluetoothGattCharacteristic.getUuid().equals(lightSensationUUID)) {
                    LogUtils.i("BluetoothLeService:2222");
                    mLightCharacteristic = bluetoothGattCharacteristic;
                    mBluetoothLeService.enableIndications(mLightCharacteristic);
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

    public void onDestroy() {
        mContext.unbindService(mServiceConnection);
        mContext.unregisterReceiver(mGattUpdateReceiver);
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
            Log.i(TAG, "Bluetooth action = " + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                ToastUtils.showToast("蓝牙已连接");
                isConnect = true;
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
                    isSelectColor = true;
                }else if (dataLength == 5){
                    isSelectColor = false;
                }
//
//                //蓝牙收到的数据
//                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
//
//                String lightOnOff = data.substring(0,2);
//                String lightColor = data.substring(2,4);
//
//                if (lightOnOff.equals("01")){
//                    isSelectColor = true;
//                    mLightOn = false;
//                }else{
//                    isSelectColor = false;
//                    mLightOn = true;
//                }
//                curLightRank = Integer.valueOf(lightColor,16);
////                ToastUtils.showToast("data: " + data + ",lightColor: " + lightColor);
//                Log.i("tomw","curLightRank: " + curLightRank);
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
