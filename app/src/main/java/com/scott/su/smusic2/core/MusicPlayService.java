package com.scott.su.smusic2.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.scott.su.smusic2.App;
import com.scott.su.smusic2.R;
import com.scott.su.smusic2.data.entity.LocalSongEntity;
import com.scott.su.smusic2.data.source.local.AppConfig;
import com.scott.su.smusic2.modules.main.MainActivity;
import com.scott.su.smusic2.modules.play.MusicPlayActivity;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.NotificationManagerCompat.IMPORTANCE_DEFAULT;
import static android.support.v4.app.NotificationManagerCompat.IMPORTANCE_HIGH;
import static android.support.v4.app.NotificationManagerCompat.IMPORTANCE_LOW;
import static com.scott.su.smusic2.core.MusicPlayConstants.KEY_EXTRA_COMMAND_CODE;

/**
 * 描述: 音乐播放服务
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayService extends Service {
    private static final int ID_NOTIFICATION = 123;

    private MusicPlayCommandReceiver mCommandReceiver;
    private LocalMusicPlayer mMusicPlayer;
    private NotificationManager mNotificationManager;
    private int mRequestCode;
    private NotificationClickReceiver mNotificationClickReceiver;
    private PlayRepeatModeChangedReceiver mRepeatModeChangedReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mMusicPlayer = new LocalMusicPlayer(getApplicationContext());
        mMusicPlayer.setCallback(mMusicPlayCallback);
        updatePlayRepeatMode();
        registerReceivers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceivers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void registerReceivers() {
        mCommandReceiver = new MusicPlayCommandReceiver();
        //由于要在Notification中发送PendingIntent，无法响应本地广播，故使用全局广播；
//        LocalBroadcastManager.getInstance(getContext())
        registerReceiver(mCommandReceiver, new IntentFilter(MusicPlayController.ACTION_MUSIC_PLAY_CONTROL));

        mNotificationClickReceiver = new NotificationClickReceiver();
        registerReceiver(mNotificationClickReceiver, new IntentFilter(NotificationClickReceiver.ACTION));

        mRepeatModeChangedReceiver = new PlayRepeatModeChangedReceiver();
        registerReceiver(mRepeatModeChangedReceiver, new IntentFilter(PlayRepeatModeChangedReceiver.ACTION));
    }

    private void unregisterReceivers() {
        if (mCommandReceiver != null) {
            unregisterReceiver(mCommandReceiver);
        }

        if (mNotificationClickReceiver != null) {
            unregisterReceiver(mNotificationClickReceiver);
        }

        if (mRepeatModeChangedReceiver != null) {
            unregisterReceiver(mRepeatModeChangedReceiver);
        }
    }

    /**
     * 更新状态栏通知
     */
    private void updateNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        Intent intentNotificationClick = new Intent();
        intentNotificationClick.setAction(NotificationClickReceiver.ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ++mRequestCode,
                intentNotificationClick, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setChannelId(getChannelId());//8.0
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_notification_music_play);
        builder.setContent(getNotificationContentView());
