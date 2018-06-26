package com.scott.su.smusic2.modules.about;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Explode;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.databinding.ActivityAboutBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/25
 */

public class AboutActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(getStartIntent(context));
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    private ActivityAboutBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        mBinding.toolbar.setTitle("");
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(mBinding.toolbar);

        setUpItemClick();
    }

    private void setUpItemClick() {
        mBinding.viewAppHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(getString(R.string.app_home_page_url));
            }
        });

        mBinding.viewAuthorHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPage(getString(R.string.author_home_page_url));
            }
        });

    }

    private void openPage(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
