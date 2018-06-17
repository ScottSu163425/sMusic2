package com.scott.su.common.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/29
 */

public abstract class BaseAndroidViewModel extends AndroidViewModel {
    private Context mContext;

    protected abstract void start();

    public BaseAndroidViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    protected Context getApplicationContext() {
        return mContext;
    }


}
