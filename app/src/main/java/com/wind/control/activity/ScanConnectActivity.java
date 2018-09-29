package com.wind.control.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wind.control.R;
import com.wind.control.adapter.BleDeviceListAdapter;
import com.wind.control.model.HttpResponse;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.service.BindDeviceRequest;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.BluetoothLeService;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.LogUtils;
import com.wind.control.util.NetUtil;
import com.wind.control.util.NoLoginUtils;
import com.wind.control.util.ScanRecordUtil;
import com.wind.control.view.RippleView;
import com.wind.control.widget.ConfirmDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.wind.control.util.ToastUtils.showToast;

/**
 * 作者：Created by luow on 2018/7/12
 * 注释：蓝牙搜索连接界面
 */
public class ScanConnectActivity extends AppCompatActivity implements BleDeviceListAdapter.OnRecycleOnDeviceClickListener {

    @BindView(R.id.ripple_view)
    RippleView mRippleView;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rcv_ble_device)
    RecyclerView mRcvBleDevice;

    private Handler mHandler;
    private Handler mLoadHandle;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private static final long LOADING_TIME = 4000;
    private BleDeviceListAdapter mAdapter;
    private ArrayList<BluetoothDevice> mLeDevices;
    private ArrayList<String> mDeviceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_connect);
        ButterKnife.bind(this);
        mRippleView.startRippleAnimation();
        mLoadHandle = new Handler();

        mLoadHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRippleView.stopRippleAnimation();
                mLlSearch.setVisibility(View.GONE);
                mRcvBleDevice.setVisibility(View.VISIBLE);
            }
        }, LOADING_TIME);

        mLeDevices = new ArrayList<>();
        mDeviceType = new ArrayList<String>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvBleDevice.setLayoutManager(linearLayoutManager);
        mAdapter = new BleDeviceListAdapter(this, mLeDevices,mDeviceType);
        mAdapter.setRecycleOnDeviceClickListener(this);
        mRcvBleDevice.setAdapter(mAdapter);
        mHandler = new Handler();

    }

    @OnClick({R.id.tv_cancle, R.id.iv_left, R.id.tv_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                mRippleView.stopRippleAnimation();
                onBackPressed();
                break;
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_scan:

                mLeDevices.clear();
                scanLeDevice(true);

                break;
        }
    }

    @Override
    public void onClickDevice(View view, final int position) {
        if (TextUtils.isEmpty(BaseInfoSPUtil.getInstance().getLoginToken(this))){
            NoLoginUtils.isLogin(this);
            return;
        }
        final ConfirmDialog dialog = new ConfirmDialog(this);
        dialog.builder().setCancelable(false).setTitle("给设备起个名字吧").
                setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(dialog.getMoney())){
                            showToast("给设备起个名字吧");
                        }else{
                            dialog.hideInputMethod();
                            dialog.closeDialog();
                            String typeName = null;
                            String type = null;
                            if (mDeviceType.get(position).equals("01")){
                                typeName = "七彩灯";
                                type = "1";
                            }else if (mDeviceType.get(position).equals("02")){
                                typeName = "智能插座";
                                type = "3";
                            }else{
                                typeName = "七彩灯";
                                type = "1";
                            }
                            requestBindDevice(mLeDevices.get(position).getAddress(), dialog.getMoney(),typeName,type);
                        }
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hideInputMethod();
                dialog.closeDialog();
            }
        }).show();
    }

    /**
     * 绑定设备
     */
    private void requestBindDevice(String mac, String title,String typeName,String type){
        BindDeviceRequest request = new BindDeviceRequest();
        request.setType(type);//1 蓝牙  2 遥控
        request.setMac(mac);
        request.setTitle(title);
        request.setName(typeName);
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        request.setBrandid(0);
        request.setCategoryid(0);
        request.setIr_devicename("");
        OkHttpUtils.postString()
                .url(Api.BIND_DEVICE)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(ScanConnectActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {

                        String mCode = response.getCode();
                        if (mCode.equals("1000")) {
                            showToast(response.getMsg());
                            finish();
                        } else{
                            showToast(response.getMsg());
                        }
                    }
                });
    }

    //--------------------------------------------ble蓝牙模块相关-------------------------------------
    private BluetoothLeService mBluetoothLeService;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
            }
            // Automatically connects to the device upon successful start-up initialization.
            //  mBluetoothLeService.connect(BaseInfoSPUtil.getInstance().getConnectName(getActivity()));
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
                showToast("已连接");
                BaseInfoSPUtil.getInstance().setConnectSuccess(ScanConnectActivity.this, "true");
                if (!isFinishing()) {
                    finish();
                }
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnect = false;
                handler.removeCallbacks(delayCheckRunnable);
                handler.postDelayed(delayCheckRunnable, 5 * 1000);
                if (!isNoConnect) {
                    if (mBluetoothLeService != null) {
                        showToast("正在尝试再次连接");
                        mBluetoothLeService.connect(BaseInfoSPUtil.getInstance().getConnectName(ScanConnectActivity.this));
                    }
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        if (mGattUpdateReceiver != null){
            unregisterReceiver(mGattUpdateReceiver);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!mLeDevices.contains(device) && !TextUtils.isEmpty(device.getName())) {
                                byte[] scanData = ScanRecordUtil.parseFromByte(scanRecord);
                                String deviceInfo = ScanRecordUtil.bytesToHex(scanData);
                                Log.i("BLESCAN","len: " + deviceInfo.length() + ",deviceInfo: " + deviceInfo);
                                if (deviceInfo != null  && deviceInfo.length() == 26){
                                    String company = deviceInfo.substring(0,4);
                                    String version = deviceInfo.substring(16,deviceInfo.length() - 2 );
                                    String type = deviceInfo.substring(deviceInfo.length() - 2,deviceInfo.length());
                                    Log.i("BLESCAN","company: " + company + ",version: " + version);
                                    mLeDevices.add(device);
                                    mDeviceType.add(type);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    });
                }
            };

    private boolean isConnect;
    private boolean isNoConnect = false;
    Runnable delayCheckRunnable = new Runnable() {

        @Override
        public void run() {
            if (isConnect) {
                LogUtils.d(" 5s 之内，又重连上了，这是误报，不进行报警");
            } else {
                LogUtils.d("5s 之后。还是断线， 进行报警");
                isNoConnect = true;
                showToast("蓝牙连接失败，无法连接");
            }

        }
    };

}