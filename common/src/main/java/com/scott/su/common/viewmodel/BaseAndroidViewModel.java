package com.scott.su.common.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/29
 */

public abstract class BaseAndroidViewModel extends AndroidViewModel {
    private Context mContext;
    private MutableLiveData<Boolean> mLiveDataLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mLiveDataEmpty = new MutableLiveData<>();


    protected abstract void start();

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    protected Context getContext() {
        return mContext;
    }

    public MutableLiveData<Boolean> getLiveDataLoading() {
        return mLiveDataLoading;
    }

    public MutableLiveData<Boolean> getLiveDataEmpty() {
        return mLiveDataEmpty;
    }
}
