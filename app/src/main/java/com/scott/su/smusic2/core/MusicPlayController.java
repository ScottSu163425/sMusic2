package com.scott.su.smusic2.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 描述: 音乐播放指令控制器
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayController implements IMusicPlayController {
    public static final String ACTION_MUSIC_PLAY_CONTROL = "ACTION_MUSIC_PLAY_CONTROL";
    public static final String KEY_EXTRA_COMMAND_CODE = "KEY_EXTRA_COMMAND_CODE";
    public static final String KEY_EXTRA_PLAY_QUEUE = "KEY_EXTRA_PLAY_QUEUE";
    public static final String KEY_EXTRA_CURRENT_PLAYING = "KEY_EXTRA_CURRENT_PLAYING";

    public static final int COMMAND_CODE_MUSIC_PLAY = 1;
    public static final int COMMAND_CODE_MUSIC_PAUSE = 2;
    public static final int COMMAND_CODE_MUSIC_SKIP_TO_NEXT = 3;
    public static final int COMMAND_CODE_MUSIC_SKIP_TO_PREVIOUS = 4;


    private static MusicPlayController sInstance;


    public static MusicPlayController getInstance() {
        if (sInstance == null) {
            synchronized (MusicPlayController.class) {
                if (sInstance == null) {
                    sInstance = new MusicPlayController();
                }
            }
        }
        return sInstance;
    }


    private MusicPlayController() {

    }

    @Override
    public void play(Context context, List<LocalSongEntity> playQueue, LocalSongEntity currentPlaying) {
        sendCommend(context, COMMAND_CODE_MUSIC_PLAY, playQueue, currentPlaying);
    }

    @Override
    public void pause(Context context) {
        sendCommend(context, COMMAND_CODE_MUSIC_PAUSE, null, null);
    }

    @Override
    public void skipToPrevious(Context context) {
        sendCommend(context, COMMAND_CODE_MUSIC_SKIP_TO_PREVIOUS, null, null);
    }

    @Override
    public void skipToNext(Context context) {
        sendCommend(context, COMMAND_CODE_MUSIC_SKIP_TO_NEXT, null, null);
    }


    private static void sendCommend(Context context, int command,
                                    @Nullable List<LocalSongEntity> playQueue,
                                    @Nullable LocalSongEntity currentPlaying) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context.getApplicationContext());

        Intent intent = new Intent(ACTION_MUSIC_PLAY_CONTROL);
        intent.putExtra(KEY_EXTRA_COMMAND_CODE, command);

        if (playQueue != null) {
            intent.putExtra(KEY_EXTRA_PLAY_QUEUE, (Serializable) playQueue);
        }

        if (currentPlaying != null) {
            intent.putExtra(KEY_EXTRA_CURRENT_PLAYING, currentPlaying);
        }

        manager.sendBroadcast(intent);
    }


}
