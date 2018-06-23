package com.scott.su.smusic2;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;

import com.scott.su.smusic2.core.MusicPlayCallbackBus;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.data.source.local.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */

public class App extends Application {
    private List<Activity> mActivities = new ArrayList<>();

    private static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.mypackage.service";
    private static final String NOTIFICATION_CHANNEL_ID_TASK = "com.mypackage.download_info";


    @Override
    public void onCreate() {
        super.onCreate();

        AppConfig.init(this);

        MusicPlayController.launch(getApplicationContext());
        MusicPlayCallbackBus.init(getApplicationContext());

        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

        if (AppConfig.getInstance().isNightModeOn()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onTerminate() {
        MusicPlayCallbackBus.release(getApplicationContext());

        super.onTerminate();
    }

    private void initChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_TASK, "Download Info", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            mActivities.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivities.remove(activity);
        }
    };

    public boolean isActivityRunning(@NonNull Class clazz) {
        if (mActivities.isEmpty()) {
            return false;
        }

        for (Activity activity : mActivities) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                return true;
            }
        }


        return false;
    }

    public boolean isTopActivity(Class clazz) {
        if (mActivities.isEmpty()) {
            return false;
        }

        return mActivities.get(mActivities.size() - 1).getClass().getName().equals(clazz.getName());
    }

}
