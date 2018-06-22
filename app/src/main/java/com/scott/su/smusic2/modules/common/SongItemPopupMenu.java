package com.scott.su.smusic2.modules.common;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.scott.su.common.manager.PopupMenuHelper;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.modules.album.AlbumDetailActivity;
import com.scott.su.smusic2.modules.collection.select.CollectionSelectFragment;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/19
 */

public class SongItemPopupMenu {

    private SongItemPopupMenu() {
    }

    public static void show(final AppCompatActivity activity, View anchor, final LocalSongEntity song) {
        PopupMenuHelper.popup(activity, anchor, R.menu.more_song_item, new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_collect) {
                    CollectionSelectFragment.newInstance().show(activity);
                } else if (id == R.id.action_check_song_info) {
                    SongInfoDialogFragment.newInstance(song).show(activity);
                } else if (id == R.id.action_check_album) {
                    AlbumDetailActivity.start(activity, song.getAlbumId(), null);
                }

                return true;
            }
        });

    }
}
