package com.wind.control.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wind.control.MyApplication;
import com.wind.control.activity.remote.AcActivity;
import com.wind.control.model.DeviceBean;
import com.wind.control.okhttp.OkHttpUtils;
import com.wind.control.okhttp.api.Api;
import com.wind.control.okhttp.callback.StringCallback;
import com.wind.control.util.FileUtils;
import com.wind.control.util.MessageUtil;

import net.irext.decodesdk.IRDecode;
import net.irext.decodesdk.bean.ACStatus;
import net.irext.decodesdk.utils.Constants;
import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.RemoteIndex;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by 1003373 on 2018/9/5.
 */

public class AircControlManager {
    private static final String TAG = "AircControlManager";
    private MyApplication mApp;
    private List<RemoteIndex> mIndexes;
    private int mId;
    private int mBrandId;

    private static final int CMD_REFRESH_INDEX_LIST = 0;
    private static final int CMD_DOWNLOAD_BIN_FILE = 1;
    private static final int CMD_BIN_FILE_DOWNLOADED = 2;
    private static final int CMD_SAVE_REMOTE_CONTROL = 3;
    private InputStream mBinStream;
    private RemoteIndex mCurrentIndex;
    private IRDecode mIRDecode;
    private boolean IS_POWER_ON_OFF = false;
    private int temp = Constants.ACTemperature.TEMP_26.getValue();
    private int mode = Constants.ACMode.MODE_COOL.getValue();
    private int speed_auto = Constants.ACWindSpeed.SPEED_AUTO.getValue();
    private boolean isFirstClick = false;
    private boolean isFirstClick1 = true;
    private String mDevice;
    private String mName;
    private Context mContext;

    private MsgHandler mMsgHandler;

    public AircControlManager(Context context, String device, int categoryId, int brandId){
        mContext = context;
        mApp = (MyApplication) context.getApplicationContext();
        mIRDecode = IRDecode.getInstance();
        mMsgHandler = new MsgHandler();
        mIndexes = new ArrayList<>();
        mDevice = device;
        mId = categoryId;
        mBrandId = brandId;
    }

    public void startControl(){
        listIndexes();
    }

    private void startAircControl(){
        int inputKeyCode = 0;
        ACStatus acStatus = new ACStatus();
        showRemote();
        acStatus.setACPower(Constants.ACPower.POWER_ON.getValue());
        acStatus.setACMode(mode);
        acStatus.setACTemp(temp);
        acStatus.setACWindSpeed(speed_auto);
        acStatus.setACWindDir(Constants.ACSwing.SWING_ON.getValue());
        acStatus.setACDisplay(0);
        acStatus.setACTimer(0);
        acStatus.setACSleep(0);

        inputKeyCode = Constants.ACFunction.FUNCTION_CHANGE_MODE.getValue();
        int[] irControl = mIRDecode.decodeBinary(inputKeyCode, acStatus, 0);
        requeatControl(irControl);
        Toast.makeText(mContext, "开始控制" + mDevice , Toast.LENGTH_SHORT).show();
    }

    private void requeatControl(int cmd[]) {
        DeviceBean bean = new DeviceBean();
        bean.setDeviceName(mDevice);
        bean.setProductName("a1StZOzzL3s");
        bean.setCmd(cmd);
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
                      /*  try {
                            JSONObject object = new JSONObject(response);
                            String code = object.getString("code");
                            if (code == 0000) {
                                Toast.makeText(mContext, "控制成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
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

    private void listIndexes() {
        new Thread() {
            @Override
            public void run() {
                mApp.mWeAPIs.listRemoteIndexes(mId,
                        mBrandId, "", "", mListIndexesCallback);
            }
        }.start();
    }

    //--------------------------------------------开源库数据相关----------------------------------------------
    private WebAPICallbacks.ListIndexesCallback mListIndexesCallback = new WebAPICallbacks.ListIndexesCallback() {

        @Override
        public void onListIndexesSuccess(List<RemoteIndex> indexes) {
            mIndexes.addAll(indexes);
            Log.d("lw", "sada" + mIndexes.size());
            mCurrentIndex = mIndexes.get(AcActivity.mIndex);
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

    private  class MsgHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int cmd = msg.getData().getInt(MessageUtil.KEY_CMD);

            switch (cmd) {

                case CMD_REFRESH_INDEX_LIST:
                    //   indexActivity.refreshIndexes();
                    break;

                case CMD_DOWNLOAD_BIN_FILE:
                    downloadBinFile();
                    break;

                case CMD_BIN_FILE_DOWNLOADED:
                    saveBinFile();
                    break;

                case CMD_SAVE_REMOTE_CONTROL:
                    startAircControl();
                default:
                    break;
            }
        }
    }
}
