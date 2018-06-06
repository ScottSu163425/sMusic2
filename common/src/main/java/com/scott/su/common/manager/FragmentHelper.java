package com.scott.su.common.manager;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class FragmentHelper {
    public static void show(FragmentActivity activity, @IdRes int containerViewId,
                            Fragment fragment, boolean anim) {
        show(activity.getSupportFragmentManager(), containerViewId, fragment, anim);
    }

    public static void hide(FragmentActivity activity, Fragment fragment, boolean anim) {
        hide(activity.getSupportFragmentManager(), fragment, anim);
    }

    public static void show(FragmentManager fragmentManager, @IdRes int containerViewId,
                            Fragment fragment, boolean anim) {
        if (null == fragment) {
            return;
        }

        if (!fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (anim) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }

            transaction.add(containerViewId, fragment);
            transaction.commitNow();
        } else {
            if (fragment.isHidden()) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                if (anim) {
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                }

                transaction.show(fragment);
                transaction.commitNow();
            }
        }
    }

    public static void hide(FragmentManager fragmentManager, Fragment fragment, boolean anim) {
        if (null == fragment) {
            return;
        }

        if (fragment.isAdded() && !fragment.isHidden()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (anim) {
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            }

            transaction.hide(fragment);
            transaction.commitNow();
        }
    }

    public static void showAndHideFragment(FragmentActivity activity, @IdRes int containerId,
                                           Fragment fragmentToShow, Fragment fragmentToHide,
                                           boolean anim) {
        showAndHideFragment(activity.getSupportFragmentManager(), containerId, fragmentToShow,
                fragmentToHide, anim);
    }

    public static void showAndHideFragment(FragmentManager fragmentManager, @IdRes int containerId,
                                           Fragment fragmentToShow, Fragment fragmentToHide,
                                           boolean anim) {
        if (fragmentToShow == fragmentToHide) {
            return;
        }

        show(fragmentManager, containerId, fragmentToShow, anim);
        hide(fragmentManager, fragmentToHide, anim);
    }

}
