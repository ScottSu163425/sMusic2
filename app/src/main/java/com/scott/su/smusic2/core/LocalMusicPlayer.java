package com.scott.su.smusic2.core;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.scott.su.common.util.ListUtil;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 本地音乐播放实现类
 * 作者: su
 * 日期: 2018/5/14
 */

public class LocalMusicPlayer {
    private static final String TAG = "===>LocalMusicPlayer";
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private MusicPlayProgressTimer mProgressTimer;
    private List<LocalSongEntity> mPlayQueue = new ArrayList<>();
    private LocalSongEntity mCurrentPlayingSong;


    public LocalMusicPlayer(Context context) {
        mContext = context;

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e(TAG, "onCompletion");
                getCallback().onComplete(mCurrentPlayingSong);

                skipToNext();
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");

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

    public void play(@Nullable LocalSongEntity song) {
        if (isPlayQueueEmpty()) {
            return;
        }

        if (song == null) {
            song = mPlayQueue.get(0);
        }

        //初次播放
        if (isPlayingSongEmpty()) {
            mCurrentPlayingSong = song;
            restart();
            return;
        }

        if (mCurrentPlayingSong.getSongId() == song.getSongId()) {
            resume();
            return;
        }

        //切歌
        mCurrentPlayingSong = song;
        restart();
    }

    /**
     * 切换播放中的音乐暂停状态
     */
    public void playPause() {
        if (!canControlCurrentPlayingSong()) {
            return;
        }

        //暂停、播放
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public void pause() {
        if (!canControlCurrentPlayingSong()) {
            return;
        }

        if (isPlaying()) {
            mMediaPlayer.pause();
            getCallback().onPause(mCurrentPlayingSong, mMediaPlayer.getCurrentPosition());
            stopProgressTimer();
        }

    }

    public void resume() {
        if (!canControlCurrentPlayingSong()) {
            return;
        }

        if (!isPlaying()) {
            mMediaPlayer.start();
            getCallback().onResume(mCurrentPlayingSong, mMediaPlayer.getCurrentPosition());
            startProgressTimer();
        }
    }

    public void skipToPrevious() {
        if (isPlayQueueEmpty()) {
            return;
        }

        mCurrentPlayingSong = ListUtil.getPrevLoop(mPlayQueue, mCurrentPlayingSong);
        restart();
    }

    public void skipToNext() {
        if (isPlayQueueEmpty()) {
            return;
        }

        mCurrentPlayingSong = ListUtil.getNextLoop(mPlayQueue, mCurrentPlayingSong);
        restart();
    }

    public void seekTo(int position) {
        if (!canControlCurrentPlayingSong()) {
            return;
        }

        if (position < 0 || position > getCurrentDuration()) {
            return;
        }

        mMediaPlayer.seekTo(position);

        resume();
    }

    public void restart() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(mCurrentPlayingSong.getPath());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        pause();

        mMediaPlayer.stop();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getCurrentDuration() {
        return mMediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public boolean isPlayQueueEmpty() {
        return mPlayQueue == null
                || mPlayQueue.isEmpty();
    }

    public boolean isPlayingSongEmpty() {
        return mCurrentPlayingSong == null
                || getCurrentDuration() <= 0;
    }

    /**
     * 是否能操作当前播放中的歌曲
     *
     * @return
     */
    private boolean canControlCurrentPlayingSong() {
        return !isPlayQueueEmpty()
                && !isPlayingSongEmpty();
    }

    private void startProgressTimer() {
        stopProgressTimer();
        mProgressTimer.start();
    }

    private void stopProgressTimer() {
        mProgressTimer.stop();
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
