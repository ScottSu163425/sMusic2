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
    private LocalMusicPlayer mMusicPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mMusicPlayer = new LocalMusicPlayer(getApplicationContext());
        mMusicPlayer.setCallback(new MusicPlayCallback() {
            @Override
            public void onStart(LocalSongEntity song) {

            }

            @Override
            public void onTik(LocalSongEntity song, int position, int duration) {

            }

            @Override
            public void onPause(LocalSongEntity song, int position) {

            }

            @Override
            public void onResume(LocalSongEntity song, int position) {

            }

            @Override
            public void onComplete(LocalSongEntity song) {

            }
        });

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

            if (commandCode == MusicPlayController.COMMAND_CODE_PLAY_PAUSE) {
                ArrayList<LocalSongEntity> playQueue
                        = (ArrayList<LocalSongEntity>) intent.getSerializableExtra(MusicPlayController.KEY_EXTRA_PLAY_QUEUE);

                LocalSongEntity currentPlaying = (LocalSongEntity) intent.getSerializableExtra(MusicPlayController.KEY_EXTRA_CURRENT_PLAYING);

                mMusicPlayer.setPlaySongs(playQueue);
                mMusicPlayer.playPause(currentPlaying);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_PREVIOUS) {
                mMusicPlayer.skipToPrevious();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_NEXT) {
                mMusicPlayer.skipToNext();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SEEK) {
                int positionSeekTo = intent.getIntExtra(MusicPlayController.KEY_EXTRA_POSITION_SEEK_TO, 0);
                mMusicPlayer.seekTo(positionSeekTo);
            }

        }

    }

}
