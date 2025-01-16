package com.divine.yang.cfas_home;

import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.widget.CustomRadioButton;

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

    private FrameLayout cfasMainFrameLayout;
    private RadioGroup cfasMainTabRadioGroup;
    private CustomRadioButton homeCustomRadioButton, approvalCustomRadioButton, billCustomRadioButton, mineCustomRadioButton;

    private void findViews() {
        cfasMainFrameLayout = findViewById(R.id.cfas_main_frame_layout);

        cfasMainTabRadioGroup = findViewById(R.id.cfas_main_tab);

        homeCustomRadioButton = findViewById(R.id.cfas_main_tab_home);
        approvalCustomRadioButton = findViewById(R.id.cfas_main_tab_approval);
        billCustomRadioButton = findViewById(R.id.cfas_main_tab_bill);
        mineCustomRadioButton = findViewById(R.id.cfas_main_tab_mine);
    }
}