//        builder.setCustomBigContentView(generateBigContentRemoteView());
//        builder.setDefaults(NotificationCompat.DEFAULT_ALL); //过滤暂停、切歌时的音效
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(true);

        mNotificationManager.notify(ID_NOTIFICATION, builder.build());
        startForeground(ID_NOTIFICATION, builder.build());
    }

    /**
     * 8.0以后，开启前台服务需要申请ChannelId，否则会抛异常
     * android.app.RemoteServiceException:
     * Bad notification for startForeground: java.lang.RuntimeException: invalid channel for service notification:
     *
     * @return
     */
    private String getChannelId() {
        String channelId = getPackageName();
        String channelName = getString(R.string.app_name);
        NotificationChannel notificationChannel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, IMPORTANCE_LOW);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(false);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        return channelId;
    }

    private RemoteViews getNotificationContentView() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_music_play_normal);
        final LocalSongEntity playingSong = mMusicPlayer.getCurrentPlayingSong();
        final boolean isPlaying = mMusicPlayer.isPlaying();

        remoteViews.setTextViewText(R.id.tv_title, playingSong.getTitle());
        remoteViews.setTextViewText(R.id.tv_artist, playingSong.getArtist());
        remoteViews.setImageViewBitmap(R.id.iv_cover, BitmapFactory.decodeFile(playingSong.getAlbumCoverPath()));
        remoteViews.setImageViewResource(R.id.iv_play_pause, isPlaying ? R.drawable.ic_pause_black : R.drawable.ic_play_arrow_black);

        Intent actionPlayPause = MusicPlayController.getCommandPlayPause();
        Intent actionSkipPrev = MusicPlayController.getCommandSkipPrev();
        Intent actionSkipNext = MusicPlayController.getCommandSkipNext();

        remoteViews.setOnClickPendingIntent(R.id.view_skip_prev, getNotificationControlPendingIntent(actionSkipPrev));
        remoteViews.setOnClickPendingIntent(R.id.view_skip_next, getNotificationControlPendingIntent(actionSkipNext));
        remoteViews.setOnClickPendingIntent(R.id.view_play_pause, getNotificationControlPendingIntent(actionPlayPause));

        return remoteViews;
    }

    private PendingIntent getNotificationControlPendingIntent(Intent intent) {
        return PendingIntent.getBroadcast(getApplicationContext(), ++mRequestCode,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 去除状态栏通知
     */
    private void cancelNotification() {
        mNotificationManager.cancelAll();
        stopForeground(true);
        stopSelf();
    }

    /**
     * 播放指令接收器
     */
    class MusicPlayCommandReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int commandCode = intent.getIntExtra(KEY_EXTRA_COMMAND_CODE,
                    MusicPlayController.COMMAND_CODE_NONE);

            if (commandCode == MusicPlayController.COMMAND_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO) {
                LocalSongEntity currentPlayingSong = mMusicPlayer.getCurrentPlayingSong();
                ArrayList<LocalSongEntity> playQueue = (ArrayList<LocalSongEntity>) mMusicPlayer.getPlayQueue();

                Intent extraData = new Intent();
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, currentPlayingSong);
                extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, playQueue);

                sendCallbackBroadcast(getApplicationContext(),
                        MusicPlayCallbackBus.EVENT_CODE_UPDATE_CURRENT_PLAYING_SONG_INFO, extraData);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_PLAY) {
                LocalSongEntity currentPlayingSong = (LocalSongEntity) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG);
                ArrayList<LocalSongEntity> playQueue
                        = (ArrayList<LocalSongEntity>) intent.getSerializableExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE);

                mMusicPlayer.setPlaySongs(playQueue);
                mMusicPlayer.restart(currentPlayingSong);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_PLAY_PAUSE) {
                mMusicPlayer.playPause();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_PREVIOUS) {
                mMusicPlayer.skipToPrevious();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SKIP_TO_NEXT) {
                mMusicPlayer.skipToNext();
            } else if (commandCode == MusicPlayController.COMMAND_CODE_SEEK) {
                int positionSeekTo = intent.getIntExtra(MusicPlayConstants.KEY_EXTRA_POSITION_SEEK_TO, 0);
                mMusicPlayer.seekTo(positionSeekTo);
            } else if (commandCode == MusicPlayController.COMMAND_CODE_CLOSE) {
                mMusicPlayer.close();
            }

        }

    }

    private MusicPlayCallback mMusicPlayCallback = new MusicPlayCallback() {
        @Override
        public void onStart(LocalSongEntity song, List<LocalSongEntity> playQueue) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_START, extraData);

            updateNotification();
        }

        @Override
        public void onTik(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_TICK, extraData);
        }

        @Override
        public void onPause(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_PAUSE, extraData);

            updateNotification();
        }

        @Override
        public void onResume(LocalSongEntity song, List<LocalSongEntity> playQueue, int position, int duration) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_POSITION_CURRENT_PLAYING, position);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_DURATION_CURRENT_PLAYING, duration);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_RESUME, extraData);

            updateNotification();
        }

        @Override
        public void onComplete(LocalSongEntity song, List<LocalSongEntity> playQueue) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_COMPLETE, extraData);

        }

        @Override
        public void onClose(LocalSongEntity song, List<LocalSongEntity> playQueue) {
            Intent extraData = new Intent();
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_CURRENT_PLAYING_SONG, song);
            extraData.putExtra(MusicPlayConstants.KEY_EXTRA_PLAY_QUEUE, (ArrayList) playQueue);

            sendCallbackBroadcast(getApplicationContext(),
                    MusicPlayCallbackBus.EVENT_CODE_ON_STOP, extraData);

            cancelNotification();
            stopSelf();
        }
    };

    /**
     * 更新循环模式
     */
    private void updatePlayRepeatMode() {
        if (mMusicPlayer == null) {
            return;
        }

        PlayRepeatMode repeatMode = PlayRepeatMode.REPEAT_ALL;
        if (AppConfig.getInstance().isRepeatOne()) {
            repeatMode = PlayRepeatMode.REPEAT_ONE;
        }
        if (AppConfig.getInstance().isRepeatShuffle()) {
            repeatMode = PlayRepeatMode.REPEAT_SHUFFLE;
        }

        mMusicPlayer.setPlayRepeatMode(repeatMode);
    }

    /**
     * 发送音乐播放回调事件广播
     *
     * @param context
     * @param eventCode
     * @param extraData
     */
    private void sendCallbackBroadcast(Context context, int eventCode, @Nullable Intent extraData) {
        if (extraData == null) {
            extraData = new Intent();
        }

        extraData.setAction(MusicPlayCallbackBus.ACTION_MUSIC_PLAY_CALLBACK);
        extraData.putExtra(MusicPlayCallbackBus.KEY_EXTRA_EVENT_CODE, eventCode);

        //发送指令广播
        sendBroadcast(extraData);
    }


    class NotificationClickReceiver extends BroadcastReceiver {
        public static final String ACTION = "NotificationClickReceiver.ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION.equals(intent.getAction())) {

                //若播放详情界面已在栈顶，则不跳转；
                boolean isMusicPlayInTop = ((App) getApplication()).isTopActivity(MusicPlayActivity.class);
                if (isMusicPlayInTop) {
                    return;
                }

                Intent intentMain = MainActivity.getStartIntent(getApplicationContext());
                intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //只跳转到主页
                Intent intentPlayDetail = MusicPlayActivity.getStartIntent(getApplicationContext(),
                        (ArrayList<LocalSongEntity>) mMusicPlayer.getPlayQueue(),
                        mMusicPlayer.getCurrentPlayingSong());
                intentPlayDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentMain.putExtra("intent", intentPlayDetail);

                startActivity(intentMain);
            }
        }
    }

    public class PlayRepeatModeChangedReceiver extends BroadcastReceiver {
        public static final String ACTION = "PlayRepeatModeChangedReceiver.ACTION";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION.equals(intent.getAction())) {
                updatePlayRepeatMode();
            }
        }
    }


}
