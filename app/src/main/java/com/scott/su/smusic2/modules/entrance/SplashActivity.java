package com.scott.su.smusic2.modules.entrance;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.entity.PermissionEntity;
import com.scott.su.common.interfaces.PermissionCallback;
import com.scott.su.common.manager.PermissionHelper;
import com.scott.su.common.util.ScreenUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.source.local.AppConfig;
import com.scott.su.smusic2.modules.main.MainActivity;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/25
 */
public class SplashActivity extends BaseActivity {
    protected boolean mAnimating = true;
    private View mViewBackground;
    private TextView mTextViewBrand, mTextViewBrand1, mTextViewBrand2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mViewBackground = findViewById(R.id.layout_background);
        mTextViewBrand = findViewById(R.id.tv_brand);
        mTextViewBrand1 = findViewById(R.id.tv_brand_1);
        mTextViewBrand2 = findViewById(R.id.tv_brand_2);

        mViewBackground.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSplashAnimation();
            }
        }, 300);
    }

    private void startSplashAnimation() {
        startBranIn();
    }

    private void startBranIn() {
        mTextViewBrand1.setTranslationX(-ScreenUtil.getScreenWidth(this) / 3 * 2);
        mTextViewBrand2.setTranslationX(ScreenUtil.getScreenWidth(this) / 3 * 2);
        mTextViewBrand1.setAlpha(0);
        mTextViewBrand2.setAlpha(0);

        mTextViewBrand1.animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationX(0)
                .alpha(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mTextViewBrand1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .start();

        mTextViewBrand2.animate()
                .setDuration(600)
                .setInterpolator(new FastOutSlowInInterpolator())
                .translationX(0)
                .alpha(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mTextViewBrand2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mTextViewBrand1.setVisibility(View.INVISIBLE);
                        mTextViewBrand2.setVisibility(View.INVISIBLE);

                        startBrandPop();
                    }
                })
                .start();
    }

    private void startBrandPop() {
        mTextViewBrand
                .animate()
                .setDuration(400)
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        mTextViewBrand.setVisibility(View.VISIBLE);

                        mTextViewBrand.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startReveal();
                            }
                        }, 200);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        mTextViewBrand
                                .animate()
                                .setDuration(400)
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setInterpolator(new FastOutSlowInInterpolator())
                                .setListener(null)
                                .start();
                    }
                })
                .start();

    }

    private void startReveal() {
        final View revealView = mViewBackground;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            revealView.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    final int centerX = revealView.getMeasuredWidth() / 2;
                    final int centerY = revealView.getMeasuredHeight() / 2;
                    final int radiusStart = 0;

                    Animator reveal = ViewAnimationUtils.createCircularReveal(revealView, centerX,
                            centerY, radiusStart, (float) Math.hypot(revealView.getMeasuredWidth(), revealView.getMeasuredHeight()));

                    reveal.setDuration(900);

                    reveal.setInterpolator(new FastOutSlowInInterpolator());
                    reveal.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            revealView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                            mTextViewBrand.setTextColor(Color.WHITE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnimating = false;

                            afterAnimationFinished();
                        }
                    });
                    reveal.start();
                }
            });
        } else {
            revealView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            mAnimating = false;
            afterAnimationFinished();
        }
    }

    private void afterAnimationFinished() {
        boolean firstTimeLaunch = AppConfig.getInstance().isFirstTimeLaunch();

        if (firstTimeLaunch) {
            // TODO: 2018/6/5
        }

        AppConfig.getInstance().setFirstTimeLaunch(  false);

        requestPermissionsIfNeed();
    }

    private void requestPermissionsIfNeed() {
        //startActivity(MainActivity.getStartIntent(getActivity()));
        final String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,

        };

        PermissionHelper.requestPermissions(getActivity(), permissions, new PermissionCallback() {
            @Override
            public void onPermissionGranted(List<PermissionEntity> permissions, boolean allGranted) {
                launchNextActivity();
            }

            @Override
            public void onPermissionDenied(List<PermissionEntity> permissions, boolean allDenied) {

            }
        });
    }

    private void launchNextActivity() {
        mTextViewBrand.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainActivity.start(getActivity());
                finish();
            }
        }, 600);
    }

    @Override
    public void onBackPressed() {
        //Can not be closed by pressing back button when delaying.

        if (mAnimating) {

        } else {
            super.onBackPressed();
        }
    }


}
