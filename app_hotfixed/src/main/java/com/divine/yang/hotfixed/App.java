package com.divine.yang.hotfixed;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //android热修复演示
        HotFixManager.installFixedDex(base);
    }
}
