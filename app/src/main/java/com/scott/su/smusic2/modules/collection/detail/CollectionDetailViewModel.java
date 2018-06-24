package com.scott.su.smusic2.modules.collection.detail;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.util.ListUtil;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;
import com.scott.su.smusic2.data.source.local.LocalSongDataSource;
import com.scott.su.smusic2.modules.common.BaseSongListViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/23
 */

public class CollectionDetailViewModel extends BaseSongListViewModel {
    private String mCollectionId;
    private MutableLiveData<LocalCollectionEntity> mLiveDataCollection = new MutableLiveData<>();
    private MutableLiveData<List<LocalSongEntity>> mLiveDataCollectionSongs = new MutableLiveData<>();


    public CollectionDetailViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {
        fetchCollection();
    }

    public void setCollectionId(String collectionId) {
        mCollectionId = collectionId;
    }

    public String getCollectionId() {
        return mCollectionId;
    }

    public MutableLiveData<LocalCollectionEntity> getLiveDataCollection() {
        return mLiveDataCollection;
    }

    public MutableLiveData<List<LocalSongEntity>> getLiveDataCollectionSongs() {
        return mLiveDataCollectionSongs;
    }

    public void removeSong(@NonNull final LocalSongEntity song) {
        final LocalCollectionEntity collection = mLiveDataCollection.getValue();

        Observable.just(collection)
                .map(new Function<LocalCollectionEntity, LocalCollectionEntity>() {
                    @Override
                    public LocalCollectionEntity apply(@io.reactivex.annotations.NonNull LocalCollectionEntity entity) throws Exception {
                        LocalCollectionDataSource.getInstance(getContext())
                                .removeSongFromCollection(getContext(), collection, song);
                        return collection;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocalCollectionEntity>() {
                    @Override
                    public void accept(LocalCollectionEntity collectionEntity) throws Exception {
                        fetchCollection();
                    }
                });


    }

    private void fetchCollection() {
        Observable.just(mCollectionId)
                .map(new Function<String, LocalCollectionEntity>() {
                    @Override
                    public LocalCollectionEntity apply(@io.reactivex.annotations.NonNull String string) throws Exception {
                        return LocalCollectionDataSource.getInstance(getContext()).getAllCollection(getContext(), string);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocalCollectionEntity>() {
                    @Override
                    public void accept(LocalCollectionEntity collectionEntity) throws Exception {
                        mLiveDataCollection.setValue(collectionEntity);

                        fetchCollectionSongs();
                    }
                });
    }

    private void fetchCollectionSongs() {
        LocalCollectionEntity collection = mLiveDataCollection.getValue();

        if (collection == null) {
            collection = new LocalCollectionEntity();
        }

        String[] idArr = collection.getCollectionSongIdArr();

        if (idArr == null) {
            idArr = new String[]{};
        }

        Observable.just(idArr)
                .map(new Function<String[], List<LocalSongEntity>>() {
                    @Override
                    public List<LocalSongEntity> apply(@io.reactivex.annotations.NonNull String[] strings) throws Exception {
                        List<LocalSongEntity> songs = LocalSongDataSource.getInstance().getSongsById(getContext(), strings);

                        if (songs == null) {
                            songs = new ArrayList<LocalSongEntity>();
                        }

                        ListUtil.reverse(songs);

                        return songs;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalSongEntity>>() {
                    @Override
                    public void accept(List<LocalSongEntity> localSongEntities) throws Exception {
                        mLiveDataCollectionSongs.setValue(localSongEntities);
                    }
                });
    }


}
