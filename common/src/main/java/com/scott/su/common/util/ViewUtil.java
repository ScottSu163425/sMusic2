package com.scott.su.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/8
 */

public class ViewUtil {

    public static int getXOnScreen(@NonNull View view) {
        int[] location = new int[2];
//        view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        return location[0];
    }

    public static int getYOnScreen(@NonNull View view) {
        int[] location = new int[2];
//        view.getLocationInWindow(location);
        view.getLocationOnScreen(location);
        return location[0];
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
