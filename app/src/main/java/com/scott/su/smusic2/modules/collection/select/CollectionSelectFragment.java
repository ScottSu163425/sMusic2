package com.scott.su.smusic2.modules.collection.select;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

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

    public void show(@NonNull AppCompatActivity activity) {
        ToastMaker.showToast(activity,"显示收藏夹列表（为空时提示，并关闭弹窗）");
    }

}
