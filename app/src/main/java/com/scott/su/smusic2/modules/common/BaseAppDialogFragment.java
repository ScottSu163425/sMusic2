package com.scott.su.smusic2.modules.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.scott.su.common.util.ScreenUtil;
import com.scott.su.smusic2.R;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/23
 */

public class BaseAppDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppDialog);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            final int width = (int) (ScreenUtil.getScreenWidth(getActivity()) * 0.8);
            dialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setWindowAnimations(R.style.animationAppDialog);
        }
    }

    public void show(@NonNull FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), "");
    }

}
