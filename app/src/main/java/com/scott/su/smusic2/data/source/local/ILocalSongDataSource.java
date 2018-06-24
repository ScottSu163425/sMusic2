package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/30
 */

public interface ILocalSongDataSource {

    /**
     * 获取所有本地音乐
     *
     * @param context
     * @return
     */
    List<LocalSongEntity> getAllSongs(@NonNull Context context);

    LocalSongEntity getSongById(@NonNull Context context,@NonNull String songId);

    List<LocalSongEntity> getSongsById(@NonNull Context context,@NonNull String[] idArr);

    /**
     * 获取所有本地音乐专辑
     *
     * @param context
     * @return
     */
    List<LocalAlbumEntity> getAllAlbums(@NonNull Context context);

    LocalAlbumEntity getAlbum(@NonNull Context context, @NonNull String albumId);

    /**
     * 获取指定专辑封面图片路径
     *
     * @param context
     * @param albumId
     * @return
     */
    String getAlbumCoverPathByAlbumId(@NonNull Context context, @NonNull String albumId);

    String getAlbumCoverPathBySongId(@NonNull Context context, @NonNull String songId);

}
