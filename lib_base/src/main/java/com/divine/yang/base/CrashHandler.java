package com.divine.yang.base;

import android.content.Context;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.base
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/11/21
 * Description   :
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        showToast(t);
    }

    /**
     * 操作
     *
     * @param thread
     */
    private void showToast(Thread thread) {

    }
}