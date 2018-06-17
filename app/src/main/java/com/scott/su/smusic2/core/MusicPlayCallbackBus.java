package com.scott.su.smusic2.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 音乐播放回调统一接收、分发管理类
 * 作者: su
 * 日期: 2018/5/22
 */

public class MusicPlayCallbackBus {
    public static final String KEY_EXTRA_EVENT_CODE = "KEY_EXTRA_EVENT_CODE";
    public static final String ACTION_MUSIC_PLAY_CALLBACK = "ACTION_MUSIC_PLAY_CALLBACK";

    public static final int EVENT_CODE_NONE = 0;
    public static final int EVENT_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO = EVENT_CODE_NONE + 1;
    public static final int EVENT_CODE_ON_START = EVENT_CODE_NONE + 2;
    public static final int EVENT_CODE_ON_TICK = EVENT_CODE_NONE + 3;
    public static final int EVENT_CODE_ON_PAUSE = EVENT_CODE_NONE + 4;
    public static final int EVENT_CODE_ON_RESUME = EVENT_CODE_NONE + 5;
    public static final int EVENT_CODE_ON_COMPLETE = EVENT_CODE_NONE + 6;
    public static final int EVENT_CODE_ON_STOP = EVENT_CODE_NONE + 7;


    private static MusicPlayCallbackBus sInstance;
    private static MusicPlayCallbackReceiver sMusicPlayCallbackReceiver;
    private static List<MusicPlayCallback> sMusicPlayCallbacks = new ArrayList<>();
    private static LocalSongEntity sCurrentPlayingSong;
    private static ArrayList<LocalSongEntity> sPlayingQueue;


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
        registerReceiver(context);
    }

    public static void release(@NonNull Context context) {
        unregisterReceiver(context);

        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback = null;
        }

        sMusicPlayCallbacks.clear();
        sMusicPlayCallbacks = null;
    }

    private static void registerReceiver(@NonNull Context context) {
        unregisterReceiver(context);

        sMusicPlayCallbackReceiver = new MusicPlayCallbackReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MUSIC_PLAY_CALLBACK);
        context.registerReceiver(sMusicPlayCallbackReceiver, intentFilter);
    }

    private static void unregisterReceiver(@NonNull Context context) {
        if (sMusicPlayCallbackReceiver != null) {
            context.unregisterReceiver(sMusicPlayCallbackReceiver);
        }
    }

    private MusicPlayCallbackBus() {

    }

    /**
     * 注册音乐播放事件回调
     *
     * @param callback
     */
    public void registerCallback(@NonNull MusicPlayCallback callback) {
        unregisterCallback(callback);

        sMusicPlayCallbacks.add(callback);
    }

    public void unregisterCallback(@NonNull MusicPlayCallback callback) {
        if (sMusicPlayCallbacks.contains(callback)) {
            sMusicPlayCallbacks.remove(callback);
        }
    }

    public static LocalSongEntity getCurrentPlayingSong() {
        return sCurrentPlayingSong;
    }

    public static ArrayList<LocalSongEntity> getPlayingQueue() {
        return sPlayingQueue == null ? new ArrayList<LocalSongEntity>() : sPlayingQueue;
    }

    private static void notifyOnStart(LocalSongEntity currentPlayingSong, ArrayList<LocalSongEntity> playQueue) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onStart(currentPlayingSong, playQueue);
        }
    }

    private static void notifyOnTick(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onTik(song, playQueue, position, duration);
        }
    }

    private static void notifyOnPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onPause(song, playQueue, position, duration);
        }
    }

    private static void notifyOnResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onResume(song, playQueue, position, duration);
        }
    }

    private static void notifyOnComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onComplete(song, playQueue);
        }
    }

    private static void notifyOnClose(LocalSongEntity song, List<LocalSongEntity> playQueue) {
        for (MusicPlayCallback callback : sMusicPlayCallbacks) {
            callback.onClose(song, playQueue);
        }
    }

    private static class MusicPlayCallbackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int eventCode = intent.getIntExtra(KEY_EXTRA_EVENT_CODE, EVENT_CODE_NONE);

//            Log.e("===>onReceive", "MusicPlayCallbackBus:" + eventCode);

            final LocalSongEntity currentPlayingSong = (LocalSongEntity) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG);
            final ArrayList<LocalSongEntity> playQueue = (ArrayList<LocalSongEntity>) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE);
            final int position = intent.getIntExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, 0);
            final int duration = intent.getIntExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, 0);

            if (eventCode == EVENT_CODE_ON_START) {
                sCurrentPlayingSong = currentPlayingSong;
                sPlayingQueue = playQueue;
                notifyOnStart(currentPlayingSong, playQueue);
            } else if (eventCode == EVENT_CODE_ON_TICK) {
                notifyOnTick(currentPlayingSong, playQueue, position, duration);
            } else if (eventCode == EVENT_CODE_ON_PAUSE) {
                notifyOnPause(currentPlayingSong, playQueue, position, duration);
            } else if (eventCode == EVENT_CODE_ON_RESUME) {
                notifyOnResume(currentPlayingSong, playQueue, position, duration);
            } else if (eventCode == EVENT_CODE_ON_COMPLETE) {
                notifyOnComplete(currentPlayingSong, playQueue);
            } else if (eventCode == EVENT_CODE_ON_STOP) {
                notifyOnClose(currentPlayingSong, playQueue);
            }

        }
    }


}
