package com.scott.su.smusic2.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述: 本地音乐数据源
 * 作者: Su
 * 日期: 2018/4/30
 */

public class LocalSongDataSource implements ILocalSongDataSource {
    private static LocalSongDataSource sInstance;


    public static LocalSongDataSource getInstance() {
        if (sInstance == null) {
            synchronized (LocalSongDataSource.class) {
                if (sInstance == null) {
                    sInstance = new LocalSongDataSource();
                }
            }
        }
        return sInstance;
    }

    private LocalSongDataSource() {
    }

    @Override
    public List<LocalSongEntity> getAllSongs(Context context) {
        return LocalSongHelper.getInstance().getAllSongs(context);
    }

    @Override
    public LocalSongEntity getSongById(@NonNull Context context, @NonNull String songId) {
        return LocalSongHelper.getInstance().getSongById(context, songId);
    }

    @Override
    public List<LocalSongEntity> getSongsById(@NonNull Context context, @NonNull String[] idArr) {
        return LocalSongHelper.getInstance().getSongsById(context, idArr);
    }

    @Override
    public List<LocalAlbumEntity> getAllAlbums(Context context) {
        return LocalSongHelper.getInstance().getAllAlbums(context);
    }

    @Override
    public LocalAlbumEntity getAlbum(Context context, String albumId) {
        return LocalSongHelper.getInstance().getAlbum(context, albumId);
    }

    @Override
    public String getAlbumCoverPathByAlbumId(Context context, String albumId) {
        return LocalSongHelper.getInstance().getAlbumCoverPathByAlbumId(context, albumId);
    }

    @Override
    public String getAlbumCoverPathBySongId(@NonNull Context context, @NonNull String songId) {
        return LocalSongHelper.getInstance().getAlbumCoverPathBySongId(context, songId);
    }

    @Override
    public List<LocalSongEntity> searchSong(@NonNull Context context, @NonNull String keyword) {
        return LocalSongHelper.getInstance().searchSong(context, keyword);
    }

    @Override
    public List<LocalAlbumEntity> searchAlbum(@NonNull Context context, @NonNull String keyword) {
        return LocalSongHelper.getInstance().searchAlbum(context, keyword);
    }

}
