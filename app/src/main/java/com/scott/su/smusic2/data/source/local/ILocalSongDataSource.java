package com.scott.su.smusic2.data.source.local;

import android.content.Context;

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
     * @param context
     * @return
     */
    List<LocalSongEntity> getAllSongs(Context context);

    /**
     * 获取所有本地音乐专辑
     * @param context
     * @return
     */
    List<LocalAlbumEntity> getAllAlbums(Context context);

    /**
     * 获取指定专辑封面图片路径
     *
     * @param context
     * @param albumId
     * @return
     */
    String getAlbumCoverPathByAlbumId(Context context, long albumId);

}
