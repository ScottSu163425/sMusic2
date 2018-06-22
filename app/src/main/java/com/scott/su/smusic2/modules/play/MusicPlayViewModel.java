package com.scott.su.smusic2.modules.play;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.source.local.AppConfig;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/5/3
 */

public class MusicPlayViewModel extends BaseAndroidViewModel {
    private MutableLiveData<Boolean> mLiveDataIsRepeatAll;
    private MutableLiveData<Boolean> mLiveDataIsRepeatOne;
    private MutableLiveData<Boolean> mLiveDataIsRepeatShuffle;


    public MusicPlayViewModel(@NonNull Application application) {
        super(application);

        mLiveDataIsRepeatAll = new MutableLiveData<>();
        mLiveDataIsRepeatOne = new MutableLiveData<>();
        mLiveDataIsRepeatShuffle = new MutableLiveData<>();
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
