package com.scott.su.smusic2.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.io.Serializable;
import java.util.List;

import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_COMMAND_CODE;
import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG;
import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE;
import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_POSITION_SEEK_TO;

/**
 * 描述: 音乐播放指令控制器
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayController implements IMusicPlayController {
    public static final String ACTION_MUSIC_PLAY_CONTROL = "ACTION_MUSIC_PLAY_CONTROL";

    public static final int COMMAND_CODE_NONE = 0;
    public static final int COMMAND_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO = COMMAND_CODE_NONE + 1;
    public static final int COMMAND_CODE_PLAY = COMMAND_CODE_NONE + 2;
    public static final int COMMAND_CODE_PLAY_PAUSE = COMMAND_CODE_NONE + 3;
    public static final int COMMAND_CODE_SKIP_TO_NEXT = COMMAND_CODE_NONE + 4;
    public static final int COMMAND_CODE_SKIP_TO_PREVIOUS = COMMAND_CODE_NONE + 5;
    public static final int COMMAND_CODE_SEEK = COMMAND_CODE_NONE + 6;
    public static final int COMMAND_CODE_STOP = COMMAND_CODE_NONE + 7;


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

    public static void launch(Context context) {
        //启动音乐播放服务
        context.startService(new Intent(context, MusicPlayService.class));
    }

    private MusicPlayController() {

    }

    @Override
    public void updateCurrentPlayingInfo(Context context) {
        sendCommend(context, COMMAND_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO, null);
    }

    @Override
    public void play(Context context, List<LocalSongEntity> playQueue, LocalSongEntity currentPlaying) {
        Intent extraData = new Intent();
        extraData.putExtra(KEY_EXTRA_PLAY_QUEUE, (Serializable) playQueue);
        extraData.putExtra(KEY_EXTRA_CURRENT_PLAYING_SONG, currentPlaying);

        sendCommend(context, COMMAND_CODE_PLAY, extraData);
    }

    @Override
    public void playPause(Context context) {
        sendCommend(context, COMMAND_CODE_PLAY_PAUSE, null);
    }

    @Override
    public void skipToPrevious(Context context) {
        sendCommend(context, COMMAND_CODE_SKIP_TO_PREVIOUS, null);
    }

    @Override
    public void skipToNext(Context context) {
        sendCommend(context, COMMAND_CODE_SKIP_TO_NEXT, null);
    }

    @Override
    public void seekTo(Context context, int position) {
        Intent extraData = new Intent();
        extraData.putExtra(KEY_EXTRA_POSITION_SEEK_TO, position);

        sendCommend(context, COMMAND_CODE_SEEK, extraData);
    }

    @Override
    public void stop(Context context) {
        sendCommend(context, COMMAND_CODE_STOP, null);
    }

    private static void sendCommend(Context context, int command, @Nullable Intent extraData) {
        //启动音乐播放服务
        context.startService(new Intent(context, MusicPlayService.class));
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context.getApplicationContext());

        if (extraData == null) {
            extraData = new Intent();
        }

        extraData.setAction(ACTION_MUSIC_PLAY_CONTROL);
        extraData.putExtra(KEY_EXTRA_COMMAND_CODE, command);

        //发送指令本地广播
        manager.sendBroadcast(extraData);
    }


}
