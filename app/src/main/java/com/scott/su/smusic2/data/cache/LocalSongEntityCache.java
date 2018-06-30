package com.scott.su.smusic2.data.cache;

import android.support.v4.util.LruCache;

import com.scott.su.smusic2.data.entity.LocalSongEntity;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/30
 */

public class LocalSongEntityCache {
    private LruCache<String, LocalSongEntity> mEntityCache;
    private static LocalSongEntityCache mInstance;

    private LocalSongEntityCache() {
        //1M space;
        mEntityCache = new LruCache<>(1 * 1024 * 1024);
    }

    public static LocalSongEntityCache getInstance() {
        if (null == mInstance) {
            mInstance = new LocalSongEntityCache();
        }
        return mInstance;
    }

    public void put(String albumId, LocalSongEntity entity) {
        mEntityCache.put(albumId, entity);
    }

    public LocalSongEntity get(String songId) {
        return mEntityCache.get(songId);
    }

    public void release() {
        if (mEntityCache != null) {
            mEntityCache.evictAll();
        }
    }
}
