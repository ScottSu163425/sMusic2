package com.scott.su.smusic2.modules.album;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.source.local.LocalSongDataSource;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/19
 */

public class AlbumDetailViewModel extends BaseAndroidViewModel {

    private MutableLiveData<LocalAlbumEntity> mLiveDataAlbum;
    private long mAlbumId;

    public AlbumDetailViewModel(@NonNull Application application) {
        super(application);

        mLiveDataAlbum = new MutableLiveData<>();
    }

    public void setAlbumId(long albumId) {
        this.mAlbumId = albumId;
    }

    @Override
    protected void start() {
        getAlbum();
    }

    public MutableLiveData<LocalAlbumEntity> getLiveDataAlbum() {
        return mLiveDataAlbum;
    }

    private void getAlbum() {
        Observable.just(mAlbumId)
                .map(new Function<Long, LocalAlbumEntity>() {
                    @Override
                    public LocalAlbumEntity apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                        return LocalSongDataSource.getInstance().getAlbum(getApplicationContext(), aLong);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LocalAlbumEntity>() {
                    @Override
                    public void accept(LocalAlbumEntity localAlbumEntity) throws Exception {
                        mLiveDataAlbum.setValue(localAlbumEntity);
                    }
                });
    }
}
