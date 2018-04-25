package com.scott.su.smusic2.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.R;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/25
 */

public class MainActivity extends BaseActivity {
    private MainFragment mFragmentMain;


    public static Intent getStartIntent(Context context, boolean start) {
        Intent intent = new Intent(context, MainActivity.class);

        if (start) {
            context.startActivity(intent);
        }

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFragmentMain = MainFragment.newInstance();

        showFragment(mFragmentMain, R.id.fl_container_main);
    }


}
