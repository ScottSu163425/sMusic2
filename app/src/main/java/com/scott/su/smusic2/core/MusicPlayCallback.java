package com.scott.su.smusic2.core;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/22
 */

public interface MusicPlayCallback {
    void onStart(LocalSongEntity song);

    void onTik(LocalSongEntity song, int position, int duration);

    void onPause(LocalSongEntity song, int position);

    void onResume(LocalSongEntity song, int position);

    void onComplete(LocalSongEntity song);

}
