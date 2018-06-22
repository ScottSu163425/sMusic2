package com.scott.su.smusic2.data.source.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.scott.su.smusic2.data.entity.LocalCollectionEntity;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

@Database(entities = {LocalCollectionEntity.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalCollectionDao getLocalCollectionDao();
}
