package com.divine.yang.splash;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.splash
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/11/25
 * Description   :
 */
public class Splash {
    public static final String SP_KEY_IS_FIRST_START = "sp_is_first_start";
    public static final String SP_KEY_IS_LOGIN = "sp_is_login";
    public LoadFinishedListener loadFinishedListener;
    private static Splash mSplash;

    private Splash() {
    }

    public static Splash instance() {
        if (null == mSplash) {
            synchronized (Splash.class) {
                if (null == mSplash) {
                    mSplash = new Splash();
                }
            }
        }
        return mSplash;
    }

    public void setLoadFinishedListener(LoadFinishedListener loadFinishedListener) {
        this.loadFinishedListener = loadFinishedListener;
    }
}
