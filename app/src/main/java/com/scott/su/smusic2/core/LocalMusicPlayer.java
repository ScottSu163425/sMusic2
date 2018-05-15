package com.scott.su.smusic2.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;

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
    }

    public void setPlaySongs(@NonNull List<LocalSongEntity> playQueue, @NonNull LocalSongEntity currentPlayingSong) {
        this.mPlayQueue = playQueue;
        this.mCurrentPlayingSong = currentPlayingSong;
    }

    public void playPause(){

    }

    public void skipToPrevious() {

    }

    public void skipToNext() {

    }

    private void restart() {

    }

    private void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

}
