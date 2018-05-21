package com.scott.su.smusic2.modules.main;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.LocalSongHelper;
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
 * 描述: 主页
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

//        mListTabContentFragment.add(mTabFragmentRecommend);
        mListTabContentFragment.add(mTabFragmentSong);
        mListTabContentFragment.add(mTabFragmentCollection);
        mListTabContentFragment.add(mTabFragmentAlbum);

        mDrawerMenuFragment = MainDrawerMenuFragment.newInstance();
        showFragment(mDrawerMenuFragment, mBinding.flContainerMenuDrawerMain.getId());

        mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.setFragments(mListTabContentFragment);
        mViewPagerAdapter.setTitles(new String[]{
//                getString(R.string.tab_main_recommend),
                getString(R.string.tab_main_song),
                getString(R.string.tab_main_collection),
                getString(R.string.tab_main_album)});
        mBinding.vpMain.setOffscreenPageLimit(mListTabContentFragment.size());
        mBinding.vpMain.setAdapter(mViewPagerAdapter);
        mBinding.tabLayoutMain.setupWithViewPager(mBinding.vpMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (R.id.action_search == id) {
            showToast("search");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(Gravity.START)) {
            mBinding.drawerLayout.closeDrawer(Gravity.START);
            return;
        }

        showExit();
    }

    @Override
    protected boolean subscribeEvents() {
        return true;
    }

    @Subscribe
    public void onEventMainTabListScroll(MainTabListScrollEvent event) {
        if (event.isIdle()) {
            showCurrentPlayingCard();
        } else {
            hideCurrentPlayingCard();
        }
    }

    private void showCurrentPlayingCard() {
        mBinding.cardCurrentPlaying
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(0)
                .start();
    }

    private void hideCurrentPlayingCard() {
        mBinding.cardCurrentPlaying
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(mBinding.cardCurrentPlaying.getBottom())
                .start();
    }

    private void showExit() {
        showSnackbar(getString(R.string.tip_exit), getString(R.string.confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
    }

    /**
     * 更新当前播放歌曲信息
     *
     * @param currentPlayingSong
     */
    private void updateCurrentPlaying(@NonNull LocalSongEntity currentPlayingSong) {
        mBinding.tvTitleCurrentPlaying.setText(currentPlayingSong.getTitle());
        mBinding.tvArtist.setText(currentPlayingSong.getArtist());

        ImageLoader.load(getActivity(), currentPlayingSong.getAlbumCoverPath(), mBinding.ivCover);
    }


}
