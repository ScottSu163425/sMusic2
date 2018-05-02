package com.scott.su.smusic2.modules.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.MainTabListDragEvent;
import com.scott.su.smusic2.databinding.ActivityMainBinding;
import com.scott.su.smusic2.modules.main.album.MainTabAlbumFragment;
import com.scott.su.smusic2.modules.main.collection.MainTabCollectionFragment;
import com.scott.su.smusic2.modules.main.drawer.MainDrawerMenuFragment;
import com.scott.su.smusic2.modules.main.recommend.MainTabRecommendFragment;
import com.scott.su.smusic2.modules.main.song.MainTabSongFragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */

public class MainActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(getStartIntent(context));
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }


    private ActivityMainBinding mBinding;
    private List<Fragment> mListTabContentFragment;
    private MainTabRecommendFragment mTabFragmentRecommend;
    private MainTabSongFragment mTabFragmentSong;
    private MainTabCollectionFragment mTabFragmentCollection;
    private MainTabAlbumFragment mTabFragmentAlbum;
    private MainDrawerMenuFragment mDrawerMenuFragment;

    private MainViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.toolbar.setTitle(R.string.app_name);
        setSupportActionBar(mBinding.toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.drawer_open, R.string.drawer_close);
        mBinding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mListTabContentFragment = new ArrayList<>();
        mTabFragmentRecommend = MainTabRecommendFragment.newInstance();
        mTabFragmentSong = MainTabSongFragment.newInstance();
        mTabFragmentCollection = MainTabCollectionFragment.newInstance();
        mTabFragmentAlbum = MainTabAlbumFragment.newInstance();

        mListTabContentFragment.add(mTabFragmentRecommend);
        mListTabContentFragment.add(mTabFragmentSong);
        mListTabContentFragment.add(mTabFragmentCollection);
        mListTabContentFragment.add(mTabFragmentAlbum);

        mDrawerMenuFragment = MainDrawerMenuFragment.newInstance();
        showFragment(mDrawerMenuFragment, mBinding.flContainerMenuDrawerMain.getId());

        mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.setFragments(mListTabContentFragment);
        mViewPagerAdapter.setTitles(new String[]{
                getString(R.string.tab_main_recommend),
                getString(R.string.tab_main_song),
                getString(R.string.tab_main_collection),
                getString(R.string.tab_main_album)});
        mBinding.vpMain.setOffscreenPageLimit(mListTabContentFragment.size());
        mBinding.vpMain.setAdapter(mViewPagerAdapter);
        mBinding.tabLayoutMain.setupWithViewPager(mBinding.vpMain);

        mBinding.fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackbar(mBinding.fabMain, "Click FAB.");
                hideFab(v);
            }
        });

    }

    @Override
    protected boolean subscribeEvents() {
        return true;
    }

    @Subscribe
    public void onEventMainTabListDragging(MainTabListDragEvent event) {
        if (event.isDragging()) {
            hideFab(mBinding.fabMain);
        } else {
            showFab(mBinding.fabMain);
        }
    }

    private void showFab(@NonNull View fab) {
        Log.e("===>","showFab");

        fab.animate()
                .setDuration(R.integer.duration_anim_m)
                .translationY(0)
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    private void hideFab(@NonNull View fab) {
        Log.e("===>","hideFab");

        fab.animate()
                .setDuration(R.integer.duration_anim_m)
                .translationY(fab.getBottom() * 1.5f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }


}
