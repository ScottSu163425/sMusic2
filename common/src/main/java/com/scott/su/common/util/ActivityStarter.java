package com.scott.su.common.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Pair;
import android.view.View;

/**
 * 描述: Activity启动辅助类
 * 作者: Su
 * 日期: 2018/5/3
 */

public class ActivityStarter {


    public static void startWithSharedElements(Context context, Intent intent,
                                               @Nullable View[] sharedElements) {
        if (context == null || intent == null) {
            return;
        }

        if (!isAfterLollipop()
                || !(context instanceof Activity)
                || sharedElements == null
                || sharedElements.length == 0) {

            context.startActivity(intent);
            return;
        }

        Pair<View, String>[] pairs = new Pair[sharedElements.length];
        for (int i = 0; i < sharedElements.length; i++) {
            pairs[i] = new Pair<>(sharedElements[i], ViewCompat.getTransitionName(sharedElements[i]));
        }

        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation((Activity) context, pairs);

        context.startActivity(intent, options.toBundle());
    }


    private static boolean isAfterLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
