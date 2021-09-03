package com.divine.dy.app_main.home;

import android.content.Intent;

import com.divine.dy.app_main.R;
import com.divine.dy.app_main.common.FragmentManagerForMain;
import com.divine.dy.app_main.common.TabLayoutCommon;
import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;
import com.divine.dy.lib_base.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TabPagerFragmentActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "DY-TPActivity";

    private ArrayList<Fragment> fragments;
    private FragmentManagerForMain fmForMain;
    private TabLayoutCommon tabLayoutCommon;

    @Override
    public int getContentViewId() {
        return R.layout.activity_tab_pager_fragment;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        getIntentData();
        findView();
        setListener();
        fmForMain = new FragmentManagerForMain(this);
        fragments = fmForMain.getFragments();
        tabLayoutCommon = new TabLayoutCommon(this, tlTabPager, fragments);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        vpTabPager.setAdapter(mViewPagerAdapter);
        tlTabPager.setupWithViewPager(vpTabPager);
        vpTabPager.setCurrentItem(0);
        tabLayoutCommon.setTabLayout(0);

    }

    private ViewPager vpTabPager;
    private TabLayout tlTabPager;

    private void getIntentData() {
        Intent intent = getIntent();
    }

    private void findView() {
        vpTabPager = findViewById(R.id.tab_pager_view_pager);
        tlTabPager = findViewById(R.id.tab_pager_tab_layout);
    }

    private void setListener() {
        vpTabPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabLayoutCommon.setTabLayout(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}