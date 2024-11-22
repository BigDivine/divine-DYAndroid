package com.divine.yang.platform;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.base.base.CustomToolbar;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MainActivity extends BaseActivity {
    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        View actionBar = findViewById(R.id.main_action_bar);
        CustomToolbar toolbar = new CustomToolbar(this, actionBar);
        toolbar.setTitle("这是标题");
   }

    @Override
    public void getData() {

    }

    @Override
    public String[] requestPermissions() {
        return new String[]{};
    }
}