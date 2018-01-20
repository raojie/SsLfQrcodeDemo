package com.sslf.qrcode;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Method: MainThreadExecutor
 * Decription:
 * Author: raoj
 * Date: 2017/10/23
 **/
public class MainThreadExecutor implements Executor {

    private Handler handler = new Handler(Looper.getMainLooper());

    private MainThreadExecutor() {
    }

    private static MainThreadExecutor sInstance = null;

    public static MainThreadExecutor getInstance() {
        if (sInstance == null) {
            synchronized (MainThreadExecutor.class) {
                if (sInstance == null)
                    sInstance = new MainThreadExecutor();
            }
        }
        return sInstance;
    }

    @Override
    public void execute(Runnable command) {
        handler.post(command);
    }
}
