package com.scott.su.smusic2.modules.play;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.SeekBar;

import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.interfaces.Judgment;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.util.ActivityStarter;
import com.scott.su.common.util.ListUtil;
import com.scott.su.common.util.TimeUtil;
import com.scott.su.common.util.ViewUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.core.MusicPlayCallback;
import com.scott.su.smusic2.core.MusicPlayCallbackBus;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.databinding.ActivityMusicPlayBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 音乐播放详情界面
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
    private LocalSongEntity mSongPlayingInit;

    private ActivityMusicPlayBinding mBinding;
    private MusicPlayCallback mMusicPlayCallback;
    private MusicPlayCoverPageAdapter mCoverPageAdapter;
    private BottomSheetBehavior<CardView> mBehaviorPlayQueue;
    private MusicPlayQueueListAdapter mPlayQueueListAdapter;
    private boolean mDraggingSeekBar;


    private Animator mAnimatorRevealPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSongList = (List<LocalSongEntity>) getIntent().getSerializableExtra(KEY_EXTRA_SONG_LIST);
        mSongPlayingInit = (LocalSongEntity) getIntent().getSerializableExtra(KEY_EXTRA_SONG);
        mSongPlaying = mSongPlayingInit;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music_play);

        initTitle();
        initCoverPager();
        initPlayControl();
        initPlayCallback();
        initPlayQueue();

        updateCurrentPlayingSong(mSongPlaying, false);
    }

    /**
     * 初始化标题栏
     */
    private void initTitle() {
        mBinding.toolbar.setTitle("");
        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * 初始化封面相关
     */
    private void initCoverPager() {
        mBinding.ivCover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mBinding.ivCover.setVisibility(View.GONE);
                mBinding.viewMask.setVisibility(View.GONE);
                return false;
            }
        });

        mCoverPageAdapter = new MusicPlayCoverPageAdapter(getSupportFragmentManager());

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0, n = mSongList.size(); i < n; i++) {
            LocalSongEntity song = mSongList.get(i);
            MusicPlayCoverFragment fragment = MusicPlayCoverFragment.newInstance(song);
            fragments.add(fragment);
        }

        mCoverPageAdapter.setFragments(fragments);

        mBinding.vpSongCover.setOffscreenPageLimit(fragments.size());
        mBinding.vpSongCover.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean firstTimeSelect = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //过滤第一次默认回调:updateCurrentPlaying中有调用歌曲列表相关方法，未初始化将抛空指针；
                if (firstTimeSelect) {
                    firstTimeSelect = false;
                    return;
                }

                updateCurrentPlayingSong(mSongList.get(position), false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.vpSongCover.setAdapter(mCoverPageAdapter);
        mBinding.vpSongCover.setCurrentItem(ListUtil.getPositionIntList(mSongList, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                return mSongPlaying.getSongId() == obj.getSongId();
            }
        }), false);

    }

    /**
     * 初始化播放控制相关
     */
    private void initPlayControl() {
        mBinding.fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayController.getInstance().playPause(getActivity());
            }
        });

        mBinding.viewSkipPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/24 根据播放循环模式，生成上一首歌曲，并获取其在播放列表坐标，
                // TODO: 2018/5/24 将ViewPager翻至该页；
