package com.wind.control.activity.device;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspAES;
import com.espressif.iot.esptouch.util.EspNetUtil;
import com.google.gson.Gson;
import com.google.zxing.activity.CaptureActivity;
import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.HttpResponse;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.GenericsCallback;
import com.wind.control.okhttp.request.DeviceRegisterRequest;
import com.wind.control.util.AppManager;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.Constants;
import com.wind.control.util.JsonGenericsSerializator;
import com.wind.control.util.NetUtil;
import com.wind.control.view.IOSAlertDialog;
import com.wind.control.widget.ConfirmDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 作者：Created by luow on 2018/9/13
 * 注释：
 */
public class StartDmsActivity extends BaseActivity {
    private static final String TAG = "StartDmsActivity";

    private static final boolean AES_ENABLE = false;
    private static final String AES_SECRET_KEY = "1234567890123456"; // TODO modify your own key

    @BindView(R.id.ll_left)
    LinearLayout mLlLeft;
    @BindView(R.id.tv_start_dms)
    TextView mTvStartDms;
    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar1;
    @BindView(R.id.tv_device_register)
    TextView mTvDeviceRegister;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar2;
    @BindView(R.id.tv_add_device)
    TextView mTvAddDevice;
    @BindView(R.id.btn_scan)
    Button mBtnScan;

    private byte[] mSsid;
    private String mBssid;
    private String mPwd;
    private EsptouchAsyncTask4 mTask;

    private IEsptouchListener myListener = new IEsptouchListener() {

        @Override
        public void onEsptouchResultAdded(final IEsptouchResult result) {
            onEsptoucResultAddedPerform(result);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_dms);
        mSsid = getIntent().getByteArrayExtra("ssid");
        mBssid = getIntent().getStringExtra("bssid");
        mPwd = getIntent().getStringExtra("pwd");
        startDms();
    }

