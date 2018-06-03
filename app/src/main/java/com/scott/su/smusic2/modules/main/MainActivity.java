package com.scott.su.smusic2.modules.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.PagerSnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.core.MusicPlayCallback;
import com.scott.su.smusic2.core.MusicPlayCallbackBus;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.LocalSongHelper;
import com.scott.su.smusic2.databinding.ActivityMainBinding;
import com.scott.su.smusic2.modules.main.album.MainTabAlbumFragment;
import com.scott.su.smusic2.modules.main.collection.MainTabCollectionFragment;
import com.scott.su.smusic2.modules.main.drawer.MainDrawerMenuFragment;
import com.scott.su.smusic2.modules.main.recommend.MainTabRecommendFragment;
import com.scott.su.smusic2.modules.main.song.MainTabSongFragment;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

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
    private MusicPlayCallback mMusicPlayCallback;

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

        initCurrentPlayingCard();
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
    protected void onDestroy() {
        super.onDestroy();

        MusicPlayCallbackBus.getInstance().unregisterCallback(mMusicPlayCallback);
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
        if (mBinding.cardCurrentPlaying.getVisibility() == View.VISIBLE) {
            return;
        }

        if (mCurrentPlayingSong == null) {
            return;
        }

        mBinding.cardCurrentPlaying
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mBinding.cardCurrentPlaying.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    private void hideCurrentPlayingCard() {
        mBinding.cardCurrentPlaying
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(mBinding.cardCurrentPlaying.getBottom())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mBinding.cardCurrentPlaying.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    private void showExit() {
        showSnackbar(getString(R.string.tip_exit), getString(R.string.confirm),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MusicPlayController.getInstance().stop(getActivity());
                        finish();
                    }
                });
    }

    private LocalSongEntity mCurrentPlayingSong;
    private List<LocalSongEntity> mCurrentPlayQueue = new ArrayList<>();

    private void initCurrentPlayingCard() {
        mBinding.cardCurrentPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayActivity.start(getActivity(), (ArrayList<LocalSongEntity>) mCurrentPlayQueue,
                        mCurrentPlayingSong, new View[]{mBinding.ivCover});
            }
        });

        mBinding.ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayController.getInstance().playPause(getActivity());
            }
        });

        mMusicPlayCallback = new MusicPlayCallback() {
            @Override
            public void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                updateCurrentPlaying(song, playQueue);
                mBinding.ivPlayPause.setImageResource(R.drawable.ic_pause_black);
            }

            @Override
            public void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                updateCurrentPlaying(song, playQueue);

                mBinding.ivPlayPause.setImageResource(R.drawable.ic_pause_black);
            }

            @Override
            public void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                mBinding.ivPlayPause.setImageResource(R.drawable.ic_play_arrow_black);
            }

            @Override
            public void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {

            }

            @Override
            public void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {

            }
        };

        MusicPlayCallbackBus.getInstance().registerCallback(mMusicPlayCallback);
    }

    /**
     * 更新当前播放歌曲信息
     *
     * @param currentPlayingSong
     */
    private void updateCurrentPlaying(LocalSongEntity currentPlayingSong, List<LocalSongEntity> playQueue) {
        showCurrentPlayingCard();

        mCurrentPlayingSong = currentPlayingSong;
        mCurrentPlayQueue = playQueue;

        mBinding.tvTitleCurrentPlaying.setText(currentPlayingSong.getTitle());
        mBinding.tvArtist.setText(currentPlayingSong.getArtist());

        ImageLoader.load(getActivity(), currentPlayingSong.getAlbumCoverPath(), mBinding.ivCover);
    }


}
