package com.scott.su.smusic2.modules.collection.select;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/23
 */

public class CollectionSelectViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<LocalCollectionEntity>> mLiveDataCollections = new MutableLiveData<>();

    public CollectionSelectViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {
        getCollections();
    }

    public MutableLiveData<List<LocalCollectionEntity>> getLiveDataCollections() {
        return mLiveDataCollections;
    }

    private void getCollections() {

        Observable.create(new ObservableOnSubscribe<List<LocalCollectionEntity>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<LocalCollectionEntity>> emitter) throws Exception {
                List<LocalCollectionEntity> list
                        = LocalCollectionDataSource.getInstance(getContext()).getAllCollections(getContext());

                if (list == null) {
                    list = new ArrayList<LocalCollectionEntity>();
                }

                emitter.onNext(list);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getLiveDataLoading().setValue(true);
                    }
                })
                .subscribe(new Consumer<List<LocalCollectionEntity>>() {
                    @Override
                    public void accept(List<LocalCollectionEntity> localCollectionEntities) throws Exception {
                        getLiveDataLoading().setValue(false);
                        getLiveDataEmpty().setValue(localCollectionEntities.isEmpty());
                        mLiveDataCollections.setValue(localCollectionEntities);
                    }
                });

    }

}
