package com.scott.su.smusic2.modules.collection.create;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ActivityStarter;
import com.scott.su.common.manager.CirclarRevealUtil;
import com.scott.su.common.manager.ToastMaker;
import com.scott.su.common.util.KeyboardUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.ActivityCollectionCreateBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * 描述: 歌曲收藏夹创建界面
 * 作者: su
 * 日期: 2018/6/14
 */

public class CollectionCreateActivity extends BaseActivity {

    public static void start(@NonNull Activity context, @NonNull View fab) {
        ActivityStarter.startWithSharedElements(context, getStartIntent(context), new View[]{fab});
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, CollectionCreateActivity.class);
        return intent;
    }

    private CollectionCreateViewModel mViewModel;
    private ActivityCollectionCreateBinding mBinding;


    private boolean mExit;
    private boolean mEnter = true;//过滤Activity销毁时，共享元素动画结束回调
    private boolean mAnimatingIn;//防止多次点击
    private boolean mAnimatingOut;//防止多次点击

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpAnimation();

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collection_create);
        mBinding.viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCollection();
            }
        });

        mViewModel = ViewModelProviders.of(this)
                .get(CollectionCreateViewModel.class);

        mViewModel.getLiveDataCreateCollectionFailMessage()
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        ToastMaker.showToast(getApplicationContext(), s);
                    }
                });

        mViewModel.getLiveDataCreateCollectionSuccess()
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean success) {
                        if (success != null && success) {
                            onBackPressed();

                            ToastMaker.showToast(getApplicationContext(),
                                    getString(R.string.success_create_new_collection));

                            //通知列表刷新
                            EventBus.getDefault().post(new CollectionsNeedRefreshEvent());
                        }
                    }
                });

        mViewModel.start();
    }

    @Override
    protected boolean autoTransition() {
        return false;
    }

    private void setUpAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setEnterTransition(new Fade());
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this)
                    .inflateTransition(R.transition.changebounds_with_arcmotion));

            getWindow().getSharedElementEnterTransition()
                    .addListener(new Transition.TransitionListener() {
                        @Override
                        public void onTransitionStart(Transition transition) {

                        }

                        @Override
                        public void onTransitionEnd(Transition transition) {
                            if (mEnter) {
                                revealBodyIn();
                            }
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

        } else {
            mBinding.cardBody.setVisibility(View.VISIBLE);
            mBinding.layoutBody.setVisibility(View.VISIBLE);
            mBinding.fab.setVisibility(View.GONE);
        }
    }

    private void revealBodyIn() {
        CirclarRevealUtil.revealIn(mBinding.cardBody, CirclarRevealUtil.DIRECTION.CENTER, 600,
                new FastOutSlowInInterpolator(), new CirclarRevealUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        mAnimatingIn = true;

                        mBinding.fab.setScaleX(0);
                        mBinding.fab.setScaleY(0);
                    }

                    @Override
                    public void onAnimEnd() {
                        CirclarRevealUtil.revealIn(mBinding.layoutBody,
                                CirclarRevealUtil.DIRECTION.CENTER_TOP, 400,
                                null, new CirclarRevealUtil.SimpleAnimListener() {
                                    @Override
                                    public void onAnimStart() {

                                    }

                                    @Override
                                    public void onAnimEnd() {
                                        mAnimatingIn = false;
                                        mEnter = false;
                                    }
                                });
                    }
                });
    }

    private void revealBodyOut() {
        CirclarRevealUtil.revealOut(mBinding.layoutBody, CirclarRevealUtil.DIRECTION.CENTER_TOP, 400,
                null, new CirclarRevealUtil.SimpleAnimListener() {
                    @Override
                    public void onAnimStart() {
                        mAnimatingOut = true;
                    }

                    @Override
                    public void onAnimEnd() {
                        CirclarRevealUtil.revealOut(mBinding.cardBody, CirclarRevealUtil.DIRECTION.CENTER, 600,
                                null, new CirclarRevealUtil.SimpleAnimListener() {
                                    @Override
                                    public void onAnimStart() {
                                    }

                                    @Override
                                    public void onAnimEnd() {

                                        mBinding.fab.animate()
                                                .setDuration(400)
                                                .setInterpolator(new OvershootInterpolator())
                                                .scaleX(1.0f)
                                                .scaleY(1.0f)
                                                .setListener(new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        super.onAnimationEnd(animation);

                                                        mAnimatingOut = false;
                                                        exit();
                                                    }
                                                })
                                                .start();

                                    }
                                }, true);
                    }
                }, true);
    }

    private void exit() {
        mExit = true;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mAnimatingIn || mAnimatingOut) {
            return;
        }


        KeyboardUtil.closeKeyboard(getActivity());

        if (mExit) {
            super.onBackPressed();
        } else {
            revealBodyOut();
        }

    }

    private void createNewCollection() {
        String collectionName = mBinding.etCollectionName.getText().toString().trim();

        if (TextUtils.isEmpty(collectionName)) {
            ToastMaker.showToast(getApplicationContext(), getString(R.string.tip_input_collection_name));
            return;
        }

        mViewModel.saveNewCollection(collectionName);
    }


}
