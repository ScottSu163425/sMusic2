package com.scott.su.common.manager;

import android.app.Activity;
import android.support.annotation.MenuRes;
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
    private PopupMenuHelper() {

    }


    public static void popup(@NonNull Activity activity, @NonNull View anchor, @NonNull int[] itemIds,
                             @NonNull String[] itemTitles, @NonNull PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(activity, anchor);
        Menu menu = popupMenu.getMenu();

        for (int i = 0, n = itemIds.length; i < n; i++) {
            menu.add(0, itemIds[i], i, itemTitles[i]);
        }

        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.show();
    }


    public static void popup(@NonNull Activity activity, @NonNull View anchor, @MenuRes int menuRes,
                             @NonNull PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(activity, anchor);
        popupMenu.inflate(menuRes);
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.show();
    }

}
