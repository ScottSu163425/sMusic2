package com.scott.su.smusic2.modules.main.collection;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class MainTabCollectionViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<LocalCollectionEntity>> mLiveDataCollectionList = new MutableLiveData<>();
    private MutableLiveData<LocalCollectionEntity> mLiveDataCollectionRemoved = new MutableLiveData<>();


    public MainTabCollectionViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {
        refreshCollectionList();
    }

    public MutableLiveData<List<LocalCollectionEntity>> getLiveDataCollectionList() {
        return mLiveDataCollectionList;
    }

    public MutableLiveData<LocalCollectionEntity> getLiveDataCollectionRemoved() {
        return mLiveDataCollectionRemoved;
    }

    public void refreshCollectionList() {
        getCollectionList();
    }

    public void removeCollection(final LocalCollectionEntity entity) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                LocalCollectionDataSource.getInstance(getContext())
                        .removeCollection(getContext(),entity);
                emitter.onNext(true);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) throws Exception {
                        mLiveDataCollectionRemoved.setValue(entity);
                    }
                });
    }

    private void getCollectionList() {
        Observable.create(new ObservableOnSubscribe<List<LocalCollectionEntity>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<LocalCollectionEntity>> emitter) throws Exception {
                emitter.onNext(LocalCollectionDataSource.getInstance(getContext()).getAllCollections(getContext()));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalCollectionEntity>>() {
                    @Override
                    public void accept(List<LocalCollectionEntity> localCollectionEntities) throws Exception {
                        mLiveDataCollectionList.setValue(localCollectionEntities);
                    }
                });
    }
}
