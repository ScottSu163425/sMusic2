package com.scott.su.common.manager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.scott.su.common.R;

/**
 * 描述:
 * 作者: Su
 * 日期: 2018/6/22
 */

public class AlertDialogHelper {

    private AlertDialogHelper() {
    }

    public static void show(@NonNull Activity context, @Nullable String message,
                            @Nullable final DialogInterface.OnClickListener listenerPos) {
        show(context, null, message, null, listenerPos, null, null);
    }

    public static void show(@NonNull Activity context, @Nullable String title, @Nullable String message,
                            @Nullable String strPos, @Nullable final DialogInterface.OnClickListener listenerPos,
                            @Nullable String strNeg, @Nullable final DialogInterface.OnClickListener listenerNeg) {
        final String strPosDefault = context.getString(R.string.confirm);
        final String strNegDefault = context.getString(R.string.cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(TextUtils.isEmpty(strPos) ? strPosDefault : strPos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listenerPos != null) {
                            listenerPos.onClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(TextUtils.isEmpty(strNeg) ? strNegDefault : strNeg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listenerNeg != null) {
                            listenerNeg.onClick(dialog, which);
                        }
                    }
                });

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        builder.create()
                .show();
    }
}
