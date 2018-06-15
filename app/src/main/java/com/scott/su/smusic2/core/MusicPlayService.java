package com.scott.su.smusic2.core;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;

import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_COMMAND_CODE;

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
            public void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_ON_START, extraData);

                updateNotification();
            }

            @Override
            public void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_ON_TICK, extraData);
            }

            @Override
            public void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_ON_PAUSE, extraData);

                updateNotification();
            }

            @Override
            public void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_ON_RESUME, extraData);

                updateNotification();
            }

            @Override
            public void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE__ON_COMPLETE, extraData);

            }
        });

        registerCommandReceiver();
    }

    private void updateNotification() {
        // TODO: 2018/6/15 更新状态栏通知

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
            int commandCode = intent.getIntExtra(KEY_EXTRA_COMMAND_CODE,
                    MusicPlayController.COMMAND_CODE_NONE);

            if (commandCode == MusicPlayController.COMMAND_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO) {
                LocalSongEntity currentPlayingSong = mMusicPlayer.getCurrentPlayingSong();
                ArrayList<LocalSongEntity> playQueue = (ArrayList<LocalSongEntity>) mMusicPlayer.getPlayQueue();

                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, currentPlayingSong);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, playQueue);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO, extraData);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_PLAY) {
                LocalSongEntity currentPlayingSong = (LocalSongEntity) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG);
                ArrayList<LocalSongEntity> playQueue
                        = (ArrayList<LocalSongEntity>) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE);

                mMusicPlayer.setPlaySongs(playQueue);
                mMusicPlayer.restart(currentPlayingSong);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_PLAY_PAUSE) {
                mMusicPlayer.playPause();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_PREVIOUS) {
                mMusicPlayer.skipToPrevious();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_NEXT) {
                mMusicPlayer.skipToNext();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SEEK) {
                int positionSeekTo = intent.getIntExtra(MusicPlayConstants.KEY_EXTRA_POSITION_SEEK_TO, 0);
                mMusicPlayer.seekTo(positionSeekTo);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_STOP) {
                mMusicPlayer.stop();
            }

        }

    }

    /**
     * 发送音乐播放回调事件广播
     *
     * @param context
     * @param eventCode
     * @param extraData
     */
    private void sendCallbackBroadcast(Context context, int eventCode, @Nullable Intent extraData) {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context.getApplicationContext());

        if (extraData == null) {
            extraData = new Intent();
        }

        extraData.setAction(MusicPlayCallbackBus.ACTION_MUSIC_PLAY_CALLBACK);
        extraData.putExtra(MusicPlayCallbackBus.KEY_EXTRA_EVENT_CODE, eventCode);

        //发送指令本地广播
        manager.sendBroadcast(extraData);
    }

}
