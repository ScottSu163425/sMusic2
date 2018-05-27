package com.scott.su.smusic2.core;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/21
 */

public class MusicPlayProgressTimer {
    private static final int DELAY = 300;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Runnable mRunnable;


    public MusicPlayProgressTimer() {
        mHandlerThread = new HandlerThread("ProgressTimer");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public void start() {
        stop();
        mRunnable = new MyTimerRunnable();
        mHandler.post(mRunnable);
    }

    public void stop() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
    }

    public void release() {
        stop();
        mHandlerThread.quitSafely();
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onTik() {

                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onTik();
    }

    private class MyTimerRunnable implements Runnable {

        @Override
        public void run() {
            getCallback().onTik();
            mHandler.postDelayed(this, DELAY);
        }
    }

}
