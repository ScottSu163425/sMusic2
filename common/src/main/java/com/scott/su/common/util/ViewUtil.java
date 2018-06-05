package com.scott.su.common.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

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
        return location[1];
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    public static int getScreenHeightPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels; // 屏幕高度（像素）
        return height;
    }

    public static int getScreenWidthPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        return width;
    }

}
