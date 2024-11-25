package com.divine.yang.platform;

import com.divine.yang.base.base.BaseApplication;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.platform
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/11/21
 * Description   :
 */
public class MainApplication extends BaseApplication {

    @Override
    protected void getMetaData() {
        isDebug = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // if (isDebug) {
        // }
    }

}