//                mBinding.vpSongCover.setCurrentItem(,true);

                MusicPlayController.getInstance().skipToPrevious(getActivity());
            }
        });

        mBinding.viewSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同Skip Prev

                MusicPlayController.getInstance().skipToNext(getActivity());
            }
        });

        mBinding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBinding.tvTimeCurrent.setText(TimeUtil.getMMssFromMills(progress, ":"));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mDraggingSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDraggingSeekBar = false;

                MusicPlayController.getInstance().seekTo(getActivity(), seekBar.getProgress());
            }
        });
    }

    /**
     * 初始化播放列表相关
     */
    private void initPlayQueue() {
        mBehaviorPlayQueue = BottomSheetBehavior.from(mBinding.layoutMusicPlayQueue);
        mBehaviorPlayQueue.setHideable(false);
        mBehaviorPlayQueue.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBinding.ivPlayIcon.setVisibility(View.VISIBLE);
                    mBinding.ivClosePlayQueue.setVisibility(View.GONE);

                    mBinding.cardCurrentPlaying.setCardElevation(0);
                    mBinding.rvPlayQueue.setAlpha(0);
                    mBinding.rvPlayQueue.setTranslationY(0);//布局重叠，可能会影响点击事件；
                    mBinding.viewMaskContent.setVisibility(View.GONE);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                final float factorIn = slideOffset;
                final float factorOut = 1 - slideOffset;

                mBinding.viewMaskContent.setVisibility(View.VISIBLE);
                mBinding.ivPlayIcon.setVisibility(View.VISIBLE);
                mBinding.ivClosePlayQueue.setVisibility(View.VISIBLE);

                mBinding.viewMaskContent.setAlpha(factorIn);
                mBinding.ivPlayIcon.setAlpha(factorOut);
                mBinding.ivPlayIcon.setScaleX(factorOut);
                mBinding.ivPlayIcon.setScaleY(factorOut);

                mBinding.ivClosePlayQueue.setAlpha(factorIn);
                mBinding.ivClosePlayQueue.setScaleX(factorIn);
                mBinding.ivClosePlayQueue.setScaleY(factorIn);

                mBinding.cardCurrentPlaying.setCardElevation(factorIn * 12);
                mBinding.rvPlayQueue.setAlpha(factorIn);
                mBinding.rvPlayQueue.setTranslationY(factorOut * -360);
            }
        });

        mBinding.ivClosePlayQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsePlayQueue();
            }
        });

        mBinding.viewPlayQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendPlayQueue();
            }
        });

        mPlayQueueListAdapter = new MusicPlayQueueListAdapter(getActivity());
        mPlayQueueListAdapter.setCallback(new MusicPlayQueueListAdapter.Callback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position) {
                updateCurrentPlayingSong(entity, false);
            }

            @Override
            public void onMoreClick(View itemView, LocalSongEntity entity, int position) {

            }
        });
        mPlayQueueListAdapter.setData(mSongList);
        mBinding.rvPlayQueue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvPlayQueue.setAdapter(mPlayQueueListAdapter);
    }

    private void initPlayCallback() {
        mMusicPlayCallback = new MusicPlayCallback() {
            @Override
            public void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                updateCurrentPlayingSongInfo(song);

                mBinding.fabPlay.setImageResource(R.drawable.ic_pause_black);
            }

            @Override
            public void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                if (mDraggingSeekBar) {
                    return;
                }

                mBinding.sbProgress.setProgress(position);
                mBinding.tvTimeCurrent.setText(TimeUtil.getMMssFromMills(position, ":"));
                mBinding.tvTimeTotal.setText(TimeUtil.getMMssFromMills(duration, ":"));
            }

            @Override
            public void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                mBinding.fabPlay.setImageResource(R.drawable.ic_play_arrow_black);
            }

            @Override
            public void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                mBinding.fabPlay.setImageResource(R.drawable.ic_pause_black);
            }

            @Override
            public void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {

            }
        };

        MusicPlayCallbackBus.getInstance().registerCallback(mMusicPlayCallback);
    }

    @Override
    public void onBackPressed() {
        if (BottomSheetBehavior.STATE_EXPANDED == mBehaviorPlayQueue.getState()) {
            collapsePlayQueue();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //与最初进入界面播放的歌曲相同，则显示sharedElement；
            boolean isInitSong = mSongPlaying.getSongId() == mSongPlayingInit.getSongId();
            mBinding.ivCover.setVisibility(isInitSong ? View.VISIBLE : View.GONE);
            mBinding.vpSongCover.setVisibility(isInitSong ? View.GONE : View.VISIBLE);
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MusicPlayCallbackBus.getInstance().unregisterCallback(mMusicPlayCallback);
    }

    private void updateCurrentPlayingSong(@NonNull LocalSongEntity currentPlayingSong, boolean playPause) {
        mSongPlaying = currentPlayingSong;

        if (playPause) {
            MusicPlayController.getInstance().playPause(getActivity());
        } else {
            MusicPlayController.getInstance().play(getActivity(), mSongList, mSongPlaying);
        }

        updateCurrentPlayingSongInfo(currentPlayingSong);
    }

    /**
     * 更新当前播放歌曲信息
     *
     * @param currentPlayingSong
     */
    private void updateCurrentPlayingSongInfo(@NonNull final LocalSongEntity currentPlayingSong) {
        ImageLoader.load(getActivity(), currentPlayingSong.getAlbumCoverPath(), mBinding.ivCover);

        int positionCurrentPlaying = ListUtil.getPositionIntList(mSongList, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                return obj.getSongId() == currentPlayingSong.getSongId();
            }
        });

        mBinding.vpSongCover.setCurrentItem(positionCurrentPlaying, false);
        mPlayQueueListAdapter.setSingleSelectedPosition(positionCurrentPlaying);

        mBinding.tvTitle.setText(currentPlayingSong.getTitle());
        mBinding.tvArtist.setText(currentPlayingSong.getArtist());
        mBinding.tvTimeCurrent.setText(TimeUtil.getHhmmssFromMills(0, null));
        mBinding.tvTimeTotal.setText(TimeUtil.getHhmmssFromMills(currentPlayingSong.getDuration(), null));

        mBinding.sbProgress.setMax((int) currentPlayingSong.getDuration());
        mBinding.sbProgress.setProgress(0);

        TransitionManager.beginDelayedTransition(mBinding.cardCurrentPlaying);

        updatePanelBackgroundColorByCover(currentPlayingSong.getAlbumCoverPath());
    }

    private void togglePlayQueue() {
        if (BottomSheetBehavior.STATE_EXPANDED == mBehaviorPlayQueue.getState()) {
            collapsePlayQueue();
        } else if (BottomSheetBehavior.STATE_COLLAPSED == mBehaviorPlayQueue.getState()) {
            expendPlayQueue();
        }
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

    /**
     * 根据歌曲（专辑）封面，修改面板背景色
     *
     * @param coverPath
     */
    private void updatePanelBackgroundColorByCover(@Nullable String coverPath) {
//        stopPanelReveal();


        final int colorDefault = ContextCompat.getColor(getActivity(), R.color.default_background_panel_music_play);

        final boolean userDefault = TextUtils.isEmpty(coverPath);
        final Bitmap bitmap = userDefault ? BitmapFactory.decodeResource(getResources(), R.drawable.pic_default_cover_album)
                : BitmapFactory.decodeFile(coverPath);

        Palette.from(bitmap)
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        //获取背景色
                        int color;

                        Palette.Swatch swatch1 = palette.getMutedSwatch();
                        Palette.Swatch swatch2 = palette.getVibrantSwatch();
                        Palette.Swatch swatch3 = palette.getDominantSwatch();

                        color = swatch1 != null ? swatch1.getRgb()
                                : (swatch2 != null ? swatch2.getRgb()
                                : (swatch3 != null ? swatch3.getRgb() : colorDefault));

                        revealPanelBackgroundColor(color);

                        updateStatusBarColor(color);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealStarter.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    final int centerX = ViewUtil.getXOnScreen(revealStarter) - ViewUtil.getXOnScreen(revealView)
                            + (revealStarter.getMeasuredWidth() / 2);

                    final int centerY = ViewUtil.getYOnScreen(revealStarter) - ViewUtil.getYOnScreen(revealView)
                            + (revealStarter.getMeasuredHeight() / 2);

                    final int radiusStart = revealStarter.getMeasuredWidth() / 2;

                    mAnimatorRevealPanel = ViewAnimationUtils.createCircularReveal(revealView, centerX,
                            centerY, radiusStart, (float) Math.hypot(revealView.getMeasuredWidth(), revealView.getMeasuredHeight()));

                    mAnimatorRevealPanel.setDuration(1200);
                    mAnimatorRevealPanel.setInterpolator(new FastOutSlowInInterpolator());
                    mAnimatorRevealPanel.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            revealView.setBackgroundColor(color);
                        }

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
            });
        } else {
            revealView.setBackgroundColor(color);
        }

    }

    private void updateStatusBarColor(int color) {
        StatusBarUtil.setColor(getActivity(), color);
    }

    private void stopPanelReveal() {
        if (mAnimatorRevealPanel != null && mAnimatorRevealPanel.isStarted()) {
            mAnimatorRevealPanel.cancel();
            mAnimatorRevealPanel = null;
        }
    }


}
