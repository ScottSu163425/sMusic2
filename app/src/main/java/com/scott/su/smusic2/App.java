package com.scott.su.smusic2;

import android.app.Application;

import com.scott.su.smusic2.core.MusicPlayController;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        MusicPlayController.getInstance().launch(getApplicationContext());
    }

}
