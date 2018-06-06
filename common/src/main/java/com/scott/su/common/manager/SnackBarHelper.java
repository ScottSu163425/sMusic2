package com.scott.su.common.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class SnackBarHelper {
    private static SnackBarHelper sInstance;


    public static SnackBarHelper getInstance(){
        if (sInstance == null)    {
            synchronized (SnackBarHelper.class){
                if ( sInstance == null) {
                    sInstance = new SnackBarHelper();
                }
            }
        }
        return sInstance;
    }


    private SnackBarHelper(){

    }

    public void showSnackBar(@NonNull View parent, @NonNull CharSequence text) {
        showSnackBar(parent, text,null,null);
    }

    public void showSnackBar(@NonNull View parent, @NonNull CharSequence text, @Nullable CharSequence action,
                                @Nullable View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(parent, text, Snackbar.LENGTH_SHORT);

        if (!TextUtils.isEmpty(action) && actionClickListener != null) {
            snackbar.setAction(action, actionClickListener);
        }

        snackbar.show();
    }

}
