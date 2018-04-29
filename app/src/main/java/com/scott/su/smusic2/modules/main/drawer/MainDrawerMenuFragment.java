package com.scott.su.smusic2.modules.main.drawer;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.databinding.FragmentMainDrawerMenuBinding;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/28
 */

public class MainDrawerMenuFragment extends BaseFragment {
    public static MainDrawerMenuFragment newInstance() {
        MainDrawerMenuFragment fragment = new MainDrawerMenuFragment();
        return fragment;
    }


    private FragmentMainDrawerMenuBinding mDrawerMenuBinding;
    private MainDrawerMenuViewModel mDrawerMenuViewModel;


    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDrawerMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_drawer_menu, container, false);
        return mDrawerMenuBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mDrawerMenuViewModel = ViewModelProviders.of(this).get(MainDrawerMenuViewModel.class);


    }

}
