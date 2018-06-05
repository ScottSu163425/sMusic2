package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/5
 */

public class AppConfig {
    private static final String KEY_FIRST_TIME_LAUNCH = "KEY_FIRST_TIME_LAUNCH";

    private static final String KEY_REPEAT_ONE = "KEY_REPEAT_ONE";
    private static final String KEY_REPEAT_ALL = "KEY_REPEAT_ALL";
    private static final String KEY_REPEAT_SHUFFLE = "KEY_REPEAT_SHUFFLE";
    private static Context sContext;
    private static SharedPreferences sSharedPreferences;


    public static void init(Context context) {
        sContext = context.getApplicationContext();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    private static AppConfig sInstance;


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

        if (flag) {
            setRepeatAll(true);
        }
    }

    public boolean isFirstTimeLaunch() {
        return sSharedPreferences.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    public void setRepeatOne(boolean flag) {
        sSharedPreferences.edit()
                .putBoolean(KEY_REPEAT_ONE, flag)
                .apply();

        if (flag) {
            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_ALL, false)
                    .apply();

            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_SHUFFLE, false)
                    .apply();

        }

    }

    public void setRepeatAll(boolean flag) {
        sSharedPreferences.edit()
                .putBoolean(KEY_REPEAT_ALL, flag)
                .apply();

        if (flag) {
            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_ONE, false)
                    .apply();

            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_SHUFFLE, false)
                    .apply();
        }

    }

    public void setRepeatShuffle(boolean flag) {
        sSharedPreferences.edit()
                .putBoolean(KEY_REPEAT_SHUFFLE, flag)
                .apply();

        if (flag) {
            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_ONE, false)
                    .apply();

            sSharedPreferences.edit()
                    .putBoolean(KEY_REPEAT_ALL, false)
                    .apply();
        }

    }

    public boolean isRepeatOne() {
        return sSharedPreferences.getBoolean(KEY_REPEAT_ONE, false);
    }

    public boolean isRepeatAll() {
        return sSharedPreferences.getBoolean(KEY_REPEAT_ALL, false);
    }

    public boolean isRepeatShuffle() {
        return sSharedPreferences.getBoolean(KEY_REPEAT_SHUFFLE, false);
    }


}
