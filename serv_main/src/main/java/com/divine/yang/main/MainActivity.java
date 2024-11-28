package com.divine.yang.main;

import com.divine.yang.base.base.BaseActivity;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/11/28
 * Description   :
 */
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
