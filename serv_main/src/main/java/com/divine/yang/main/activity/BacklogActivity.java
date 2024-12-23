package com.divine.yang.main.activity;

import android.view.View;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.main.R;

public class BacklogActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_backlog;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void getData() {

    }

    @Override
    public String[] requestPermissions() {
        return new String[0];
    }

    @Override
    public void initView() {
        View backlogRecyclerView = findViewById(R.id.recycler_in_backlog);
    }
}   