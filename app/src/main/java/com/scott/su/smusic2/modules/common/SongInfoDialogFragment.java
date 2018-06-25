package com.scott.su.smusic2.modules.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.util.FileUtil;
import com.scott.su.common.util.TimeUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/19
 */

public class SongInfoDialogFragment extends BaseAppDialogFragment {
    private static final String KEY_EXTRA_SONG = "KEY_EXTRA_SONG";

    public static SongInfoDialogFragment newInstance(@NonNull LocalSongEntity song) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_EXTRA_SONG, song);

        SongInfoDialogFragment fragment = new SongInfoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LocalSongEntity mSongEntity;
    private View mViewRoot;
    private ImageView mImageViewCover;
    private TextView mTextViewTitle, mTextViewArtist, mTextViewAlbum, mTextViewDuration, mTextViewFileSize,
            mTextViewFilePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSongEntity = (LocalSongEntity) getArguments().getSerializable(KEY_EXTRA_SONG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViewRoot == null) {
            mViewRoot = inflater.inflate(R.layout.fragment_song_info, container, false);

            mImageViewCover = mViewRoot.findViewById(R.id.iv_cover);
            mTextViewTitle = mViewRoot.findViewById(R.id.tv_title);
            mTextViewArtist = mViewRoot.findViewById(R.id.tv_artist);
            mTextViewAlbum = mViewRoot.findViewById(R.id.tv_album);
            mTextViewDuration = mViewRoot.findViewById(R.id.tv_duration);
            mTextViewFileSize = mViewRoot.findViewById(R.id.tv_file_size);
            mTextViewFilePath = mViewRoot.findViewById(R.id.tv_file_path);

            mImageViewCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                }
            });

        }

        setUpInfo();

        return mViewRoot;
    }

    private void setUpInfo() {
        if (mSongEntity == null) {
            return;
        }

        ImageLoader.load(getActivity(), mSongEntity.getAlbumCoverPath(), mImageViewCover);

        mTextViewTitle.setText(mSongEntity.getTitle());
        mTextViewArtist.setText(mSongEntity.getArtist());
        mTextViewAlbum.setText(mSongEntity.getAlbum());
        mTextViewDuration.setText(TimeUtil.getMMssFromMills(mSongEntity.getDuration(), null));
        mTextViewFileSize.setText(FileUtil.formatFileSize(mSongEntity.getFileSize()));
        mTextViewFilePath.setText(mSongEntity.getFilePath());
    }


}
