package com.scott.su.smusic2.modules.main.song;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.common.LocalSongEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/29
 */

public class MainTabSongViewModel extends BaseAndroidViewModel {
    private MutableLiveData<List<LocalSongEntity>> mLiveDataSongList = new MutableLiveData<>();


    public MainTabSongViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void start() {
        //test

        List<LocalSongEntity> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(new LocalSongEntity());
        }

        mLiveDataSongList.setValue(list);
    }

    public MutableLiveData<List<LocalSongEntity>> getLiveDataSongList() {
        return mLiveDataSongList;
    }

}
