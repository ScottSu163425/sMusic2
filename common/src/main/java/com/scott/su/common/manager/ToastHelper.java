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
    private static ToastHelper sInstance;


    public static ToastHelper getInstance() {
        if (sInstance == null) {
            synchronized (ToastHelper.class) {
                if (sInstance == null) {
                    sInstance = new ToastHelper( );
                }
            }
        }
        return sInstance;
    }

    private ToastHelper( ) {

    }

    public void showToast(@NonNull Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(@NonNull Context context,CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

}
