package com.scott.su.smusic2.data.source.local.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scott.su.smusic2.data.entity.LocalCollectionEntity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

@Dao
public interface LocalCollectionDao {
    @Query("SELECT * FROM collections")
    List<LocalCollectionEntity> getAllCollections();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createNewCollection(LocalCollectionEntity entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCollection(LocalCollectionEntity entity);
}
