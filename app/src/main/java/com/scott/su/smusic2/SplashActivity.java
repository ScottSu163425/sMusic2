package com.scott.su.smusic2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.main.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/25
 */
public class SplashActivity extends BaseActivity {
    protected boolean mDelaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Observable.empty()
//                .delay(3, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        MainActivity.getStartIntent(getActivity(), true);
//                    }
//                });
        MainActivity.getStartIntent(getActivity(), true);
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
