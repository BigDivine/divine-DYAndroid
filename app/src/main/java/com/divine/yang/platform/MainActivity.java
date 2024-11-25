package com.divine.yang.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.login.LoginActivity;
import com.divine.yang.splash.LoadFinishedListener;
import com.divine.yang.splash.Splash;
import com.divine.yang.splash.SplashActivity;

public class MainActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
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
                // startActivity(new Intent(fromActivity, SplashActivity.class));

            }

            @Override
            public void otherHandle(Activity fromActivity) {
                // startActivity(new Intent(fromActivity, SplashActivity.class));

            }
        });
        startActivity(new Intent(this, SplashActivity.class));
    }

    @Override
    public void getData() {

    }

    @Override
    public String[] requestPermissions() {
        return new String[]{};
    }


}