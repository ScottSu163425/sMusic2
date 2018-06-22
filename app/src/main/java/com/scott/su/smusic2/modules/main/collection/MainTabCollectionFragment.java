package com.scott.su.smusic2.modules.main.collection;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.databinding.FragmentMainTabCollectionBinding;

import java.util.List;

/**
 * 描述: 收藏夹列表
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

    private FragmentMainTabCollectionBinding mBinding;
    private MainTabCollectionViewModel mViewModel;
    private MainTabCollectionListAdapter mCollectionListAdapter;

    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_tab_collection, container, false);

        return mBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mViewModel = ViewModelProviders.of(this).get(MainTabCollectionViewModel.class);
        mViewModel.getLiveDataCollectionList()
                .observe(this, new Observer<List<LocalCollectionEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<LocalCollectionEntity> localCollectionEntities) {

                    }
                });

        mCollectionListAdapter=new MainTabCollectionListAdapter(getActivity());
        mViewModel.start();
    }


}
