package com.scott.su.smusic2.modules.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.FragmentHelper;
import com.scott.su.common.manager.SnackBarHelper;
import com.scott.su.common.manager.ToastHelper;
import com.scott.su.common.util.ViewUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.databinding.ActivityMainBinding;
import com.scott.su.smusic2.modules.main.album.MainTabAlbumFragment;
import com.scott.su.smusic2.modules.main.collection.MainTabCollectionFragment;
import com.scott.su.smusic2.modules.main.drawer.MainDrawerMenuFragment;
import com.scott.su.smusic2.modules.main.song.MainTabSongFragment;

import org.greenrobot.eventbus.EventBus;
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

    private static final int INDEX_TAB_SONG = 0;
    private static final int INDEX_TAB_COLLECTION = 1;
    private static final int INDEX_TAB_ALBUM = 2;


    private ActivityMainBinding mBinding;
    private List<Fragment> mListTabContentFragment;
    private MainTabSongFragment mTabFragmentSong;
    private MainTabCollectionFragment mTabFragmentCollection;
    private MainTabAlbumFragment mTabFragmentAlbum;
    private MainDrawerMenuFragment mDrawerMenuFragment;
    private MainViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initTitle();
        initDrawer();
        initViewPager();

        mBinding.fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewUtil.isFastDoubleClick()) {
                    return;
                }

                ToastHelper.getInstance()
                        .showToast(getActivity(), mBinding.vpMain.getCurrentItem() + "");
            }
        });

        EventBus.getDefault().register(this);
    }

    private void initTitle() {
        mBinding.toolbar.setTitle(R.string.app_name);
        setSupportActionBar(mBinding.toolbar);
    }

    private void initDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout,
                mBinding.toolbar, R.string.drawer_open, R.string.drawer_close);
        mBinding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initViewPager() {
        mListTabContentFragment = new ArrayList<>();
        mTabFragmentSong = MainTabSongFragment.newInstance();
        mTabFragmentCollection = MainTabCollectionFragment.newInstance();
        mTabFragmentAlbum = MainTabAlbumFragment.newInstance();

        mListTabContentFragment.add(mTabFragmentSong);
        mListTabContentFragment.add(mTabFragmentCollection);
        mListTabContentFragment.add(mTabFragmentAlbum);

        mDrawerMenuFragment = MainDrawerMenuFragment.newInstance();
        FragmentHelper.show(getActivity(), mBinding.flContainerMenuDrawerMain.getId(), mDrawerMenuFragment, false);

        mViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.setFragments(mListTabContentFragment);
        mViewPagerAdapter.setTitles(new String[]{
//                getString(R.string.tab_main_recommend),
                getString(R.string.tab_main_song),
                getString(R.string.tab_main_collection),
                getString(R.string.tab_main_album)});
        mBinding.vpMain.setOffscreenPageLimit(mListTabContentFragment.size());
        mBinding.vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == INDEX_TAB_SONG) {
                    showFabForSwitchingPage(R.drawable.ic_play_arrow_white);
                    showFabForScrolling();
                } else if (position == INDEX_TAB_COLLECTION) {
                    showFabForSwitchingPage(R.drawable.ic_add_white);
                    showFabForScrolling();
                } else if (position == INDEX_TAB_ALBUM) {
                    hideFabForSwitchingPage();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            ToastHelper.getInstance().showToast(getActivity(), "search");
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

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventMainTabListScroll(MainTabListScrollEvent event) {
        if (event.isIdle()) {
            showFabForScrolling();
        } else {
            hideFabForScrolling();
        }
    }

    private void showFabForScrolling() {
        mBinding.fabMain
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mBinding.fabMain.setVisibility(View.VISIBLE);
                    }
                })
                .start();
    }

    private void hideFabForScrolling() {
        mBinding.fabMain
                .animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationY(mBinding.fabMain.getBottom())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mBinding.fabMain.setVisibility(View.GONE);
                    }
                })
                .start();
    }

    private void showFabForSwitchingPage(@DrawableRes final int fabIcon) {
        final int duration = 400;

        mBinding.fabMain
                .animate()
                .setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator())
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .rotation(mBinding.fabMain.getRotation() + 360)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                        mBinding.fabMain.setVisibility(View.VISIBLE);

                        mBinding.fabMain.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mBinding.fabMain.setImageResource(fabIcon);
                            }
                        }, duration / 2);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
    }

    private void hideFabForSwitchingPage() {
        final int duration = 400;

        mBinding.fabMain
                .animate()
                .setDuration(duration)
                .setInterpolator(new FastOutSlowInInterpolator())
                .scaleX(0.2f)
                .scaleY(0.2f)
                .alpha(0.0f)
                .rotation(-360)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mBinding.fabMain.setVisibility(View.GONE);
                    }
                })
                .start();
    }


    private void showExit() {
        SnackBarHelper.getInstance()
                .showSnackBar(mBinding.fabMain, getString(R.string.tip_exit), getString(R.string.confirm),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MusicPlayController.getInstance().stop(getActivity());
                                finish();
                            }
                        });
    }

}
