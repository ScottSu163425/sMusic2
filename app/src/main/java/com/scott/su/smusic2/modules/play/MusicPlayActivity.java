package com.scott.su.smusic2.modules.play;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;
import com.jaeger.library.StatusBarUtil;
import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.interfaces.Judgment;
import com.scott.su.common.manager.ActivityStarter;
import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.manager.ToastMaker;
import com.scott.su.common.util.ListUtil;
import com.scott.su.common.util.ScreenUtil;
import com.scott.su.common.util.TimeUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.core.MusicPlayCallback;
import com.scott.su.smusic2.core.MusicPlayCallbackBus;
import com.scott.su.smusic2.core.MusicPlayController;
import com.scott.su.smusic2.core.MusicPlayService;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.ActivityMusicPlayBinding;
import com.scott.su.smusic2.modules.collection.select.CollectionSelectDialogFragment;
import com.scott.su.smusic2.modules.common.CommonSongListAdapter;
import com.scott.su.smusic2.modules.common.SongItemPopupMenu;

import org.greenrobot.eventbus.EventBus;

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

    private static final int DURATION_REVEAL = 1200;


    public static void start(@NonNull Activity context, ArrayList<LocalSongEntity> songList,
                             LocalSongEntity currentSong, @Nullable View[] shareElements) {
        Intent intent = getStartIntent(context, songList, currentSong);

        if (shareElements == null || shareElements.length == 0) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, shareElements);
    }

    public static Intent getStartIntent(Context context, ArrayList<LocalSongEntity> songList,
                                        LocalSongEntity currentSong) {
        Intent intent = new Intent(context, MusicPlayActivity.class);
        intent.putExtra(KEY_EXTRA_SONG_LIST, songList);
        intent.putExtra(KEY_EXTRA_SONG, currentSong);

        return intent;
    }

    private MusicPlayViewModel mViewModel;
    private List<LocalSongEntity> mSongList;
    private LocalSongEntity mSongPlaying;
    private LocalSongEntity mSongPlayingInit;

    private ActivityMusicPlayBinding mBinding;
    private MusicPlayCallback mMusicPlayCallback;
    private MusicPlayCoverPageAdapter mCoverPageAdapter;
    private BottomSheetBehavior<CardView> mBehaviorPlayQueue;
    private CommonSongListAdapter mPlayQueueSongListAdapter;
    private boolean mDraggingSeekBar;
    private boolean mEnterTransitionEnd;

    private Animator mAnimatorRevealPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTransition();

        mSongList = (List<LocalSongEntity>) getIntent().getSerializableExtra(KEY_EXTRA_SONG_LIST);
        mSongPlayingInit = (LocalSongEntity) getIntent().getSerializableExtra(KEY_EXTRA_SONG);
        mSongPlaying = mSongPlayingInit;

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_music_play);

        initTitle();
        initViewModel();
        initCoverPager();
        initPlayControl();
        initPlayCallback();
        initPlayQueue();

        playSong(mSongPlaying);

        mViewModel.start();
    }

    private void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.BOTTOM);
            getWindow().setEnterTransition(slide);
            slide.excludeTarget(android.R.id.statusBarBackground, true);
            slide.excludeTarget(android.R.id.navigationBarBackground, true);

            getWindow().getEnterTransition()
                    .addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {

                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            //修复未点击封面，导致封面IV不消失，VP滚动时动画被覆盖；
                            hideCover();
                            mEnterTransitionEnd = true;
                        }

                        @Override
                        public void onTransitionCancel(Transition transition) {

                        }

                        @Override
                        public void onTransitionPause(Transition transition) {

                        }

                        @Override
                        public void onTransitionResume(Transition transition) {

                        }
                    });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MusicPlayViewModel.class);

        mViewModel.getLiveDataIsRepeatAll()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        showOrHideRepeat(mBinding.ivRepeatAll, aBoolean);
                    }
                });

        mViewModel.getLiveDataIsRepeatOne()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        showOrHideRepeat(mBinding.ivRepeatOne, aBoolean);
                    }
                });

        mViewModel.getLiveDataIsRepeatShuffle()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        showOrHideRepeat(mBinding.ivRepeatShufflel, aBoolean);
                    }
                });

        mViewModel.getLiveDataCollectSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    ToastMaker.showToast(getActivity(), getString(R.string.success_add_into_collection));
                    //更新收藏夹封面
                    EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
                }
            }
        });

        mViewModel.getLiveDataCollectFailMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                ToastMaker.showToast(getActivity(), s);
            }
        });
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
                /*若不添加mask，会在共享动画过程中，VP选中封面和共享元素图像相同，看起来
                * 是2份，而不是1份移动；不事先把VP隐藏是因为在调试中，发现设置VP由GONE到
                * VISIBLE会有一段可感知的延时和闪动；*/
                hideCover();

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
            boolean selectByGesture = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (selectByGesture) {
                    playSong(mSongList.get(position));
                    selectByGesture = false;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    selectByGesture = true;
                }
            }
        });
        mBinding.vpSongCover.setAdapter(mCoverPageAdapter);
        mBinding.vpSongCover.setCurrentItem(ListUtil.getPositionIntList(mSongList, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                return mSongPlaying.getSongId() == obj.getSongId();
            }
        }), true);

    }

    /**
     * 初始化播放控制相关
     */
    private void initPlayControl() {
        setPlayFabImageResource(true);

        mBinding.fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicPlayController.getInstance().playPause(getActivity());
            }
        });

        mBinding.viewSkipPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayController.getInstance().skipToPrevious(getActivity());
            }
        });

        mBinding.viewSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayController.getInstance().skipToNext(getActivity());
            }
        });

        mBinding.sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBinding.tvTimeCurrent.setText(TimeUtil.getMMssFromMills(progress, null));
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

        mBinding.viewRepeatMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.toggleRepeatMode();
                //更新循环模式
                sendBroadcast(new Intent(MusicPlayService.PlayRepeatModeChangedReceiver.ACTION));
            }
        });

    }

    /**
     * 初始化播放列表相关
     */
    private void initPlayQueue() {
        mBinding.layoutCurrentPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayQueue();
            }
        });

        mBehaviorPlayQueue = BottomSheetBehavior.from(mBinding.layoutMusicPlayQueue);
        mBehaviorPlayQueue.setHideable(false);
        mBehaviorPlayQueue.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBinding.rvPlayQueue.setAlpha(0);
                    mBinding.rvPlayQueue.setTranslationY(0);//布局重叠，可能会影响点击事件；
                    mBinding.viewMaskContent.setVisibility(View.GONE);
                    mBinding.layoutMusicPlayQueuePeek.setAlpha(0);
                    mBinding.layoutCurrentPlaying.setCardElevation(0);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                final float factorIn = slideOffset;
                final float factorOut = 1 - slideOffset;

                mBinding.viewMaskContent.setVisibility(View.VISIBLE);
                mBinding.viewMaskContent.setAlpha(factorIn);
                mBinding.layoutCurrentPlaying.setCardElevation(12 * factorIn);

                mBinding.rvPlayQueue.setAlpha(factorIn);
                mBinding.rvPlayQueue.setTranslationY(factorOut * -360);
                mBinding.layoutMusicPlayQueuePeek.setAlpha(factorIn);
            }
        });

        mBinding.spacePeek.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBehaviorPlayQueue.setPeekHeight(mBinding.spacePeek.getMeasuredHeight());
            }
        });

        mBinding.viewPlayQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expendPlayQueue();
            }
        });

        mPlayQueueSongListAdapter = new CommonSongListAdapter(getActivity());
        mPlayQueueSongListAdapter.setCallback(new CommonSongListAdapter.Callback() {
            @Override
            public void onItemClick(View itemView, LocalSongEntity entity, int position) {
                playSong(entity);
            }

            @Override
            public void onMoreClick(View itemView, LocalSongEntity entity, int position) {
                showItemMenu(itemView, entity);
            }
        });
        mPlayQueueSongListAdapter.setData(mSongList);
        mBinding.rvPlayQueue.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rvPlayQueue.setAdapter(mPlayQueueSongListAdapter);

        mBinding.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemMenu(v, mSongPlaying);
            }
        });
    }

    private void initPlayCallback() {
        mMusicPlayCallback = new MusicPlayCallback() {

            @Override
            public void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                updateCurrentPlayingSongInfo(song);
                togglePlayButtonState(true, true);
            }

            @Override
            public void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                if (mDraggingSeekBar) {
                    return;
                }

                mBinding.sbProgress.setProgress(position);
                mBinding.tvTimeCurrent.setText(TimeUtil.getMMssFromMills(position, null));
                mBinding.tvTimeTotal.setText(TimeUtil.getMMssFromMills(duration, null));
            }

            @Override
            public void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                togglePlayButtonState(false, true);
            }

            @Override
            public void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
                togglePlayButtonState(true, true);
            }

            @Override
            public void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {

            }

            @Override
            public void onClose(LocalSongEntity song, List<LocalSongEntity> playQueue) {
                closeActivity();
            }
        };

        MusicPlayCallbackBus.getInstance().registerCallback(mMusicPlayCallback);
    }

    private void closeActivity() {
        mCloseActivity = true;
        onBackPressed();
    }

    private void hideCover() {
        mBinding.ivCover.setVisibility(View.GONE);
        mBinding.viewMask.setVisibility(View.GONE);
    }

    private boolean mCloseActivity;

    @Override
    public void onBackPressed() {
        if (!mCloseActivity && BottomSheetBehavior.STATE_EXPANDED == mBehaviorPlayQueue.getState()) {
            collapsePlayQueue();

            return;
        }

        boolean isInitSong = mSongPlaying.getSongId().equals(mSongPlayingInit.getSongId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //与最初进入界面播放的歌曲相同，则显示sharedElement；
            mBinding.ivCover.setVisibility(isInitSong ? View.VISIBLE : View.GONE);
            mBinding.viewMask.setVisibility(View.GONE);
            mBinding.vpSongCover.setVisibility(isInitSong ? View.GONE : View.VISIBLE);

            finishAfterTransition();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MusicPlayCallbackBus.getInstance().unregisterCallback(mMusicPlayCallback);
    }

    private void togglePlayButtonState(final boolean isPlaying, final boolean anim) {
        if (isPlaying == mPlayFabPlaying) {
            return;
        }

        if (!anim) {
            setPlayFabImageResource(isPlaying);
            return;
        }

        mBinding.fabPlay.animate()
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator())
                .rotation(isPlaying ? mBinding.fabPlay.getRotation() + 360 : -360)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                        mBinding.fabPlay.setRotation(0);

                        mBinding.fabPlay.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setPlayFabImageResource(isPlaying);
                            }
                        }, 200);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mBinding.fabPlay.setRotation(0);
                    }

                })
                .start();
    }

    private boolean mPlayFabPlaying;

    private void setPlayFabImageResource(boolean playing) {
        mBinding.fabPlay.setImageResource(playing ? R.drawable.ic_pause_black
                : R.drawable.ic_play_arrow_black);

        mPlayFabPlaying = playing;
    }

    private void playSong(@NonNull LocalSongEntity currentPlayingSong) {
        mSongPlaying = currentPlayingSong;
        MusicPlayController.getInstance().play(getActivity(), mSongList, mSongPlaying);
        updateCurrentPlayingSongInfo(currentPlayingSong);
    }

    /**
     * 更新当前播放歌曲信息
     *
     * @param currentPlayingSong
     */
    private void updateCurrentPlayingSongInfo(@NonNull final LocalSongEntity currentPlayingSong) {
        mSongPlaying = currentPlayingSong;

        //会默认把ImageView设为VISIBLE?
        ImageLoader.load(getActivity(), currentPlayingSong.getAlbumCoverPath(), mBinding.ivCover);

        if (mEnterTransitionEnd) {
            hideCover();
        }

        int positionCurrentPlaying = ListUtil.getPositionIntList(mSongList, new Judgment<LocalSongEntity>() {
            @Override
            public boolean test(LocalSongEntity obj) {
                return obj.getSongId().equals(currentPlayingSong.getSongId());
            }
        });

        if (positionCurrentPlaying != mBinding.vpSongCover.getCurrentItem()) {
            mBinding.vpSongCover.setCurrentItem(positionCurrentPlaying, true);
        }

        mPlayQueueSongListAdapter.setSingleSelectedPosition(positionCurrentPlaying);

        mBinding.tvTitle.setText(currentPlayingSong.getTitle());
        mBinding.tvArtist.setText(currentPlayingSong.getArtist());
        mBinding.tvTimeCurrent.setText(TimeUtil.getMMssFromMills(0, null));
        mBinding.tvTimeTotal.setText(TimeUtil.getMMssFromMills(currentPlayingSong.getDuration(), null));

        mBinding.sbProgress.setMax((int) currentPlayingSong.getDuration());
        mBinding.sbProgress.setProgress(0);

        TransitionManager.beginDelayedTransition(mBinding.layoutCurrentPlaying);

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
        final int colorDefault = ContextCompat.getColor(getActivity(), R.color.musicPlayDefaultPanelBackgroundColor);

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
            int colorCurrent = Color.TRANSPARENT;

            Drawable background = revealView.getBackground();
            if (background instanceof ColorDrawable) {
                colorCurrent = ((ColorDrawable) background).getColor();
            }

            if (color == colorCurrent) {
                return;
            }

            stopPanelReveal();

            revealStarter.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    final int centerX = ScreenUtil.getXOnScreen(revealStarter) - ScreenUtil.getXOnScreen(revealView)
                            + (revealStarter.getMeasuredWidth() / 2);

                    final int centerY = ScreenUtil.getYOnScreen(revealStarter) - ScreenUtil.getYOnScreen(revealView)
                            + (revealStarter.getMeasuredHeight() / 2);

                    final int radiusStart = revealStarter.getMeasuredWidth() / 2;

                    mAnimatorRevealPanel = ViewAnimationUtils.createCircularReveal(revealView, centerX,
                            centerY, radiusStart, (float) Math.hypot(revealView.getMeasuredWidth(), revealView.getMeasuredHeight()));

                    mAnimatorRevealPanel.setDuration(DURATION_REVEAL);

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

    private void showOrHideRepeat(final View view, final boolean show) {
        view.animate()
                .alpha(show ? 1.0f : 0)
                .scaleX(show ? 1.0f : 0.0f)
                .scaleY(show ? 1.0f : 0.0f)
                .setDuration(400)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);

                        if (show) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        if (!show) {
                            view.setVisibility(View.GONE);
                        }
                    }
                })
                .start();
    }

    private void showItemMenu(View anchor, final LocalSongEntity song) {
        SongItemPopupMenu.show(getActivity(), anchor, song, new CollectionSelectDialogFragment.Callback() {
            @Override
            public void onCollectSelected(LocalCollectionEntity collection) {
                mViewModel.collectSong(collection, song);
            }
        });
    }

}
