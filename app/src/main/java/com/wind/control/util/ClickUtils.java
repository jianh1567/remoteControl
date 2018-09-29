package com.wind.control.util;

/**
 * 点击事件上游处理工具
 * Created by luow on 2017/12/7.
 */

public class ClickUtils {
    private static long sLastClickTime;

    /**
     * 是否为连续点击
     *
     * @return
     */
    public static boolean isContinual() {
        long current = System.currentTimeMillis();
        if (current - sLastClickTime <= 400) {
            return true;
        }
        sLastClickTime = current;
        return false;
    }
}
