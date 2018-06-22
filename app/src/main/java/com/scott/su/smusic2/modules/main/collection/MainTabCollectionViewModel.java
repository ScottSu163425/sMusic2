package com.scott.su.smusic2.modules.main.collection;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class MainTabCollectionViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<LocalCollectionEntity>> mLiveDataCollectionList;

    public MainTabCollectionViewModel(@NonNull Application application) {
        super(application);

        mLiveDataCollectionList = new MutableLiveData<>();
    }

    @Override
    protected void start() {
        refreshCollectionList();
    }

    public MutableLiveData<List<LocalCollectionEntity>> getLiveDataCollectionList() {
        return mLiveDataCollectionList;
    }

    public void refreshCollectionList() {
        Observable.create(new ObservableOnSubscribe<List<LocalCollectionEntity>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<LocalCollectionEntity>> emitter) throws Exception {
                emitter.onNext(LocalCollectionDataSource.getInstance(getApplicationContext()).getAllCollections());
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
