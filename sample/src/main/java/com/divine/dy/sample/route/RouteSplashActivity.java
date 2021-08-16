package com.divine.dy.sample.route;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.divine.dy.app_splash.SplashActivity;
import com.divine.dy.app_main.MainActivity;

/**
 * Author: Divine
 * CreateDate: 2021/8/13
 * Describe:
 */
@Route(path = "/app/splash")
public class RouteSplashActivity extends SplashActivity {
    @Override
    public void toLogin() {
        ARouter.getInstance().build("/app/login").navigation(this);
        this.finish();
    }

    @Override
    public void toMain() {
        startActivity(MainActivity.class, null);
        this.finish();
    }
}
