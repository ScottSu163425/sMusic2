package com.scott.su.smusic2.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.ArrayList;

/**
 * 描述: 音乐播放服务
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayService extends Service {
    private MusicPlayCommandReceiver mCommandReceiver;
    private LocalMusicPlayer mMusicPlayCore;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMusicPlayCore = new LocalMusicPlayer(getApplicationContext());

        registerCommandReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterCommandReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void registerCommandReceiver() {
        unregisterCommandReceiver();

        mCommandReceiver = new MusicPlayCommandReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayController.ACTION_MUSIC_PLAY_CONTROL);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mCommandReceiver, intentFilter);
    }

    private void unregisterCommandReceiver() {
        if (mCommandReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .unregisterReceiver(mCommandReceiver);
        }
    }

    /**
     * 播放指令接收器
     */
    class MusicPlayCommandReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int commandCode = intent.getIntExtra(MusicPlayController.KEY_EXTRA_COMMAND_CODE, 0);

            if (commandCode == MusicPlayController.COMMAND_CODE_MUSIC_PLAY) {
                ArrayList<LocalSongEntity> playQueue
                        = (ArrayList<LocalSongEntity>) intent.getSerializableExtra(MusicPlayController.KEY_EXTRA_PLAY_QUEUE);

                LocalSongEntity currentPlaying = (LocalSongEntity) intent.getSerializableExtra(MusicPlayController.KEY_EXTRA_CURRENT_PLAYING);

                mMusicPlayCore.play();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_MUSIC_PAUSE) {
                mMusicPlayCore.pause();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_MUSIC_SKIP_TO_PREVIOUS) {
                mMusicPlayCore.skipToPrevious();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_MUSIC_SKIP_TO_NEXT) {
                mMusicPlayCore.skipToNext();
            }

        }

    }

}
