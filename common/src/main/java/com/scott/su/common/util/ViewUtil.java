package com.scott.su.common.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/8
 */

public class ViewUtil {
    private static long sLastClickTime;

    public static boolean isFastDoubleClick() {
        long currentClickTime = System.currentTimeMillis();

        if ((currentClickTime - sLastClickTime) < 500) {
            return true;
        }

        sLastClickTime = currentClickTime;
        return false;
    }

    public static boolean isViewVisible(@NonNull View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static boolean isViewInVisible(@NonNull View view) {
        return view.getVisibility() == View.INVISIBLE;
    }

    public static boolean isViewGone(@NonNull View view) {
        return view.getVisibility() == View.GONE;
    }

    public static void setViewVisible(@NonNull View view) {
        if (isViewVisible(view)) {
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void setViewInVisible(@NonNull View view) {
        if (isViewInVisible(view)) {
            return;
        }
        view.setVisibility(View.INVISIBLE);
    }

    public static void setViewGone(@NonNull View view) {
        if (isViewGone(view)) {
            return;
        }
        view.setVisibility(View.GONE);
    }

    public static void setText(@NonNull TextView textView, String text) {
        setText(textView,text,"");
    }

    public static void setText(@NonNull TextView textView, String text, String defaultText) {
        textView.setText(TextUtils.isEmpty(text) ? defaultText : text);
    }

    public static void runDelay(@NonNull View view, @NonNull Runnable action, long delayMills) {
        view.postDelayed(action, delayMills);
    }

}
