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
    private static final String KEY_NIGHT_MODE_ON = "KEY_NIGHT_MODE_ON";

    private static final int VALUE_REPEAT_ALL = 0;
    private static final int VALUE_REPEAT_ONE = 1;
    private static final int VALUE_REPEAT_SHUFFLE = 2;

    private static SharedPreferences sSharedPreferences;
    private static AppConfig sInstance;

    public static void init(@NonNull Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        }
    }

    public static AppConfig getInstance() {
        if (sInstance == null) {
            synchronized (AppConfig.class) {
                if (sInstance == null) {
                    sInstance = new AppConfig();
                }
            }
        }
        return sInstance;
    }


    private AppConfig() {

    }

    public void setFirstTimeLaunch(boolean flag) {
        sSharedPreferences.edit()
                .putBoolean(KEY_FIRST_TIME_LAUNCH, flag)
                .apply();
    }

    public boolean isFirstTimeLaunch() {
        return sSharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    public boolean isNightModeOn() {
        return sSharedPreferences.getBoolean(KEY_NIGHT_MODE_ON, false);
    }

    public void setNightMode(boolean isOn) {
        sSharedPreferences.edit()
                .putBoolean(KEY_NIGHT_MODE_ON, isOn)
                .apply();
    }

    public void setRepeatAll() {
        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL)
                .apply();
    }

    public void setRepeatOne() {
        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_ONE)
                .apply();
    }

    public void setRepeatShuffle() {
        sSharedPreferences.edit()
                .putInt(KEY_REPEAT_MODE, VALUE_REPEAT_SHUFFLE)
                .apply();
    }

    public boolean isRepeatAll() {
        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_ALL;
    }

    public boolean isRepeatOne() {
        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_ONE;
    }

    public boolean isRepeatShuffle() {
        return sSharedPreferences.getInt(KEY_REPEAT_MODE, VALUE_REPEAT_ALL) == VALUE_REPEAT_SHUFFLE;
    }


}
