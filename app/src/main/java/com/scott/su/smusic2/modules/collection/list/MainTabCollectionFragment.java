package com.scott.su.smusic2.modules.collection.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scott.su.common.fragment.BaseFragment;
import com.scott.su.common.manager.AlertDialogHelper;
import com.scott.su.common.manager.PopupMenuHelper;
import com.scott.su.common.manager.ToastMaker;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.data.entity.event.CollectionsNeedRefreshEvent;
import com.scott.su.smusic2.databinding.FragmentMainTabCollectionBinding;
import com.scott.su.smusic2.modules.collection.detail.CollectionDetailActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

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
        mBinding.rv.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        mCollectionListAdapter = new MainTabCollectionListAdapter(getActivity()) {
            @Override
            void onItemClick(View itemView, ImageView cover, LocalCollectionEntity entity, int position) {
                CollectionDetailActivity.start(getContext(), entity.getCollectionId(), new View[]{cover});
            }

            @Override
            void onMoreClick(View view, LocalCollectionEntity entity, int position) {
                popMenu(view, entity);
            }
        };
        mBinding.rv.setAdapter(new ScaleInAnimationAdapter(mCollectionListAdapter));

        mViewModel = ViewModelProviders.of(this).get(MainTabCollectionViewModel.class);
        mViewModel.getLiveDataCollectionList().observe(this, new Observer<List<LocalCollectionEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocalCollectionEntity> localCollectionEntities) {
                mCollectionListAdapter.setData(localCollectionEntities);

                mBinding.layoutEmpty.setVisibility(mCollectionListAdapter.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        mViewModel.getLiveDataCollectionRemoved().observe(this, new Observer<LocalCollectionEntity>() {
            @Override
            public void onChanged(@Nullable LocalCollectionEntity entity) {
                mCollectionListAdapter.removeData(entity);
            }
        });

        mViewModel.start();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEventCollectionsNeedRefreshEvent(CollectionsNeedRefreshEvent event) {
        mViewModel.refreshCollectionList();
    }

    private void popMenu(View anchor, final LocalCollectionEntity entity) {
        PopupMenuHelper.popup(getActivity(), anchor, new int[]{1}, new String[]{getString(R.string.remove_collection)},
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (1 == item.getItemId()) {
                            AlertDialogHelper.show(getActivity(), getString(R.string.tip_remove_collection),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mViewModel.removeCollection(entity);
                                        }
                                    });
                        }
                        return false;
                    }
                });

    }


}
