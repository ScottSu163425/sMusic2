package com.scott.su.smusic2.modules.album;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalAlbumEntity;
import com.scott.su.smusic2.databinding.FragmentMainTabAlbumBinding;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;

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


    private FragmentMainTabAlbumBinding mBinding;
    private MainTabAlbumListAdapter mAlbumListAdapter;
    private MainTabAlbumViewModel mViewModel;


    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_tab_album, container, false);
        return mBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mAlbumListAdapter = new MainTabAlbumListAdapter(getActivity());

        mAlbumListAdapter.setCallback(new MainTabAlbumListAdapter.Callback() {
            @Override
            public void onItemClick(View itemView, ImageView cover, LocalAlbumEntity entity, int position) {
                AlbumDetailActivity.start(getActivity(), entity.getAlbumId(), cover);
            }
        });

        mBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.rv.setAdapter(new SlideInRightAnimationAdapter(mAlbumListAdapter));

        mViewModel = ViewModelProviders.of(this).get(MainTabAlbumViewModel.class);
        mViewModel.getLiveDataSongList()
                .observe(this, new Observer<List<LocalAlbumEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<LocalAlbumEntity> localAlbumEntities) {
                        mAlbumListAdapter.setData(localAlbumEntities);
                    }
                });
        mViewModel.start();
    }

}
