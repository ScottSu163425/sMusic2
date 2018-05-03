package com.scott.su.common.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseFragment extends Fragment {
    private static final String DEFAULT_LOADING_TIP = "请稍候..";

    protected View viewRoot;
    private boolean mFirstTimeResume;
    private ProgressDialog mLoadingDialog;


    protected abstract View provideContentView(LayoutInflater inflater, @Nullable ViewGroup container,
                                               @Nullable Bundle savedInstanceState);

    protected abstract void onInit();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstTimeResume = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (viewRoot == null) {
            viewRoot = provideContentView(inflater, container, savedInstanceState);
        }

        return viewRoot;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (mFirstTimeResume) {
//            mFirstTimeResume = false;
//
//            if (subscribeEvents()) {
//                EventBus.getDefault()
//                        .register(this);
//            }
//
//            onInit();
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (subscribeEvents()) {
            EventBus.getDefault()
                    .register(this);
        }

        onInit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscribeEvents()) {
            EventBus.getDefault()
                    .unregister(this);
        }

        if (viewRoot != null) {
            ((ViewGroup) viewRoot).removeAllViews();
        }
    }

    protected String provideDefaultLoadingTips() {
        return DEFAULT_LOADING_TIP;
    }

    protected boolean subscribeEvents() {
        return false;
    }

    protected void postEvent(Object event) {
        EventBus.getDefault()
                .post(event);
    }

    protected View findViewById(@IdRes int id) {
        if (viewRoot == null) {
            return null;
        }
        return viewRoot.findViewById(id);
    }

    protected void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackbar(@NonNull String text) {
        showSnackbar(getView(), text,null,null);
    }

    protected void showSnackbar(@NonNull View parent, @NonNull String text) {
        showSnackbar(parent, text,null,null);
    }

    protected void showSnackbar(@NonNull View parent, @NonNull String text, @Nullable String action,
                                @Nullable View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(parent, text, Snackbar.LENGTH_SHORT);

        if (!TextUtils.isEmpty(action) && actionClickListener != null) {
            snackbar.setAction(action, actionClickListener);
        }

        snackbar.show();
    }

    protected void showLoadingDialog() {
        showLoadingDialog(provideDefaultLoadingTips(), false);
    }

    protected void showLoadingDialog(String msg) {
        showLoadingDialog(msg, false);
    }

    protected void showLoadingDialog(String msg, boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(getActivity());
        }

//        hideLoadingDialog();

        mLoadingDialog.setCancelable(cancelable);

        if (!TextUtils.isEmpty(msg)) {
            mLoadingDialog.setMessage(msg);
        }

        if (!isLoadingDialogShown()) {
            mLoadingDialog.show();
        }

    }

    protected void hideLoadingDialog() {
        if (isLoadingDialogShown()) {
            mLoadingDialog.dismiss();
        }
    }

    protected boolean isLoadingDialogShown() {
        return (mLoadingDialog != null) && (mLoadingDialog.isShowing());
    }

    /**
     * 关闭系统软键盘
     */
    protected void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            IBinder token = getActivity().getWindow().getDecorView().getWindowToken();
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

}

