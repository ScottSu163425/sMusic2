package com.scott.su.common.fragment;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment {
    protected View viewRoot;


    protected abstract View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                               @Nullable Bundle savedInstanceState);

    protected abstract void onInit();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (viewRoot == null) {
            viewRoot = provideContentView(inflater, container, savedInstanceState);
        }

        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (viewRoot != null) {
            ((ViewGroup) viewRoot).removeAllViews();
        }
    }

    protected View findViewById(@IdRes int id) {
        if (viewRoot == null) {
            return null;
        }
        return viewRoot.findViewById(id);
    }

}

