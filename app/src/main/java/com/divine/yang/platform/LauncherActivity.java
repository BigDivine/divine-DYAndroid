package com.divine.yang.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.login.Login;
import com.divine.yang.login.LoginActivity;
import com.divine.yang.login.LoginListener;
import com.divine.yang.main.MainActivity;
import com.divine.yang.splash.LoadFinishedListener;
import com.divine.yang.splash.Splash;
import com.divine.yang.splash.SplashActivity;

public class LauncherActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_launcher;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Splash.instance().setLoadFinishedListener(new LoadFinishedListener() {
            @Override
            public void toLogin(Activity fromActivity) {
                startActivity(new Intent(fromActivity, LoginActivity.class));
            }

            @Override
            public void toHome(Activity fromActivity) {
                startActivity(new Intent(fromActivity, MainActivity.class));
            }

            @Override
            public void otherHandle(Activity fromActivity) {
                // startActivity(new Intent(fromActivity, SplashActivity.class));

            }
        });
        Login.instance().setLoginListener(new LoginListener() {
            @Override
            public void toHome(Activity fromActivity) {
                startActivity(new Intent(fromActivity, MainActivity.class));
            }

            @Override
            public void otherHandle(Activity fromActivity) {
                // startActivity(new Intent(fromActivity, SplashActivity.class));

            }
        });
        startActivity(new Intent(this, SplashActivity.class));
        this.finish();
    }

    @Override
    public void getData() {

    }

    @Override
    public String[] requestPermissions() {
        return new String[]{};
    }


}