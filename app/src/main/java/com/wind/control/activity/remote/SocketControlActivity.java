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
import android.widget.TextView;

import com.wind.control.R;
import com.wind.control.dialog.LoadingDialog;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.BleProtocol;
import com.wind.control.util.BluetoothLeService;
import com.wind.control.util.LogUtils;
import com.wind.control.util.ToastUtils;

import java.util.List;

import static com.wind.control.util.Constants.LED_LIGHT_READ_UUID;
import static com.wind.control.util.Constants.LED_LIGHT_WRITE_UUID;
import static com.wind.control.util.ScanRecordUtil.bytesToHex;

/**
 * @author wangtongming
 * @create 2018/9/18
 * @Describe 插座控制界面
 */
public class SocketControlActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "SocketControlActivity";
    private static final int TOME_OUT = 6000;

    private ImageView mImageBack;
    private TextView mTitleName;
    private Button mNightStatus;
    private String mDeviceName;

    private boolean isConnect = false;
    private boolean isSelectColor = false;

    private BluetoothGattCharacteristic mWriteCharacteristic,mReadCharacteristic;
    private BluetoothLeService mBluetoothLeService;
    private LoadingDialog mLoadingDialog;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_control_layout);

        mImageBack = (ImageView)findViewById(R.id.tv_back);
        mImageBack.setOnClickListener(this);

        mTitleName = (TextView)findViewById(R.id.title_bar_context);
        mTitleName.setText("智能插座控制");

        mNightStatus = (Button) findViewById(R.id.socket_control_switch);
        mNightStatus.setOnClickListener(this);
        mNightStatus.setBackgroundResource(R.drawable.ble_light_off);

        mLoadingDialog = new LoadingDialog(this, R.style.LoadingDialog, "");
        mLoadingDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingDialog.dismiss();
            }
        }, TOME_OUT);

        mDeviceName = getIntent().getStringExtra("mac");

//        mDeviceName = BaseInfoSPUtil.getInstance().getConnectName(this);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

    }

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
            case R.id.socket_control_switch:
            {
                if (!isSelectColor) {
                    isSelectColor = true;
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_on);
                    lightControlSendOrder(BleProtocol.BleSendOnOff(true));
                } else {
                    isSelectColor = false;
                    mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
                    lightControlSendOrder(BleProtocol.BleSendOnOff(false));
                }
                break;
            }
            default:break;
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
            mBluetoothLeService.connect(mDeviceName);
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
                LogUtils.i("BluetoothLeService:" + bluetoothGattCharacteristic.getUuid());
                if (bluetoothGattCharacteristic.getUuid().equals(LED_LIGHT_WRITE_UUID)) {
                    mWriteCharacteristic = bluetoothGattCharacteristic;
                } else if (bluetoothGattCharacteristic.getUuid().equals(LED_LIGHT_READ_UUID)) {
                    mReadCharacteristic = bluetoothGattCharacteristic;
                    mBluetoothLeService.enableIndications(mReadCharacteristic);
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


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i("BluetoothLeService","ble action: " + action);
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
                    mBluetoothLeService.connect(mDeviceName);
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //蓝牙收到的数据
                byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                int dataLength = data.length;
                if (data.length == 5){
                    if (data[4] == 1){
                        mNightStatus.setBackgroundResource(R.drawable.ble_light_on);
                        isSelectColor = true;
                    }else if (data[4] == 0){
                        isSelectColor = false;
                        mNightStatus.setBackgroundResource(R.drawable.ble_light_off);
                    }
                }

                Log.i(TAG,"data: " + bytesToHex(data) + ",dataLength: " + dataLength);
            }

            int bleState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);

            if (BluetoothAdapter.STATE_OFF == bleState) {

            } else if (BluetoothAdapter.STATE_ON == bleState) {
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.connect(mDeviceName);
                }
            }
        }
    };
}
