package com.divine.yang.cfas_home;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.divine.yang.base.base.BaseActivity;

@Route(path = "/cfas/main")
public class CfasMainActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_cfas_main;
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

    }
}