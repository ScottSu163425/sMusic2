package com.scott.su.common.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;

import com.scott.su.common.R;


public abstract class BaseActivity extends AppCompatActivity {
    private static final int sDefaultAnimOpenIn = R.anim.slide_in_right;
    private static final int sDefaultAnimOpenOut = android.R.anim.fade_out;
    private static final int sDefaultAnimCloseIn = R.anim.fade_in;
    private static final int sDefaultAnimCloseOut = R.anim.slide_out_right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(provideAnimOpenIn(), provideAnimOpenOut());
        } else {
            if (autoTransition()) {
                getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            overridePendingTransition(provideAnimCloseIn(), provideAnimCloseOut());
        }
    }

    @Override
    public void startActivity(Intent intent) {
        //以Transition方式启动界面
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    protected boolean autoTransition() {
        return true;
    }

    protected
    @AnimRes
    int provideAnimOpenIn() {
        return sDefaultAnimOpenIn;
    }

    protected
    @AnimRes
    int provideAnimOpenOut() {
        return sDefaultAnimOpenOut;
    }

    protected
    @AnimRes
    int provideAnimCloseIn() {
        return sDefaultAnimCloseIn;
    }

    protected
    @AnimRes
    int provideAnimCloseOut() {
        return sDefaultAnimCloseOut;
    }

    protected AppCompatActivity getActivity() {
        return this;
    }


}
