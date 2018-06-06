package com.scott.su.common.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class KeyboardUtil {

    private KeyboardUtil() {
    }

    /**
     * 关闭系统软键盘
     */
    public static void closeKeyboard(@NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            IBinder token = activity.getWindow().getDecorView().getWindowToken();
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

}
