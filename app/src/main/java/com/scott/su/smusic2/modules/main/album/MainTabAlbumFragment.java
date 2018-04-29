package com.scott.su.smusic2.modules.main.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/27
 */

public class MainTabAlbumFragment extends BaseFragment {

    public static MainTabAlbumFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainTabAlbumFragment fragment = new MainTabAlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_recommend, container, false);

    }

    @Override
    protected void onInit() {

    }
}
