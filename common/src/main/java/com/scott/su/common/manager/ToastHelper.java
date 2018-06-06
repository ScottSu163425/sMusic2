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


    public static ToastHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (ToastHelper.class) {
                if (sInstance == null) {
                    sInstance = new ToastHelper(context);
                }
            }
        }
        return sInstance;
    }

    private Context mContext;

    private ToastHelper(@NonNull Context context) {
        mContext = context.getApplicationContext();
    }

    public void showToast(CharSequence text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(CharSequence text) {
        Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
    }

}
