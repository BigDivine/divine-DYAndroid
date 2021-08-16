package com.divine.dy.sample.route;

import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.divine.dy.app_main.MainActivity;
import com.divine.dy.sample.WaterMarkActivity;

/**
 * Author: Divine
 * CreateDate: 2021/8/13
 * Describe:
 */
@Route(path = "/app/main")
public class RouteMainActivity extends MainActivity {
    @Override
    public void toWaterMarkTextView() {
        startActivity(new Intent(this, WaterMarkActivity.class));
    }
}
