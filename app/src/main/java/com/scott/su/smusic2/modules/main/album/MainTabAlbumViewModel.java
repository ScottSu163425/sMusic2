package com.scott.su.smusic2.modules.main.album;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.source.local.LocalSongDataSource;

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
 * 日期: 2018/5/16
 */

public class MainTabAlbumViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<LocalAlbumEntity>> mLiveDataAlbumList = new MutableLiveData<>();


    public MainTabAlbumViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {
        Observable.create(new ObservableOnSubscribe<List<LocalAlbumEntity>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<LocalAlbumEntity>> emitter) throws Exception {
                emitter.onNext(LocalSongDataSource.getInstance().getAllAlbums(getApplicationContext()));
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<LocalAlbumEntity>>() {
                    @Override
                    public void accept(List<LocalAlbumEntity> localAlbumEntities) throws Exception {
                        mLiveDataAlbumList.setValue(localAlbumEntities);
                    }
                });
    }

    public MutableLiveData<List<LocalAlbumEntity>> getLiveDataSongList() {
        return mLiveDataAlbumList;
    }

}
