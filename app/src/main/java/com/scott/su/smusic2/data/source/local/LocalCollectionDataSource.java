package com.scott.su.smusic2.data.source.local;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.db.AppDatabase;
import com.scott.su.smusic2.data.source.local.db.LocalCollectionDao;

import java.util.ArrayList;
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
    public List<LocalCollectionEntity> getAllCollections(@NonNull Context context) {
        List<LocalCollectionEntity> list = mCollectionDao.getAllCollections();

        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.isEmpty()) {
            return list;
        }

        //获取并设置封面路径
        for (LocalCollectionEntity collectionEntity : list) {
            String coverSongId = collectionEntity.getLastAddedSongId();
            collectionEntity.setCoverPath(LocalSongHelper.getInstance().getAlbumCoverPathBySongId(context, coverSongId));
        }

        List<LocalCollectionEntity> listOrdered = new ArrayList<>();

        int n = list.size();
        for (int i = n - 1; i >= 0; i--) {
            listOrdered.add(list.get(i));
        }

        return listOrdered;
    }

    @Override
    public LocalCollectionEntity getCollection(@NonNull Context context, @NonNull String collectionId) {
        if (TextUtils.isEmpty(collectionId)) {
            return null;
        }

        List<LocalCollectionEntity> list = getAllCollections(context);

        if (list != null && !list.isEmpty()) {
            for (LocalCollectionEntity collectionEntity : list) {
                if (collectionId.equals(collectionEntity.getCollectionId())) {
                    return collectionEntity;
                }
            }
        }

        return null;
    }

    @Override
    public void deleteCollection(@NonNull Context context, @NonNull LocalCollectionEntity entity) {
        mCollectionDao.deleteCollection(entity);
    }

    @Override
    public List<LocalCollectionEntity> search(@NonNull Context context, @NonNull String keyword) {
        List<LocalCollectionEntity> listAll = getAllCollections(context);
        List<LocalCollectionEntity> listResult = new ArrayList<>();

        if (listAll != null && !listAll.isEmpty()) {
            for (int i = 0, n = listAll.size(); i < n; i++) {
                LocalCollectionEntity entity = listAll.get(i);

                if (entity.getCollectionName().contains(keyword)) {
                    listResult.add(entity);
                }
            }
        }

        return listResult;
    }

    @Override
    public boolean addSongIntoCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection, @NonNull LocalSongEntity song) {
        if (collection.containSong(song.getSongId())) {
            return false;
        }

        collection.addSongIntoCollection(song.getSongId());
        mCollectionDao.updateCollection(collection);
        return true;
    }

    @Override
    public boolean removeSongFromCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection, @NonNull LocalSongEntity song) {
        if (!collection.containSong(song.getSongId())) {
            return false;
        }

        collection.removeSong(song.getSongId());
        mCollectionDao.updateCollection(collection);
        return true;
    }

    @Override
    public void clearSongsFromCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection) {
        collection.clearSongs();
        mCollectionDao.updateCollection(collection);
    }

    @Override
    public boolean isCollectionNameExist(@NonNull Context context, @NonNull String collectionName) {
        List<LocalCollectionEntity> list = getAllCollections(context);

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
    public void createNewCollection(@NonNull Context context, LocalCollectionEntity entity) {
        mCollectionDao.createNewCollection(entity);
    }


}
