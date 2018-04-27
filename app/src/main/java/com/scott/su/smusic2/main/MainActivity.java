package com.scott.su.smusic2.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.R;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/25
 */

public class MainActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(getStartIntent(context));
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        mTabLayout = findViewById(R.id.tab_layout_main);

        mTabLayout.addTab(mTabLayout.newTab().setText("推荐"), 0, false);
        mTabLayout.addTab(mTabLayout.newTab().setText("歌曲"), 1, true);
        mTabLayout.addTab(mTabLayout.newTab().setText("收藏"), 2, false);
        mTabLayout.addTab(mTabLayout.newTab().setText("专辑"), 3, false);
    }


}
