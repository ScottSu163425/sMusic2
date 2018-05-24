package com.scott.su.smusic2.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.io.IOException;
import java.util.List;

/**
 * 描述: 本地音乐播放实现类
 * 作者: su
 * 日期: 2018/5/14
 */

public class LocalMusicPlayer {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private MusicPlayProgressTimer mProgressTimer;
    private List<LocalSongEntity> mPlayQueue;
    private LocalSongEntity mCurrentPlayingSong;


    public LocalMusicPlayer(Context context) {
        mContext = context;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                getCallback().onComplete(mCurrentPlayingSong);
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                getCallback().onStart(mCurrentPlayingSong);
                startProgressTimer();
            }
        });

        mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });

        mProgressTimer = new MusicPlayProgressTimer();
        mProgressTimer.setCallback(new MusicPlayProgressTimer.Callback() {
            @Override
            public void onTik() {
                getCallback().onTik(mCurrentPlayingSong, getCurrentPosition(), getCurrentDuration());
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

        if (mCurrentPlayingSong == null
                || currentPlayingSong.getSongId() != mCurrentPlayingSong.getSongId()) {

            //切歌
            mCurrentPlayingSong = currentPlayingSong;
            restart();
        } else {
            //暂停、播放
            if (isPlaying()) {
                pause();
            } else {
                resume();
            }
        }

    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void skipToPrevious() {

    }

    public void skipToNext() {

    }

    public int getCurrentPosition() {
        if (isPlaying()) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getCurrentDuration() {
        if (isPlaying()) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    private void restart() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mCurrentPlayingSong.getPath());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startProgressTimer() {
        mProgressTimer.start();
    }

    private void stopProgressTimer() {
        mProgressTimer.stop();
    }

    public void pause() {
        if (!isPlaying()) {
            return;
        }

        mMediaPlayer.pause();
        getCallback().onPause(mCurrentPlayingSong, mMediaPlayer.getCurrentPosition());
        stopProgressTimer();
    }

    public void resume() {
        if (isPlaying()) {
            return;
        }

        mMediaPlayer.start();
        getCallback().onResume(mCurrentPlayingSong, mMediaPlayer.getCurrentPosition());
        startProgressTimer();
    }

    private MusicPlayCallback mCallback;

    public void setCallback(MusicPlayCallback callback) {
        this.mCallback = callback;
    }

    private MusicPlayCallback getCallback() {
        if (mCallback == null) {
            mCallback = new MusicPlayCallback() {
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
            };
        }
        return mCallback;
    }


}
