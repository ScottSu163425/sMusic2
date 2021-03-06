package com.scott.su.smusic2.modules.play;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.AppConfig;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;
import com.scott.su.smusic2.modules.common.BaseSongListViewModel;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/5/3
 */

public class MusicPlayViewModel extends BaseSongListViewModel {
    private MutableLiveData<Boolean> mLiveDataIsRepeatAll= new MutableLiveData<>();
    private MutableLiveData<Boolean> mLiveDataIsRepeatOne= new MutableLiveData<>();
    private MutableLiveData<Boolean> mLiveDataIsRepeatShuffle= new MutableLiveData<>();


    public MusicPlayViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    protected void start() {
        mLiveDataIsRepeatAll.setValue(AppConfig.getInstance().isRepeatAll());
        mLiveDataIsRepeatOne.setValue(AppConfig.getInstance().isRepeatOne());
        mLiveDataIsRepeatShuffle.setValue(AppConfig.getInstance().isRepeatShuffle());
    }

    public MutableLiveData<Boolean> getLiveDataIsRepeatAll() {
        return mLiveDataIsRepeatAll;
    }

    public MutableLiveData<Boolean> getLiveDataIsRepeatOne() {
        return mLiveDataIsRepeatOne;
    }

    public MutableLiveData<Boolean> getLiveDataIsRepeatShuffle() {
        return mLiveDataIsRepeatShuffle;
    }

    public void toggleRepeatMode() {
        if (mLiveDataIsRepeatAll.getValue()) {
            AppConfig.getInstance().setRepeatOne();
            mLiveDataIsRepeatAll.setValue(false);
            mLiveDataIsRepeatOne.setValue(true);
            mLiveDataIsRepeatShuffle.setValue(false);
        } else if (mLiveDataIsRepeatOne.getValue()) {
            AppConfig.getInstance().setRepeatShuffle();
            mLiveDataIsRepeatAll.setValue(false);
            mLiveDataIsRepeatOne.setValue(false);
            mLiveDataIsRepeatShuffle.setValue(true);
        } else if (mLiveDataIsRepeatShuffle.getValue()) {
            AppConfig.getInstance().setRepeatAll();
            mLiveDataIsRepeatAll.setValue(true);
            mLiveDataIsRepeatOne.setValue(false);
            mLiveDataIsRepeatShuffle.setValue(false);
        }
    }

}
