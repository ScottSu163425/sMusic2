package com.scott.su.common.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class LoadingDialogHelper {
    private static final String DEFAULT_LOADING_TIP = "请稍候..";

    private static LoadingDialogHelper sInstance;


    public static LoadingDialogHelper getInstance(@NonNull Activity activity) {
        if (sInstance == null) {
            synchronized (LoadingDialogHelper.class) {
                if (sInstance == null) {
                    sInstance = new LoadingDialogHelper(activity);
                }
            }
        }
        return sInstance;
    }

    private Activity mActivity;

    private LoadingDialogHelper(@NonNull Activity activity) {
        this.mActivity = activity;
    }

    private ProgressDialog mLoadingDialog;

    public void showLoadingDialog() {
        showLoadingDialog(DEFAULT_LOADING_TIP, false);
    }

    public void showLoadingDialog(CharSequence msg) {
        showLoadingDialog(msg, false);
    }

    public void showLoadingDialog(CharSequence msg, boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(mActivity);
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

    public void hideLoadingDialog() {
        if (isLoadingDialogShown()) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean isLoadingDialogShown() {
        return (mLoadingDialog != null) && (mLoadingDialog.isShowing());
    }

}
