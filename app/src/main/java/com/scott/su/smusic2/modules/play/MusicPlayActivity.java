package com.scott.su.smusic2.modules.play;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.interfaces.Judgment;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.util.ActivityStarter;
import com.scott.su.common.util.ListUtil;
import com.scott.su.common.util.ViewUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ActivityMusicPlayBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 音乐播放详情
 * 作者: Su
 * 日期: 2018/5/3
 */

public class MusicPlayActivity extends BaseActivity {
    private static final String KEY_EXTRA_SONG_LIST = "KEY_EXTRA_SONG_LIST";
    private static final String KEY_EXTRA_SONG = "KEY_EXTRA_SONG";


    public static void start(Context context, ArrayList<LocalSongEntity> songList,
                             LocalSongEntity currentSong, @Nullable View[] shareElements) {

        Intent intent = getStartIntent(context, songList, currentSong, shareElements);

        if (shareElements == null || shareElements.length == 0) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, shareElements);
    }

    public static Intent getStartIntent(Context context, ArrayList<LocalSongEntity> songList,
                                        LocalSongEntity currentSong, @Nullable View[] shareElements) {
        Intent intent = new Intent(context, MusicPlayActivity.class);
        intent.putExtra(KEY_EXTRA_SONG_LIST, songList);
        intent.putExtra(KEY_EXTRA_SONG, currentSong);

        return intent;
    }

    private List<LocalSongEntity> mSongList;
    private LocalSongEntity mSongPlaying;

    private ActivityMusicPlayBinding mBinding;
    private MusicPlayCoverPageAdapter mCoverPageAdapter;
    private BottomSheetBehavior<CardView> mBehaviorPlayQueue;


    private Animator mAnimatorRevealPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music_play);

        //设置状态栏颜色
        StatusBarUtil.setTranslucentForCoordinatorLayout(getActivity(), 40);

        mSongList = (List<LocalSongEntity>) getIntent().getSerializableExtra(KEY_EXTRA_SONG_LIST);
        mSongPlaying = (LocalSongEntity) getIntent().getSerializableExtra(KEY_EXTRA_SONG);

        ImageLoader.load(getActivity(), mSongPlaying.getAlbumCoverPath(), mBinding.ivCover);

        updatePanelBackgroundColor(mSongPlaying.getAlbumCoverPath());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition()
                    .addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {

                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            mBinding.ivCover.setVisibility(View.GONE);
                        }

                        @Override
                        public void onTransitionCancel(Transition transition) {
                            mBinding.ivCover.setVisibility(View.GONE);
                        }

                        @Override
                        public void onTransitionPause(Transition transition) {

                        }

                        @Override
                        public void onTransitionResume(Transition transition) {

                        }
                    });
        } else {
            mBinding.ivCover.setVisibility(View.GONE);
        }

        mBinding.ivCover.setVisibility(View.GONE);

        mCoverPageAdapter = new MusicPlayCoverPageAdapter(getSupportFragmentManager());

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0, n = mSongList.size(); i < n; i++) {
            LocalSongEntity song = mSongList.get(i);
            MusicPlayCoverFragment fragment = MusicPlayCoverFragment.newInstance(song);
            fragments.add(fragment);
        }

        mCoverPageAdapter.setFragments(fragments);
        mBinding.vpSongCover.setAdapter(mCoverPageAdapter);
        mBinding.vpSongCover.setOffscreenPageLimit(fragments.size());
        mBinding.vpSongCover.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //应该在播放引擎监听回调处更新;
                mSongPlaying = mSongList.get(position);
                stopPanelReveal();

                updatePanelBackgroundColor(mSongPlaying.getAlbumCoverPath());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.vpSongCover.setCurrentItem(ListUtil.getPositionIntList(mSongList, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                return mSongPlaying.getSongId() == obj.getSongId();
            }
        }), false);

        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mBehaviorPlayQueue = BottomSheetBehavior.from(mBinding.layoutMusicPlayQueue);
        mBehaviorPlayQueue.setHideable(false);

        mBinding.viewSpace.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mBehaviorPlayQueue.setPeekHeight(mBinding.viewSpace.getHeight());
                    }
                });

        mBehaviorPlayQueue.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                float factor = slideOffset / bottomSheet.getHeight();
                Log.e("===>onSlide：", String.valueOf(slideOffset));
            }
        });

        mBinding.viewPlayQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendPlayQueue();
            }
        });

        mBinding.fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        if (BottomSheetBehavior.STATE_EXPANDED == mBehaviorPlayQueue.getState()) {
            collapsePlayQueue();
            return;
        }

        super.onBackPressed();
    }

    /**
     * 收起播放列表布局
     */
    private void collapsePlayQueue() {
        mBehaviorPlayQueue.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    /**
     * 展开播放列表布局
     */
    private void expendPlayQueue() {
        mBehaviorPlayQueue.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void updatePanelBackgroundColor(@Nullable String coverPath) {
        if (TextUtils.isEmpty(coverPath)) {
            revealPanelBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            return;
        }

        Palette.from(BitmapFactory.decodeFile(coverPath))
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        int colorDefault = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
                        int color = palette.getMutedColor(colorDefault);

                        revealPanelBackgroundColor(color);
                    }
                });
    }

    /**
     * 更新背景色
     *
     * @param color
     */
    private void revealPanelBackgroundColor(final int color) {
        final View revealStarter = mBinding.fabPlay;
        final View revealView = mBinding.viewBackgroundUpper;


        int centerX = ViewUtil.getXOnScreen(revealStarter) - ViewUtil.getXOnScreen(revealView)
                + (revealStarter.getWidth() / 2);

        int centerY = ViewUtil.getYOnScreen(revealStarter) - ViewUtil.getYOnScreen(revealView)
                - (revealStarter.getHeight() / 2) - ViewUtil.getStatusBarHeight(getActivity());

        revealView.setBackgroundColor(color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAnimatorRevealPanel = ViewAnimationUtils.createCircularReveal(revealView, centerX,
                    centerY, 0, revealView.getWidth());

            mAnimatorRevealPanel.setDuration(1100);
            mAnimatorRevealPanel.setInterpolator(new FastOutSlowInInterpolator());
            mAnimatorRevealPanel.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mBinding.viewBackgroundUnder.setBackgroundColor(color);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                    mBinding.viewBackgroundUnder.setBackgroundColor(color);
                }
            });
            mAnimatorRevealPanel.start();
        }

    }

    private void stopPanelReveal() {
        if (mAnimatorRevealPanel != null && mAnimatorRevealPanel.isRunning()) {
            mAnimatorRevealPanel.cancel();
            mAnimatorRevealPanel = null;
        }
    }

}
