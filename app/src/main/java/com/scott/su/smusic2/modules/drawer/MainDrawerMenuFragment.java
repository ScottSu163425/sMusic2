package com.scott.su.smusic2.modules.drawer;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.common.util.ScreenUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.source.local.AppConfig;
import com.scott.su.smusic2.databinding.FragmentMainDrawerMenuBinding;
import com.scott.su.smusic2.modules.about.AboutActivity;
import com.scott.su.smusic2.modules.main.NightModeChangedEvent;

import org.greenrobot.eventbus.EventBus;

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
        View root = mDrawerMenuBinding.getRoot();
        root.setLayoutParams(new ViewGroup.LayoutParams((int) (ScreenUtil.getScreenWidth(getActivity()) * 0.82),
                ViewGroup.LayoutParams.MATCH_PARENT));
        return mDrawerMenuBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mDrawerMenuViewModel = ViewModelProviders.of(this).get(MainDrawerMenuViewModel.class);

        mDrawerMenuBinding.switchNightMode.setChecked(AppConfig.getInstance().isNightModeOn());
        mDrawerMenuBinding.switchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppConfig.getInstance().setNightMode(isChecked);
                EventBus.getDefault().post(new NightModeChangedEvent(isChecked));
            }
        });

        mDrawerMenuBinding.viewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.start(getActivity());
            }
        });
    }

}
