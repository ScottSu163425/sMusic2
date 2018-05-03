package com.scott.su.smusic2.modules.main.song;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.entity.MainTabListScrollEvent;
import com.scott.su.smusic2.databinding.FragmentMainTabSongBinding;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/4/27
 */

public class MainTabSongFragment extends BaseFragment {

    public static MainTabSongFragment newInstance() {
        Bundle args = new Bundle();

        MainTabSongFragment fragment = new MainTabSongFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentMainTabSongBinding mBinding;
    private MainTabSongViewModel mViewModel;
    private MainTabSongListAdapter mSongListAdapter;


    @Override
    protected View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_tab_song, container, false);
        return mBinding.getRoot();
    }

    @Override
    protected void onInit() {
        mSongListAdapter = new MainTabSongListAdapter(getActivity());
        mSongListAdapter.setCallback(new MainTabSongListAdapter.Callback() {
            @Override
            public void onItemClick(View itemView, ImageView cover, LocalSongEntity entity, int position) {
                MusicPlayActivity.start(getActivity(),
                        (ArrayList<LocalSongEntity>) mSongListAdapter.getData(), entity, new View[]{cover});
            }

            @Override
            public void onMoreClick(View itemView, ImageView cover, LocalSongEntity entity, int position) {
                showSnackbar("onMoreClick" + entity.getTitle());
            }
        });


        mBinding.rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mBinding.rv.setAdapter(mSongListAdapter);
        mBinding.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                boolean dragging = RecyclerView.SCROLL_STATE_DRAGGING == newState;
                boolean settling = RecyclerView.SCROLL_STATE_SETTLING == newState;
                boolean idle = RecyclerView.SCROLL_STATE_IDLE == newState;

                postEvent(new MainTabListScrollEvent(dragging, settling, idle));
            }
        });


        mViewModel = ViewModelProviders.of(this).get(MainTabSongViewModel.class);
        mViewModel.getLiveDataSongList().observe(this, new Observer<List<LocalSongEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocalSongEntity> localSongEntities) {
                mSongListAdapter.setData(localSongEntities);
            }
        });

        mViewModel.start();
    }

}
