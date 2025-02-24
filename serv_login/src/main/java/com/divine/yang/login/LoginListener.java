package com.divine.yang.login;

import android.app.Activity;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.splash
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/11/22
 * Description   :
 */
public interface LoginListener {
    void toHome(Activity fromActivity);

    void otherHandle(Activity fromActivity);
}
