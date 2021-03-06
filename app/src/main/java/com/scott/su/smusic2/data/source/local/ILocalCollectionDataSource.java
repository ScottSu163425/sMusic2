package com.scott.su.smusic2.data.source.local;


import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public interface ILocalCollectionDataSource {

    boolean isCollectionNameExist(@NonNull Context context, @NonNull String collectionName);

    void createNewCollection(@NonNull Context context, @NonNull LocalCollectionEntity entity);

    List<LocalCollectionEntity> getAllCollections(@NonNull Context context);

    LocalCollectionEntity getCollection(@NonNull Context context, @NonNull String collectionId);

    boolean addSongIntoCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection,
                                  @NonNull LocalSongEntity song);

    boolean removeSongFromCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection,
                                     @NonNull LocalSongEntity song);

    void clearSongsFromCollection(@NonNull Context context, @NonNull LocalCollectionEntity collection);

    void deleteCollection(@NonNull Context context, @NonNull LocalCollectionEntity entity);

    List<LocalCollectionEntity> search(@NonNull Context context, @NonNull String keyword);

}
