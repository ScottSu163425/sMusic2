package com.scott.su.smusic2.modules.collection.select;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.manager.ToastMaker;
import com.scott.su.common.util.ScreenUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalCollectionEntity;
import com.scott.su.smusic2.databinding.FragmentCollectionSelectBinding;
import com.scott.su.smusic2.modules.common.BaseAppDialogFragment;

import java.util.List;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class CollectionSelectDialogFragment extends BaseAppDialogFragment {

    public static CollectionSelectDialogFragment newInstance() {

        Bundle args = new Bundle();

        CollectionSelectDialogFragment fragment = new CollectionSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private CollectionSelectViewModel mViewModel;
    private FragmentCollectionSelectBinding mBinding;
    private CollectionSelectListAdapter mListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_collection_select, container, false);

        mViewModel = ViewModelProviders.of(this).get(CollectionSelectViewModel.class);
        mViewModel = new CollectionSelectViewModel(getActivity().getApplication());
        mViewModel.getLiveDataCollections()
                .observe(this, new Observer<List<LocalCollectionEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<LocalCollectionEntity> localCollectionEntities) {
                        mListAdapter.setData(localCollectionEntities);
                        adjustHeight();

                        if (localCollectionEntities == null || localCollectionEntities.isEmpty()) {
                            ToastMaker.showToast(getActivity(), getString(R.string.error_empty_collection_to_add));
                            dismissAllowingStateLoss();
                        }
                    }
                });

        mListAdapter = new CollectionSelectListAdapter(getActivity()) {
            @Override
            public void onItemClick(View view, LocalCollectionEntity entity, int position) {
                getCallback().onCollectSelected(entity);
                dismissAllowingStateLoss();
            }
        };

        mBinding.rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mBinding.rv.setAdapter(mListAdapter);
        mViewModel.start();

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void adjustHeight() {
        final int width = (int) (ScreenUtil.getScreenWidth(getActivity()) * 0.8);
        final int heiht1 = ViewGroup.LayoutParams.WRAP_CONTENT;
        final int heiht2 = (int) (ScreenUtil.getScreenHeight(getActivity()) * 0.4);
        final int height = mListAdapter.getCount() > 5 ? heiht2 : heiht1;

        getDialog().getWindow().setLayout(width, height);
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onCollectSelected(LocalCollectionEntity collection) {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onCollectSelected(LocalCollectionEntity collection);
    }

}
