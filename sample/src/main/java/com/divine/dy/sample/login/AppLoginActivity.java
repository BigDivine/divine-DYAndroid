package com.divine.dy.sample.login;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.divine.dy.app_login.LoginActivity;

/**
 * Author: Divine
 * CreateDate: 2021/8/12
 * Describe:
 */
@Route(path = "/app/login")
public class AppLoginActivity extends LoginActivity {
    @Override
    public void toMain() {
        ARouter.getInstance().build("/app/main").navigation(this);
        this.finish();
    }
}
