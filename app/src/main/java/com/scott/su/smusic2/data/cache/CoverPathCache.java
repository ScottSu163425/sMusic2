package com.scott.su.smusic2.data.cache;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/30
 */

public class CoverPathCache {
    private LruCache<String, String> coverPathCache;
    private static CoverPathCache instance;

    private CoverPathCache() {
        //2M cache space;
        coverPathCache = new LruCache<>(2 * 1024 * 1024);
    }

    public static CoverPathCache getInstance() {
        if (null == instance) {
            instance = new CoverPathCache();
        }
        return instance;
    }

    public void put(String songId, String coverPath) {
        if (TextUtils.isEmpty(songId) || TextUtils.isEmpty(coverPath)) {
            return;
        }

        coverPathCache.put(songId, coverPath);
    }

    public String get(String songId) {
        if (TextUtils.isEmpty(songId)) {
            return null;
        }

        return coverPathCache.get(songId);
    }

    public void release() {
        if (coverPathCache != null) {
            coverPathCache.evictAll();
        }
    }

}
