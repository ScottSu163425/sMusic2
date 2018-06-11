package com.scott.su.common.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class ToastHelper {
    private ToastHelper() {

    }

    public static void showToast(@NonNull Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(@NonNull Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
