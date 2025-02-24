package com.divine.yang.splash;

import android.app.Activity;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.splash
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/11/22
 * Description   :
 */
public interface LoadFinishedListener {
    void toLogin(Activity fromActivity);

    void toHome(Activity fromActivity);

    void otherHandle(Activity fromActivity);
}
