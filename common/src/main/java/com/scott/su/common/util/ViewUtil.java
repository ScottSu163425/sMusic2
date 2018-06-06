package com.scott.su.common.util;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/8
 */

public class ViewUtil {
    private static long sLastClickTime;

    public static boolean isFastDoubleClick() {
        long currentClickTime = System.currentTimeMillis();

        if ((currentClickTime - sLastClickTime) < 1000) {
            return true;
        }

        sLastClickTime = currentClickTime;
        return false;
    }



}
