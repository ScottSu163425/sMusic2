package com.scott.su.smusic2.modules.common;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/24
 */

public abstract class BaseSongListViewModel extends BaseAndroidViewModel {
    private MutableLiveData<Boolean> mLiveDataCollectSuccess = new MutableLiveData<>();
    private MutableLiveData<String> mLiveDataCollectFailMessage = new MutableLiveData<>();


    public BaseSongListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getLiveDataCollectSuccess() {
        return mLiveDataCollectSuccess;
    }

    public MutableLiveData<String> getLiveDataCollectFailMessage() {
        return mLiveDataCollectFailMessage;
    }

    public void collectSong(final LocalCollectionEntity collection, final LocalSongEntity song) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> emitter) throws Exception {
                boolean success = LocalCollectionDataSource.getInstance(getContext())
                        .addSongIntoCollection(getContext(), collection, song);

                emitter.onNext(success);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Exception {
                        getLiveDataCollectSuccess().setValue(success);

                        if (!success) {
                            getLiveDataCollectFailMessage()
                                    .setValue(getContext().getString(R.string.error_already_exist_in_collection));
                        }

                    }
                });
    }

}
