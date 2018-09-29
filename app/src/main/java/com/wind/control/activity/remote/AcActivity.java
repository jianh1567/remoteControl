package com.wind.control.activity.remote;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wind.control.MyApplication;
import com.wind.control.R;
import com.wind.control.base.BaseActivity;
import com.wind.control.model.DeviceBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.StringCallback;
import com.wind.control.util.BaseInfoSPUtil;
import com.wind.control.util.FileUtils;
import com.wind.control.util.MessageUtil;
import com.wind.control.widget.SinglePopwindows;

import net.irext.decodesdk.IRDecode;
import net.irext.decodesdk.bean.ACStatus;
import net.irext.decodesdk.utils.Constants;
import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.RemoteIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 作者：Created by luow on 2018/8/8
 * 注释：
 */
public class AcActivity extends BaseActivity {
    private static final String TAG = "AcActivity";
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.rl)
    RelativeLayout mRl;
    @BindView(R.id.tv_du)
    TextView mTvDu;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_mode)
    LinearLayout mLlMode;
    @BindView(R.id.ll_power)
    LinearLayout mLlPower;
    @BindView(R.id.ll_saofeng)
    LinearLayout mLlSaofeng;
    @BindView(R.id.ll_hot)
    LinearLayout mLlHot;
    @BindView(R.id.ll_add)
    LinearLayout mLlAdd;
    @BindView(R.id.ll_fengxiang)
    LinearLayout mLlFengxiang;
    @BindView(R.id.ll_zhilen)
    LinearLayout mLlZhilen;
    @BindView(R.id.ll_down)
    LinearLayout mLlDown;
    @BindView(R.id.ll_fengliang)
    LinearLayout mLlFengliang;
    @BindView(R.id.ll_text)
    LinearLayout mLlText;
    @BindView(R.id.rll)
    RelativeLayout mRll;
    private MyApplication mApp;
    private List<RemoteIndex> mIndexes;
    private int mId;
    private int mBrandId;
    private MsgHandler mMsgHandler;

    private static final int CMD_REFRESH_INDEX_LIST = 0;
    private static final int CMD_DOWNLOAD_BIN_FILE = 1;
    private static final int CMD_BIN_FILE_DOWNLOADED = 2;
    private static final int CMD_SAVE_REMOTE_CONTROL = 3;
    private InputStream mBinStream;
    private RemoteIndex mCurrentIndex;
    public static int mIndex = 0;
    private IRDecode mIRDecode;

    //电源是否已开
    private boolean IS_POWER_ON_OFF = false;
    private int temp = Constants.ACTemperature.TEMP_26.getValue();
    private int mode = Constants.ACMode.MODE_COOL.getValue();
    private int speed_auto = Constants.ACWindSpeed.SPEED_AUTO.getValue();
    private boolean isFirstClick = false;
    private boolean isFirstClick1 = true;
    private String mDevice;
    private String mName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);
        mApp = (MyApplication) getApplication();
        mIRDecode = IRDecode.getInstance();
        mMsgHandler = new MsgHandler(this);
        mIndexes = new ArrayList<>();
        mId = getIntent().getIntExtra("id", 0);
        mBrandId = getIntent().getIntExtra("brandId", 0);
        mDevice = getIntent().getStringExtra("device");
        mName = getIntent().getStringExtra("name");
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listIndexes();
    }

    private void setViewInvisible() {
        mLlText.setVisibility(View.INVISIBLE);
        IS_POWER_ON_OFF = false;
//        temp = Constants.ACTemperature.TEMP_26.getValue();
//        mode = Constants.ACMode.MODE_COOL.getValue();
//        speed_auto = Constants.ACWindSpeed.SPEED_AUTO.getValue();
    }

    @OnClick({R.id.ll_mode, R.id.ll_power,
            R.id.ll_saofeng, R.id.ll_hot, R.id.ll_add, R.id.ll_fengxiang,
            R.id.ll_zhilen, R.id.ll_down, R.id.ll_fengliang})
    public void onClick(View view) {
        showPop(view);
        int inputKeyCode = 0;
        ACStatus acStatus = new ACStatus();
        switch (view.getId()) {
            case R.id.ll_mode:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);

                //模式
                if (mode == Constants.ACMode.MODE_COOL.getValue()) {
                    mode = Constants.ACMode.MODE_HEAT.getValue();
                } else if (mode == Constants.ACMode.MODE_HEAT.getValue()) {
                    mode = Constants.ACMode.MODE_AUTO.getValue();
                } else if (mode == Constants.ACMode.MODE_AUTO.getValue()) {
                    mode = Constants.ACMode.MODE_FAN.getValue();
                } else if (mode == Constants.ACMode.MODE_FAN.getValue()) {
                    mode = Constants.ACMode.MODE_DEHUMIDITY.getValue();
                } else if (mode == Constants.ACMode.MODE_DEHUMIDITY.getValue()) {
                    mode = Constants.ACMode.MODE_COOL.getValue();
                }
                inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_power:
                showRemote();
                if (!IS_POWER_ON_OFF) {
                    IS_POWER_ON_OFF = true;
                    mLlText.setVisibility(View.VISIBLE);
                    //打开空调
                    inputKeyCode = Constants.ACFunction.FUNCTION_SWITCH_POWER.getValue();
                    acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                    acStatus.setACMode(mode);
                    acStatus.setACTemp(temp);
                    acStatus.setACWindSpeed(speed_auto);
                    acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                    acStatus.setACDisplay(0);
                    acStatus.setACTimer(0);
                    acStatus.setACSleep(0);
                } else {
                    IS_POWER_ON_OFF = false;
                    mLlText.setVisibility(View.INVISIBLE);
                    //关闭空调
                    inputKeyCode = Constants.ACFunction.FUNCTION_SWITCH_POWER.getValue();
                    acStatus.setACPower(Constants.ACPower.POWER_OFF.getValue());
                    acStatus.setACMode(mode);
                    acStatus.setACTemp(temp);
                    acStatus.setACWindSpeed(speed_auto);
                    acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                    acStatus.setACDisplay(0);
                    acStatus.setACTimer(0);
                    acStatus.setACSleep(0);
                }
                break;
            case R.id.ll_saofeng:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);
                //扫风
                mode = Constants.ACMode.MODE_FAN.getValue();
                inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_hot:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);
                //制热
                mode = Constants.ACMode.MODE_HEAT.getValue();
                inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_add:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);

                if (temp == Constants.ACTemperature.TEMP_16.getValue()) {
                    temp = Constants.ACTemperature.TEMP_17.getValue();
                    mTvDu.setText("17°");
                } else if (temp == Constants.ACTemperature.TEMP_17.getValue()) {
                    temp = Constants.ACTemperature.TEMP_18.getValue();
                    mTvDu.setText("18°");
                } else if (temp == Constants.ACTemperature.TEMP_18.getValue()) {
                    temp = Constants.ACTemperature.TEMP_19.getValue();
                    mTvDu.setText("19°");
                } else if (temp == Constants.ACTemperature.TEMP_19.getValue()) {
                    temp = Constants.ACTemperature.TEMP_20.getValue();
                    mTvDu.setText("20°");
                } else if (temp == Constants.ACTemperature.TEMP_20.getValue()) {
                    temp = Constants.ACTemperature.TEMP_21.getValue();
                    mTvDu.setText("21°");
                } else if (temp == Constants.ACTemperature.TEMP_21.getValue()) {
                    temp = Constants.ACTemperature.TEMP_22.getValue();
                    mTvDu.setText("22°");
                } else if (temp == Constants.ACTemperature.TEMP_22.getValue()) {
                    temp = Constants.ACTemperature.TEMP_23.getValue();
                    mTvDu.setText("23°");
                } else if (temp == Constants.ACTemperature.TEMP_23.getValue()) {
                    temp = Constants.ACTemperature.TEMP_24.getValue();
                    mTvDu.setText("24°");
                } else if (temp == Constants.ACTemperature.TEMP_24.getValue()) {
                    temp = Constants.ACTemperature.TEMP_25.getValue();
                    mTvDu.setText("25°");
                } else if (temp == Constants.ACTemperature.TEMP_25.getValue()) {
                    temp = Constants.ACTemperature.TEMP_26.getValue();
                    mTvDu.setText("26°");
                } else if (temp == Constants.ACTemperature.TEMP_26.getValue()) {
                    temp = Constants.ACTemperature.TEMP_27.getValue();
                    mTvDu.setText("27°");
                } else if (temp == Constants.ACTemperature.TEMP_27.getValue()) {
                    temp = Constants.ACTemperature.TEMP_28.getValue();
                    mTvDu.setText("28°");
                } else if (temp == Constants.ACTemperature.TEMP_28.getValue()) {
                    temp = Constants.ACTemperature.TEMP_29.getValue();
                    mTvDu.setText("29°");
                } else if (temp == Constants.ACTemperature.TEMP_29.getValue()) {
                    temp = Constants.ACTemperature.TEMP_30.getValue();
                    mTvDu.setText("30°");
                } else if (temp == Constants.ACTemperature.TEMP_30.getValue()) {

                }
                inputKeyCode = Constants.ACFunction.FUNCTION_TEMPERATURE_UP.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_down:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);

                if (temp == Constants.ACTemperature.TEMP_16.getValue()) {
                } else if (temp == Constants.ACTemperature.TEMP_17.getValue()) {
                    temp = Constants.ACTemperature.TEMP_16.getValue();
                    mTvDu.setText("16°");
                } else if (temp == Constants.ACTemperature.TEMP_18.getValue()) {
                    temp = Constants.ACTemperature.TEMP_17.getValue();
                    mTvDu.setText("17°");
                } else if (temp == Constants.ACTemperature.TEMP_19.getValue()) {
                    temp = Constants.ACTemperature.TEMP_18.getValue();
                    mTvDu.setText("18°");
                } else if (temp == Constants.ACTemperature.TEMP_20.getValue()) {
                    temp = Constants.ACTemperature.TEMP_19.getValue();
                    mTvDu.setText("19°");
                } else if (temp == Constants.ACTemperature.TEMP_21.getValue()) {
                    temp = Constants.ACTemperature.TEMP_20.getValue();
                    mTvDu.setText("20°");
                } else if (temp == Constants.ACTemperature.TEMP_22.getValue()) {
                    temp = Constants.ACTemperature.TEMP_21.getValue();
                    mTvDu.setText("21°");
                } else if (temp == Constants.ACTemperature.TEMP_23.getValue()) {
                    temp = Constants.ACTemperature.TEMP_22.getValue();
                    mTvDu.setText("22°");
                } else if (temp == Constants.ACTemperature.TEMP_24.getValue()) {
                    temp = Constants.ACTemperature.TEMP_23.getValue();
                    mTvDu.setText("23°");
                } else if (temp == Constants.ACTemperature.TEMP_25.getValue()) {
                    temp = Constants.ACTemperature.TEMP_24.getValue();
                    mTvDu.setText("24°");
                } else if (temp == Constants.ACTemperature.TEMP_26.getValue()) {
                    temp = Constants.ACTemperature.TEMP_25.getValue();
                    mTvDu.setText("25°");
                } else if (temp == Constants.ACTemperature.TEMP_27.getValue()) {
                    temp = Constants.ACTemperature.TEMP_26.getValue();
                    mTvDu.setText("26°");
                } else if (temp == Constants.ACTemperature.TEMP_28.getValue()) {
                    temp = Constants.ACTemperature.TEMP_27.getValue();
                    mTvDu.setText("27°");
                } else if (temp == Constants.ACTemperature.TEMP_29.getValue()) {
                    temp = Constants.ACTemperature.TEMP_28.getValue();
                    mTvDu.setText("28°");
                } else if (temp == Constants.ACTemperature.TEMP_30.getValue()) {
                    temp = Constants.ACTemperature.TEMP_29.getValue();
                    mTvDu.setText("29°");
                }
                inputKeyCode = Constants.ACFunction.FUNCTION_TEMPERATURE_DOWN.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_fengxiang:
                mLlText.setVisibility(View.VISIBLE);
                showRemote();
                //风向
                mode = Constants.ACMode.MODE_FAN.getValue();
                inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
            case R.id.ll_zhilen:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);
                //制冷
                mode = Constants.ACMode.MODE_COOL.getValue();
                inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);

                break;

            case R.id.ll_fengliang:
                showRemote();
                mLlText.setVisibility(View.VISIBLE);
                //风向
                if (speed_auto == Constants.ACWindSpeed.SPEED_AUTO.getValue()) {
                    speed_auto = Constants.ACWindSpeed.SPEED_HIGH.getValue();
                } else if (speed_auto == Constants.ACWindSpeed.SPEED_HIGH.getValue()) {
                    speed_auto = Constants.ACWindSpeed.SPEED_LOW.getValue();
                } else if (speed_auto == Constants.ACWindSpeed.SPEED_LOW.getValue()) {
                    speed_auto = Constants.ACWindSpeed.SPEED_MEDIUM.getValue();
                } else if (speed_auto == Constants.ACWindSpeed.SPEED_MEDIUM.getValue()) {
                    speed_auto = Constants.ACWindSpeed.SPEED_AUTO.getValue();
                }
                inputKeyCode = Constants.ACFunction.FUNCTION_SWITCH_WIND_SPEED.getValue();
                acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
                acStatus.setACMode(mode);
                acStatus.setACTemp(temp);
                acStatus.setACWindSpeed(speed_auto);
                acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
                acStatus.setACDisplay(0);
                acStatus.setACTimer(0);
                acStatus.setACSleep(0);
                break;
        }

        int[] irControl = mIRDecode.decodeBinary(inputKeyCode, acStatus, 0);
        requeatControl(irControl);