    private void startDms() {
        byte[] ssid = mSsid;
        byte[] password = ByteUtil.getBytesByString(mPwd);
        byte[] bssid = EspNetUtil.parseBssid2bytes(mBssid);
        byte[] deviceCount = "1".getBytes();

        if (mTask != null) {
            mTask.cancelEsptouch();
        }
        mTask = new EsptouchAsyncTask4(this);
        mTask.execute(ssid, bssid, password, deviceCount);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancelEsptouch();
            mTask.cancel(true);
        }
    }

    @OnClick({R.id.ll_left, R.id.btn_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                onBackPressed();
                break;
            case R.id.btn_scan:
                Intent it = new Intent(this, CaptureActivity.class);
                startActivityForResult(it, Constants.REQ_QR_CODE);
                break;
        }
    }

    private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                String text = result.getBssid() + " is connected to the wifi";
                Toast.makeText(StartDmsActivity.this, text,
                        Toast.LENGTH_LONG).show();
            }

        });
    }


    private static class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
        private WeakReference<StartDmsActivity> mActivity;

        // without the lock, if the user tap confirm and cancel quickly enough,
        // the bug will arise. the reason is follows:
        // 0. task is starting created, but not finished
        // 1. the task is cancel for the task hasn't been created, it do nothing
        // 2. task is created
        // 3. Oops, the task should be cancelled, but it is running
        private final Object mLock = new Object();
        //   private ProgressDialog mProgressDialog;
        //   private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;


        EsptouchAsyncTask4(StartDmsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        void cancelEsptouch() {
            cancel(true);
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            StartDmsActivity activity = mActivity.get();
            activity.mTvStartDms.setText("设备正在配网中...");
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            StartDmsActivity activity = mActivity.get();
            int taskResultCount;
            synchronized (mLock) {
                // !!!NOTICE
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
                Context context = activity.getApplicationContext();
                if (AES_ENABLE) {
                    byte[] secretKey = AES_SECRET_KEY.getBytes();
                    EspAES aes = new EspAES(secretKey);
                    mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, aes, context);
                } else {
                    mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, null, context);
                }
                mEsptouchTask.setEsptouchListener(activity.myListener);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            final StartDmsActivity activity = mActivity.get();
            if (result == null) {
                Intent it = new Intent(activity, BindFailActivity.class);
                activity.startActivity(it);
                activity.finish();
                AppManager.getInstance().finishActivity(ConfirmWifiActivity.class);
                AppManager.getInstance().finishActivity(BindDeviceActivity.class);
                return;
            }

            IEsptouchResult firstResult = result.get(0);
            // check whether the task is cancelled and no results received
            if (!firstResult.isCancelled()) {
                int count = 0;
                // max results to be displayed, if it is more than maxDisplayCount,
                // just show the count of redundant ones
                final int maxDisplayCount = 5;
                // the task received some results including cancelled while
                // executing before receiving enough results
                if (firstResult.isSuc()) {
                    StringBuilder sb = new StringBuilder();
                    for (IEsptouchResult resultInList : result) {
                        sb.append("Esptouch success, bssid = ")
                                .append(resultInList.getBssid())
                                .append(", InetAddress = ")
                                .append(resultInList.getInetAddress().getHostAddress())
                                .append("\n");
                        count++;
                        if (count >= maxDisplayCount) {
                            break;
                        }
                    }
                    if (count < result.size()) {
                        sb.append("\nthere's ")
                                .append(result.size() - count)
                                .append(" more result(s) without showing\n");
                    }
                    activity.mProgressBar1.setVisibility(View.GONE);
                    activity.mTvStartDms.setText("设备配网成功");
                    IOSAlertDialog iosAlertDialog = new IOSAlertDialog(activity);
                    iosAlertDialog.builder().setCancelable(false).setTitle("温馨提示")
                            .setMessage("设备配网成功，请扫描设备左上角的二维码，将设备注册到云端")
                            .setNegativeButton("去扫描", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent it = new Intent(activity, CaptureActivity.class);
                                    activity.startActivityForResult(it, Constants.REQ_QR_CODE);
                                }
                            }).show();

                } else {
                    //失败
                    Intent it = new Intent(activity, BindFailActivity.class);
                    activity.startActivity(it);
                    activity.finish();
                    AppManager.getInstance().finishActivity(ConfirmWifiActivity.class);
                    AppManager.getInstance().finishActivity(BindDeviceActivity.class);
                }
            }
            activity.mTask = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constants.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constants.INTENT_EXTRA_KEY_QR_SCAN);
            Log.i(TAG, "scanResult = " + scanResult);
            if (scanResult.contains("ProductKey")) {
                try {
                    mProgressBar2.setVisibility(View.VISIBLE);
                    JSONObject object = new JSONObject(scanResult);
                    final String productKey = object.getString("ProductKey");
                    final String deviceName = object.getString("DeviceName");
                    final ConfirmDialog dialog = new ConfirmDialog(StartDmsActivity.this);
                    dialog.builder().setCancelable(false)
                            .setVisible()
                            .setTitle("给设备起个名字吧").
                            setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (TextUtils.isEmpty(dialog.getMoney())) {
                                        showToast("给设备起个名字吧");
                                    } else {
                                        dialog.hideInputMethod();
                                        dialog.closeDialog();
                                        requestRegister(productKey, deviceName, dialog.getMoney());
                                    }
                                }
                            }).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                mBtnScan.setVisibility(View.VISIBLE);
                showToast("此二维码不合法，请重新去扫描");
            }
        } else {
            mBtnScan.setVisibility(View.VISIBLE);
        }
    }

    private void requestRegister(String productKey, String deviceName, String title) {
        DeviceRegisterRequest request = new DeviceRegisterRequest();
        request.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        request.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        request.setDeviceName(deviceName);
        request.setProductKey(productKey);
        request.setTriadTitle(title);
        OkHttpUtils.postString()
                .url(Api.ADD_TRIAD)
                .content(new Gson().toJson(request))
                .build()
                .execute(new GenericsCallback<HttpResponse>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {
                        if (!NetUtil.isNetworkAvailable(StartDmsActivity.this)) {
                            showToast(getResources().getString(R.string.isNetWork));
                            mProgressBar2.setVisibility(View.GONE);
                            mTvAddDevice.setText("设备添加失败");
                            Intent it = new Intent(StartDmsActivity.this, BindFailActivity.class);
                            startActivity(it);
                            finish();
                            AppManager.getInstance().finishActivity(ConfirmWifiActivity.class);
                            AppManager.getInstance().finishActivity(BindDeviceActivity.class);
                        }
                    }

                    @Override
                    public void onResponse(HttpResponse response, int id, int code) {
                        mProgressBar2.setVisibility(View.GONE);
                        mBtnScan.setVisibility(View.GONE);
                        mTvAddDevice.setText("设备添加成功");
                        showToast("设备添加成功");
                        finish();
                        AppManager.getInstance().finishActivity(ConfirmWifiActivity.class);
                        AppManager.getInstance().finishActivity(BindDeviceActivity.class);
                    }
                });
    }
}