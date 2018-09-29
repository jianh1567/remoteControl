package com.wind.control.util;

import android.os.Environment;

import java.io.File;
import java.util.UUID;

/**
 * Created by luow on 17/1/12.
 */

public final class Constants {

    //是否关闭log
    public static final boolean IS_DEBUG = true;

    //统一log名
    public static final String DEBUG_TAG = "RemoteControl";

    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";

    public static final int ARICCODE = 1;
    public static final int LIGHTCODE = 2;

    public static final String APP_NAME = "RemoteControl.apk";
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "RemoteControl";

    public static final UUID LED_LIGHT_WRITE_UUID
            = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final UUID LED_LIGHT_READ_UUID
            = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static final UUID LED_LIGHT_CCCD_UUID
            = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}
