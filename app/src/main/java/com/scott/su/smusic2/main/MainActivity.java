package com.scott.su.smusic2.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.main.album.MainTabAlbumFragment;
import com.scott.su.smusic2.main.collection.MainTabCollectionFragment;
import com.scott.su.smusic2.main.recommend.MainTabRecommendFragment;
import com.scott.su.smusic2.main.song.MainTabSongFragment;

import java.util.ArrayList;
import java.util.List;

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

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFAB;

    private List<Fragment> mListTabContentFragment;
    private MainTabRecommendFragment mTabFragmentRecommend;
    private MainTabSongFragment mTabFragmentSong;
    private MainTabCollectionFragment mTabFragmentCollection;
    private MainTabAlbumFragment mTabFragmentAlbum;

    private MainViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mTabLayout = findViewById(R.id.tab_layout_main);
        mViewPager = findViewById(R.id.vp_main);
        mFAB = findViewById(R.id.fab_main);

        mListTabContentFragment = new ArrayList<>();
        mTabFragmentRecommend = MainTabRecommendFragment.newInstance();
        mTabFragmentSong = MainTabSongFragment.newInstance();
        mTabFragmentCollection = MainTabCollectionFragment.newInstance();
        mTabFragmentAlbum = MainTabAlbumFragment.newInstance();

        mListTabContentFragment.add(mTabFragmentRecommend);
        mListTabContentFragment.add(mTabFragmentSong);
        mListTabContentFragment.add(mTabFragmentCollection);
        mListTabContentFragment.add(mTabFragmentAlbum);

        mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.setFragments(mListTabContentFragment);
        mViewPagerAdapter.setTitles(new String[]{
                getString(R.string.tab_main_recommend),
                getString(R.string.tab_main_song),
                getString(R.string.tab_main_collection),
                getString(R.string.tab_main_album)});

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


}
