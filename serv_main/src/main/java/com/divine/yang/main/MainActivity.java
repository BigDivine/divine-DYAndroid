package com.divine.yang.main;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.main.fragment.HomeFragment;
import com.divine.yang.main.fragment.UserFragment;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/11/28
 * Description   :
 */
public class MainActivity extends BaseActivity {
    // private ViewPager2 mViewPager2;
    // private FrameLayout customFrameLayout;
    private LinearLayout customNavigatorMain, customNavigatorUser;

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
        customNavigatorMain = findViewById(R.id.custom_navigator_main);
        customNavigatorUser = findViewById(R.id.custom_navigator_user);
        // customFrameLayout = findViewById(R.id.custom_frame_layout);

        HomeFragment homeFragment = new HomeFragment();
        UserFragment userFragment = new UserFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.custom_frame_layout, homeFragment);
        fragmentTransaction.commit();
        customNavigatorMain.setOnClickListener(view -> {
            changeFragment(homeFragment);
        });
        customNavigatorUser.setOnClickListener(view -> {
            changeFragment(userFragment);
        });
    }

    private void changeFragment(Fragment toFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.custom_frame_layout, toFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
