package com.scott.su.smusic2.modules.play.engine;

import android.content.Context;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/14
 */

public interface IMusicPlayCore {
    void play(Context context, List<LocalSongEntity> playQueue, LocalSongEntity currentPlaying);

    void pause(Context context);

    void skipToPrevious(Context context);

    void skipToNext(Context context);
}
