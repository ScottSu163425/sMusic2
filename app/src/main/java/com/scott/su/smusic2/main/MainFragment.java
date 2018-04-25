package com.scott.su.smusic2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.fragment.BaseTitleFragment;
import com.scott.su.smusic2.R;

/**
 * Description:
 * Author: Su
 * Date: 2018/4/25
 */

public class MainFragment extends BaseTitleFragment {


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    protected void onInit() {
        showTitleTextLeft("Main");
        showToast("onInit: Main");
    }

}
