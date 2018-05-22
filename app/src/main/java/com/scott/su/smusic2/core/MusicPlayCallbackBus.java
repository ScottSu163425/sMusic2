package com.scott.su.smusic2.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 音乐播放回调接收、分发
 * 作者: su
 * 日期: 2018/5/22
 */

public class MusicPlayCallbackBus {
    private static MusicPlayCallbackBus sInstance;
    private static MusicPlayCallbackReceiver sMusicPlayCallbackReceiver = new MusicPlayCallbackReceiver();


    public static MusicPlayCallbackBus getInstance() {
        if (sInstance == null) {
            synchronized (MusicPlayCallbackBus.class) {
                if (sInstance == null) {
                    sInstance = new MusicPlayCallbackBus();
                }
            }
        }
        return sInstance;
    }


    public static void init(@NonNull Context context) {

    }

    public static void release(@NonNull Context context) {
    }

    private List<MusicPlayCallback> mMusicPlayCallbacks = new ArrayList<>();

    private MusicPlayCallbackBus() {

    }

    public void registerCallback(@NonNull MusicPlayCallback callback) {
        unregisterCallback(callback);

        mMusicPlayCallbacks.add(callback);
    }

    public void unregisterCallback(@NonNull MusicPlayCallback callback) {
        if (mMusicPlayCallbacks.contains(callback)) {
            mMusicPlayCallbacks.remove(callback);
        }
    }


    private static class MusicPlayCallbackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

}