//        Log.d("lw", Arrays.toString(mIRDecode.decodeBinary(inputKeyCode, acStatus,
//                0)) + "");
//        // send decoded integer array to IR emitter
//        ConsumerIrManager irEmitter =
//                (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
//        if (irEmitter.hasIrEmitter()) {
//            if (null != irControl && irControl.length > 0) {
//                irEmitter.transmit(38000, irControl);
//            }
//        }
    }

    private void requeatControl(int cmd[]) {
        DeviceBean bean = new DeviceBean();
        bean.setDeviceName(mDevice);
        bean.setProductName("a1StZOzzL3s");
        bean.setCmd(cmd);
        bean.setToken(BaseInfoSPUtil.getInstance().getLoginToken(this));
        bean.setPhone(BaseInfoSPUtil.getInstance().getUserPhoneNum(this));
        OkHttpUtils.postString().url(Api.PUB)
                .content(new Gson().toJson(bean))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id, int code) {

                    }

                    @Override
                    public void onResponse(String response, int id, int code) {
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            String code = object.getString("code");
//                            if (code == 0000) {
//                                Toast.makeText(AcActivity.this, "控制成功", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(AcActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
    }

    private void showPop(View view) {
        if (isFirstClick) {
            SinglePopwindows singleListPopwindows = new SinglePopwindows(this);
            singleListPopwindows.setOnSingleChangeListener(new SinglePopwindows.OnSingleChangeListener() {
                @Override
                public void onNext() {
                    isFirstClick = false;
                    setViewInvisible();
                    ++mIndex;
                    Log.d("lw", "ssaabbba" + mIndex);
                    if (mIndex >= mIndexes.size()) {
                        mIndex = mIndexes.size() - 1;
                        Log.d("lw", "ai" + mIndex);

                    }
                    mCurrentIndex = mIndexes.get(mIndex);
                    mTvTitle.setText(mName + "空调(" + (mIndex + 1) + "/" + (mIndexes.size()) + ")");
                    MessageUtil.postMessage(mMsgHandler, CMD_DOWNLOAD_BIN_FILE);
                }

                @Override
                public void onPrevious() {
                    setViewInvisible();
                    --mIndex;
                    if (mIndex < 0) {
                        mIndex = 0;
                    }
                    mTvTitle.setText(mName + "空调(" + (mIndex + 1) + "/" + (mIndexes.size()) + ")");
                    mCurrentIndex = mIndexes.get(mIndex);
                    Log.d("lw", "ssaaa" + mIndex);
                    MessageUtil.postMessage(mMsgHandler, CMD_DOWNLOAD_BIN_FILE);
                }

                @Override
                public void onOk() {
                    Toast.makeText(AcActivity.this, "配置成功", Toast.LENGTH_SHORT).show();
                    isFirstClick = false;
                    showRemote();
                }
            });
            singleListPopwindows.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }

        if (isFirstClick1) {
            isFirstClick1 = false;
            isFirstClick = true;
        }

    }

    //--------------------------------------------开源库数据相关----------------------------------------------
    private WebAPICallbacks.ListIndexesCallback mListIndexesCallback = new WebAPICallbacks.ListIndexesCallback() {

        @Override
        public void onListIndexesSuccess(List<RemoteIndex> indexes) {
            mIndexes.addAll(indexes);
            Log.d("lw", "sada" + mIndexes.size());
            mCurrentIndex = mIndexes.get(mIndex);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvTitle.setText(mName + "空调(" + (mIndex + 1) + "/" + (mIndexes.size()) + ")");
                }
            });
            MessageUtil.postMessage(mMsgHandler, CMD_DOWNLOAD_BIN_FILE);
        }

        @Override
        public void onListIndexesFailed() {
            Log.w(TAG, "list indexes failed");
        }

        @Override
        public void onListIndexesError() {
            Log.e(TAG, "list indexes error");
        }
    };

    private void listIndexes() {
        new Thread() {
            @Override
            public void run() {
                mApp.mWeAPIs.listRemoteIndexes(mId,
                        mBrandId, "", "", mListIndexesCallback);
            }
        }.start();
    }

    private WebAPICallbacks.DownloadBinCallback mDownloadBinCallback = new WebAPICallbacks.DownloadBinCallback() {
        @Override
        public void onDownloadBinSuccess(InputStream inputStream) {
            Log.d(TAG, "binary file download successfully");
            mBinStream = inputStream;
            MessageUtil.postMessage(mMsgHandler, CMD_BIN_FILE_DOWNLOADED);

        }

        @Override
        public void onDownloadBinFailed() {
            Log.w(TAG, "download bin file failed");
        }

        @Override
        public void onDownloadBinError() {
            Log.w(TAG, "download bin file error");
        }
    };

    private void downloadBinFile() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String remoteMap = mCurrentIndex.getRemoteMap();
                    int indexId = mCurrentIndex.getId();
                    mApp.mWeAPIs.downloadBin(remoteMap, indexId, mDownloadBinCallback);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private boolean createDirectory() {
        File file = new File(FileUtils.BIN_PATH);
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    private void showRemote() {
        String binFileName = FileUtils.BIN_PATH + FileUtils.FILE_NAME_PREFIX +
                mCurrentIndex.getRemoteMap() + FileUtils.FILE_NAME_EXT;
        /* decode SDK - load binary file */
        int ret = mIRDecode.openFile(mCurrentIndex.getCategoryId(), mCurrentIndex.getSubCate(), binFileName);
        File binFile = new File(binFileName);
        byte[] binaries = new byte[(int) binFile.length()];
        try {
            if (null != binFile) {
                FileInputStream fin = new FileInputStream(binFile);
                fin.read(binaries);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "binary opened : " + ret);
    }

    private void saveBinFile() {
        new Thread() {
            @Override
            public void run() {
                if (createDirectory()) {
                    File binFile = new File(FileUtils.BIN_PATH +
                            FileUtils.FILE_NAME_PREFIX + mCurrentIndex.getRemoteMap() +
                            FileUtils.FILE_NAME_EXT);
                    boolean ret = FileUtils.write(binFile, mBinStream);
                    Log.d(TAG, "write bin file succeeded : " + ret);

                } else {
                    Log.w(TAG, "no directory to contain bin file");
                }

                if (null != mBinStream) {
                    MessageUtil.postMessage(mMsgHandler, CMD_SAVE_REMOTE_CONTROL);
                } else {
                    Log.e(TAG, "bin file download failed");
                }
            }
        }.start();
    }

    private static class MsgHandler extends Handler {

        WeakReference<AcActivity> mIndexFragment;

        MsgHandler(AcActivity activity) {
            mIndexFragment = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            int cmd = msg.getData().getInt(MessageUtil.KEY_CMD);

            AcActivity indexActivity = mIndexFragment.get();
            switch (cmd) {

                case CMD_REFRESH_INDEX_LIST:
                    //   indexActivity.refreshIndexes();
                    break;

                case CMD_DOWNLOAD_BIN_FILE:
                    indexActivity.downloadBinFile();
                    break;

                case CMD_BIN_FILE_DOWNLOADED:
                    indexActivity.saveBinFile();
                    break;

                case CMD_SAVE_REMOTE_CONTROL:
                    //  indexActivity.saveRemoteControl();

                default:
                    break;
            }
        }
    }

}