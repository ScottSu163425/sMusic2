package com.scott.su.smusic2.modules.collection.select;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.common.manager.ToastMaker;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class CollectionSelectFragment extends DialogFragment {

    public static CollectionSelectFragment newInstance() {

        Bundle args = new Bundle();

        CollectionSelectFragment fragment = new CollectionSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void show(@NonNull AppCompatActivity activity) {
        ToastMaker.showToast(activity,"显示收藏夹列表（为空时提示，并关闭弹窗）");
    }

}
