package com.divine.dy.sample;

import android.app.Application;

import com.divine.dy.lib_log.LocalLogcat;

/**
 * Author: Divine
 * CreateDate: 2021/8/2
 * Describe:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
     }
}
