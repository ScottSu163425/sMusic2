package com.scott.su.smusic2.modules.collection.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.scott.su.common.activity.BaseActivity;
import com.scott.su.common.manager.ActivityStarter;

/**
 * 描述: 收藏夹详情
 * 作者: Su
 * 日期: 2018/6/22
 */

public class CollectionDetailActivity extends BaseActivity {
    private static final String KEY_EXTRA_COLLECTION_ID = "KEY_EXTRA_COLLECTION_ID";

    public static void start(Context context, String collectionId, @Nullable View[] shareElements) {
        Intent intent = getStartIntent(context, collectionId);

        if (shareElements == null || shareElements.length == 0) {
            context.startActivity(intent);
            return;
        }

        ActivityStarter.startWithSharedElements(context, intent, shareElements);
    }

    public static Intent getStartIntent(Context context, String collectionId) {
        Intent intent = new Intent(context, CollectionDetailActivity.class);
        intent.putExtra(KEY_EXTRA_COLLECTION_ID, collectionId);

        return intent;
    }

    String mCollectionId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCollectionId = getIntent().getStringExtra(KEY_EXTRA_COLLECTION_ID);
    }

}
