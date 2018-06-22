package com.scott.su.smusic2.data.source.local;


import android.support.annotation.NonNull;

import com.scott.su.smusic2.data.entity.LocalCollectionEntity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public interface ILocalCollectionDataSource {

    boolean isCollectionNameExist(@NonNull String collectionName);

    void createNewCollection(@NonNull LocalCollectionEntity entity);

    List<LocalCollectionEntity> getAllCollections();


}
