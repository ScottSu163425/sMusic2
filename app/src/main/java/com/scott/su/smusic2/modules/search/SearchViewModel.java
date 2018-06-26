package com.scott.su.smusic2.modules.search;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;
import com.scott.su.smusic2.data.source.local.LocalSongDataSource;

import java.util.ArrayList;
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
 * 日期: 2018/6/26
 */

public class SearchViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<Object>> mLiveDataResult = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {

    }

    public MutableLiveData<List<Object>> getLiveDataResult() {
        return mLiveDataResult;
    }

    public void search(@NonNull final String keyWord) {
        Observable.create(new ObservableOnSubscribe<List<Object>>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<Object>> emitter) throws Exception {
                List<Object> result = new ArrayList<>();

                List<LocalSongEntity> songs = LocalSongDataSource.getInstance().searchSong(getContext(), keyWord);
                List<LocalAlbumEntity> albums = LocalSongDataSource.getInstance().searchAlbum(getContext(), keyWord);
                List<LocalCollectionEntity> collections = LocalCollectionDataSource.getInstance(getContext()).search(getContext(), keyWord);

                if (songs != null && !songs.isEmpty()) {
                    result.add(getContext().getString(R.string.song));
                    result.addAll(songs);
                }

                if (collections != null && !collections.isEmpty()) {
                    result.add(getContext().getString(R.string.collection));
                    result.addAll(collections);
                }

                if (albums != null && !albums.isEmpty()) {
                    result.add(getContext().getString(R.string.album));
                    result.addAll(albums);
                }

                emitter.onNext(result);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Object>>() {
                    @Override
                    public void accept(List<Object> objects) throws Exception {
                        mLiveDataResult.setValue(objects);
                    }
                });


    }

}
