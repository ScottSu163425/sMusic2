package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/5
 */

public class AppConfig {
    private static final String KEY_FIRST_TIME_LAUNCH = "KEY_FIRST_TIME_LAUNCH";

    private static final String KEY_REPEAT_MODE = "KEY_REPEAT_MODE";

    private static final int VALUE_REPEAT_ALL = 0;
    private static final int VALUE_REPEAT_ONE = 1;
    private static final int VALUE_REPEAT_SHUFFLE = 2;

    private static SharedPreferences sSharedPreferences;

    private AppConfig() {

    }

    private static void initIfNeed(@NonNull Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }

    public static void setFirstTimeLaunch(Context context, boolean flag) {
        initIfNeed(context);

        sSharedPreferences.edit()
                .putBoolean(KEY_FIRST_TIME_LAUNCH, flag)
                .apply();
    }

    public static boolean isFirstTimeLaunch(Context context) {
        initIfNeed(context);

        return sSharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    public static void setRepeatAll(Context context) {
        initIfNeed(context);

        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL)
                .apply();
    }

    public static void setRepeatOne(Context context) {
        initIfNeed(context);

        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_ONE)
                .apply();
    }

    public static void setRepeatShuffle(Context context) {
        initIfNeed(context);

        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_SHUFFLE)
                .apply();
    }

    public static boolean isRepeatAll(Context context) {
        initIfNeed(context);

        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_ALL;
    }

    public static boolean isRepeatOne(Context context) {
        initIfNeed(context);

        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_ONE;
    }

    public static boolean isRepeatShuffle(Context context) {
        initIfNeed(context);

        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_SHUFFLE;
    }


}
