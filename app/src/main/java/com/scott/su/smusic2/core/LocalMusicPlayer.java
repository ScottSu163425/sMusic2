package com.scott.su.smusic2.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述: 本地音乐播放实现类
 * 作者: su
 * 日期: 2018/5/14
 */

public class LocalMusicPlayer {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private List<LocalSongEntity> mPlayQueue;
    private LocalSongEntity mCurrentPlayingSong;


    public LocalMusicPlayer(Context context) {
        mContext = context;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });

        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
    }

    public void setPlaySongs(@NonNull List<LocalSongEntity> playQueue) {
        this.mPlayQueue = playQueue;
    }

    public void playPause(@Nullable LocalSongEntity currentPlayingSong) {
        if (currentPlayingSong == null) {
            currentPlayingSong = mPlayQueue.get(0);
        }

        if (mCurrentPlayingSong == null) {
            mCurrentPlayingSong = currentPlayingSong;

            restart();
        } else {

        }

    }

    public void skipToPrevious() {

    }

    public void skipToNext() {

    }

    private void restart() {

    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void resume() {
        mMediaPlayer.start();
    }


}
