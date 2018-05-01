package com.scott.su.smusic2.data.source.local;

import android.content.Context;

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
    public List<LocalSongEntity> getAll(Context context) {
        return LocalSongHelper.getLocalSongs(context);
    }

}
