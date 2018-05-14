package com.scott.su.smusic2.common;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 描述: 音乐播放指令控制器
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayController {
    public static final String ACTION_MUSIC_PLAY = "MusicPlayController_ACTION_MUSIC_PLAY";
    public static final String ACTION_MUSIC_PAUSE = "MusicPlayController_ACTION_MUSIC_PAUSE";
    public static final String ACTION_MUSIC_SKIP_TO_PREVIOUS = "MusicPlayController_ACTION_MUSIC_SKIP_TO_PREVIOUS";
    public static final String ACTION_MUSIC_SKIP_TO_NEXT = "MusicPlayController_ACTION_MUSIC_SKIP_TO_NEXT";


    private MusicPlayController() {
    }

    public static void play(Context context) {
        sendCommend(context, ACTION_MUSIC_PLAY);
    }

    public static void pause(Context context) {
        sendCommend(context, ACTION_MUSIC_PAUSE);
    }

    public static void skipToPrevious(Context context) {
        sendCommend(context, ACTION_MUSIC_SKIP_TO_PREVIOUS);
    }

    public static void skipToNext(Context context) {
        sendCommend(context, ACTION_MUSIC_SKIP_TO_NEXT);
    }


    private static void sendCommend(Context context, String action) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context.getApplicationContext());
        manager.sendBroadcast(new Intent(action));
    }


}
