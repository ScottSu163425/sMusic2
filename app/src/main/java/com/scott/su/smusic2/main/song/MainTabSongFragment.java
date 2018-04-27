package com.scott.su.smusic2.main.song;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/27
 */

public class MainTabSongFragment extends BaseFragment {

    public static MainTabSongFragment newInstance() {
        Bundle args = new Bundle();

        MainTabSongFragment fragment = new MainTabSongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_song, container, false);
    }

    @Override
    protected void onInit() {

    }
}
