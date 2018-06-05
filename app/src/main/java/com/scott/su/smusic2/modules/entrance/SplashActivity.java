package com.scott.su.smusic2.modules.entrance;

import android.Manifest;
import android.os.Bundle;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.entity.PermissionEntity;
import com.scott.su.common.interfaces.PermissionCallback;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.source.local.AppConfig;
import com.scott.su.smusic2.modules.main.MainActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */
public class SplashActivity extends BaseActivity {
    protected boolean mDelaying;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean firstTimeLaunch = AppConfig.getInstance().isFirstTimeLaunch();

        if (firstTimeLaunch) {

        }

        AppConfig.getInstance().setFirstTimeLaunch(false);


        Observable.just("")
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        requestPermissionsIfNeed();
                    }
                });
    }

    private void requestPermissionsIfNeed() {
        //startActivity(MainActivity.getStartIntent(getActivity()));
        final String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        requestPermissions(permissions, new PermissionCallback() {
            @Override
            public void onPermissionGranted(List<PermissionEntity> permissions, boolean allGranted) {
                MainActivity.start(getActivity());
                finish();
            }

            @Override
            public void onPermissionDenied(List<PermissionEntity> permissions, boolean allDenied) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //Can not be closed by pressing back button when delaying.

        if (mDelaying) {

        } else {
            super.onBackPressed();
        }
    }


}
