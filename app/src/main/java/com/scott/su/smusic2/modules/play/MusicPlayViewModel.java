package com.scott.su.smusic2.modules.play;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.source.local.AppConfig;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/5/3
 */

public class MusicPlayViewModel extends BaseAndroidViewModel {
    private AppConfig mAppConfig;
    private MutableLiveData<Boolean> mLiveDataIsRepeatAll = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLiveDataIsRepeatOne = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLiveDataIsRepeatShuffle = new MutableLiveData<>();


    public MusicPlayViewModel(@NonNull Application application) {
        super(application);

        mAppConfig = AppConfig.getInstance();

    }

    @Override
    protected void start() {
        mLiveDataIsRepeatAll.setValue(mAppConfig.isRepeatAll());
        mLiveDataIsRepeatOne.setValue(mAppConfig.isRepeatOne());
        mLiveDataIsRepeatShuffle.setValue(mAppConfig.isRepeatShuffle());
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
            mAppConfig.setRepeatOne(true);
            mLiveDataIsRepeatAll.setValue(false);
            mLiveDataIsRepeatOne.setValue(true);
            mLiveDataIsRepeatShuffle.setValue(false);
        } else if (mLiveDataIsRepeatOne.getValue()) {
            mAppConfig.setRepeatShuffle(true);
            mLiveDataIsRepeatAll.setValue(false);
            mLiveDataIsRepeatOne.setValue(false);
            mLiveDataIsRepeatShuffle.setValue(true);
        } else if (mLiveDataIsRepeatShuffle.getValue()) {
            mAppConfig.setRepeatAll(true);
            mLiveDataIsRepeatAll.setValue(true);
            mLiveDataIsRepeatOne.setValue(false);
            mLiveDataIsRepeatShuffle.setValue(false);
        }
    }

}
