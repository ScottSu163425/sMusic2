package com.scott.su.smusic2.core;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/22
 */

public interface MusicPlayCallback {

    void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue);

    void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration);

    void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration);

    void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration);

    void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue);

    void onClose(LocalSongEntity song, List<LocalSongEntity> playQueue);

}
