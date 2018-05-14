package com.scott.su.smusic2.modules.play;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 描述: 音乐播放服务
 * 作者: su
 * 日期: 2018/5/14
 */

public class MusicPlayService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
