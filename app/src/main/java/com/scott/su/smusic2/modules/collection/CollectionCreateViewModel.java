package com.scott.su.smusic2.modules.collection;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.scott.su.common.manager.ToastMaker;
import com.scott.su.common.viewmodel.BaseAndroidViewModel;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.source.local.LocalCollectionDataSource;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/14
 */

public class CollectionCreateViewModel extends BaseAndroidViewModel {
    private MutableLiveData<Boolean> mLiveDataCreateCollectionSuccess;

    public CollectionCreateViewModel(@NonNull Application application) {
        super(application);

        mLiveDataCreateCollectionSuccess = new MutableLiveData<>();
    }

    @Override
    protected void start() {

    }

    public MutableLiveData<Boolean> getLiveDataCreateCollectionSuccess() {
        return mLiveDataCreateCollectionSuccess;
    }

    public void saveNewCollection(final String collectionName) {
        Observable.just(collectionName)
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(@io.reactivex.annotations.NonNull String collectionName) throws Exception {
                        boolean isNameExist = LocalCollectionDataSource.getInstance(getApplicationContext())
                                .isCollectionNameExist(collectionName);

                        if (isNameExist) {
                            return false;
                        }

                        LocalCollectionEntity collectionEntity = new LocalCollectionEntity();
                        collectionEntity.setCollectionId(System.currentTimeMillis());
                        collectionEntity.setCollectionName(collectionName);

                        LocalCollectionDataSource.getInstance(getApplicationContext())
                                .createNewCollection(collectionEntity);
                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean success) throws Exception {
                        if (success) {
                            mLiveDataCreateCollectionSuccess.setValue(true);
                        }else {
                            getLiveDataTip().setValue(getApplicationContext().getString(R.string.error_collection_name_exist));
                        }
                    }
                });
    }


}
