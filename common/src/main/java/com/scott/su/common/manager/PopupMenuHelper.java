package com.scott.su.common.manager;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/6
 */

public class PopupMenuHelper {
    private static PopupMenuHelper sInstance;


    public static PopupMenuHelper getInstance() {
        if (sInstance == null) {
            synchronized (PopupMenuHelper.class) {
                if (sInstance == null) {
                    sInstance = new PopupMenuHelper();
                }
            }
        }
        return sInstance;
    }


    private PopupMenuHelper() {

    }


    public void popup(@NonNull Activity activity, @NonNull View anchor, @NonNull int[] itemIds,
                      @NonNull String[] itemTitles, @NonNull PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(activity, anchor);

        Menu menu = popupMenu.getMenu();

        for (int i = 0,n=itemIds.length; i <n ; i++) {
            menu.add(0, itemIds[i], i, itemTitles[i]);
        }

        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.show();
    }

}
