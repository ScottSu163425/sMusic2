package com.scott.su.smusic2.modules.common;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.common.manager.ImageLoader;
import com.scott.su.common.util.ScreenUtil;
import com.scott.su.common.util.TimeUtil;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/6/19
 */

public class SongInfoDialogFragment extends DialogFragment {
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
    private View mViewClose;
    private ImageView mImageViewCover;
    private TextView mTextViewTitle, mTextViewArtist, mTextViewAlbum, mTextViewDuration, mTextViewFileSize,
            mTextViewFilePath;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppDialog);

        mSongEntity = (LocalSongEntity) getArguments().getSerializable(KEY_EXTRA_SONG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mViewRoot == null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setWindowAnimations(R.style.animation_dialog_song_info);

            mViewRoot = inflater.inflate(R.layout.fragment_song_info, container, false);

            mViewClose = mViewRoot.findViewById(R.id.view_close);
            mImageViewCover = mViewRoot.findViewById(R.id.iv_cover);
            mTextViewTitle = mViewRoot.findViewById(R.id.tv_title);
            mTextViewArtist = mViewRoot.findViewById(R.id.tv_artist);
            mTextViewAlbum = mViewRoot.findViewById(R.id.tv_album);
            mTextViewDuration = mViewRoot.findViewById(R.id.tv_duration);
            mTextViewFileSize = mViewRoot.findViewById(R.id.tv_file_size);
            mTextViewFilePath = mViewRoot.findViewById(R.id.tv_file_path);

            mViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                }
            });

        }

        setUpInfo();

        return mViewRoot;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout((int) (ScreenUtil.getScreenWidth(getActivity()) * 0.9),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public void show(@NonNull FragmentActivity activity) {
        show(activity.getSupportFragmentManager(), "");
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
        mTextViewFileSize.setText(mSongEntity.getFileSize() + "");
        mTextViewFilePath.setText(mSongEntity.getFilePath());
    }


}
