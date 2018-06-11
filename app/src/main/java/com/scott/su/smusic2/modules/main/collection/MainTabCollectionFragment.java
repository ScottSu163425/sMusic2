package com.scott.su.smusic2.modules.main.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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

public class MainTabCollectionFragment extends BaseFragment {

    public static MainTabCollectionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainTabCollectionFragment fragment = new MainTabCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView mRvCollection;
    private MainTabCollectionListAdapter mCollectionListAdapter;
    
    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_tab_collection, container, false);
    }

    @Override
    protected void onInit() {

    }


}
