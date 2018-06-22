package com.scott.su.smusic2.data.source.local;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.source.local.db.AppDatabase;
import com.scott.su.smusic2.data.source.local.db.LocalCollectionDao;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class LocalCollectionDataSource implements ILocalCollectionDataSource {
    private static LocalCollectionDataSource sInstance;

    public static LocalCollectionDataSource getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LocalCollectionDataSource.class) {
                if (sInstance == null) {
                    sInstance = new LocalCollectionDataSource(context);
                }
            }
        }
        return sInstance;
    }

    private LocalCollectionDao mCollectionDao;

    private LocalCollectionDataSource(Context context) {
        mCollectionDao = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                context.getString(R.string.db_name))
                .build()
                .getLocalCollectionDao();
    }

    @Override
    public List<LocalCollectionEntity> getAllCollections() {
        return mCollectionDao.getAllCollections();
    }

    @Override
    public boolean isCollectionNameExist(@NonNull String collectionName) {
        List<LocalCollectionEntity> list = getAllCollections();

        if (list == null || list.isEmpty()) {
            return false;
        }

        for (LocalCollectionEntity entity : list) {
            if (collectionName.equals(entity.getCollectionName())) {
                return true;
            }
        }


        return false;
    }

    @Override
    public void createNewCollection(LocalCollectionEntity entity) {
        mCollectionDao.createNewCollection(entity);
    }


}
