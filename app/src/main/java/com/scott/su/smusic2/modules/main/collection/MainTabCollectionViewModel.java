package com.scott.su.smusic2.modules.main.collection;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;

import java.util.List;

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

    }

    public MutableLiveData<List<LocalCollectionEntity>> getLiveDataCollectionList() {
        return mLiveDataCollectionList;
    }
}
