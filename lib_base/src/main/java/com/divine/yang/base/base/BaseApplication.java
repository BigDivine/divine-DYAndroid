package com.divine.yang.base.base;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.divine.yang.base.CrashHandler;
import com.divine.yang.base.LocalLogcat;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.base.base
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/11/21
 * Description   :
 */

public abstract class BaseApplication extends Application {
    public static boolean isDebug = false;
    protected abstract void getMetaData();

    @Override
    public void onCreate() {
        super.onCreate();
        getMetaData();
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }
    protected String getApplicationMetaDataString(String key) {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    protected boolean getApplicationMetaDataBoolean(String key) {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData.getBoolean(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
