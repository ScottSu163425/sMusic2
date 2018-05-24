package com.scott.su.smusic2.core;

import android.content.Context;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/14
 */

public interface IMusicPlayController {
    void playPause(Context context, List<LocalSongEntity> playQueue, LocalSongEntity currentPlaying);

    void skipToPrevious(Context context);

    void skipToNext(Context context);

    void seekTo(Context context, int position);


}
