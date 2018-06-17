package com.scott.su.smusic2;

import android.app.Application;

import com.scott.su.smusic2.core.MusicPlayCallbackBus;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.data.source.local.AppConfig;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        MusicPlayController.launch(getApplicationContext());
        MusicPlayCallbackBus.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        MusicPlayCallbackBus.release(getApplicationContext());

        super.onTerminate();
    }

}
