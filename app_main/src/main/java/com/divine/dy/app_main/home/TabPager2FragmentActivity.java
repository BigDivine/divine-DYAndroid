package com.divine.dy.app_main.home;

import android.content.Intent;
import android.util.Log;

import com.divine.dy.app_main.R;
import com.divine.dy.app_main.common.FragmentManagerForMain;
import com.divine.dy.app_main.common.TabLayoutCommon;
import com.divine.dy.lib_base.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

public class TabPager2FragmentActivity extends BaseActivity {
    private static final String TAG = "DY-TP2Activity";

    private ArrayList<Fragment> fragments;
    private FragmentManagerForMain fmForMain;
    private TabLayoutCommon tabLayoutCommon;

    @Override
    public int getContentViewId() {
        return R.layout.activity_tab_pager2_fragment;
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
        tabLayoutCommon = new TabLayoutCommon(this, tlTabPager2, fragments);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Lifecycle mLifeCycle = getLifecycle();
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPager2Adapter mViewPagerAdapter = new ViewPager2Adapter(mFragmentManager, mLifeCycle, fragments);
        vp2TabPager2.setAdapter(mViewPagerAdapter);

        TabLayoutMediator mTabLayoutMediator = new TabLayoutMediator(tlTabPager2, vp2TabPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.e(TAG, tab.getText() + ":position:" + position);
            }
        });
        mTabLayoutMediator.attach();

        vp2TabPager2.setCurrentItem(0);
        tabLayoutCommon.setTabLayout(0);
    }

    private ViewPager2 vp2TabPager2;
    private TabLayout tlTabPager2;

    private void getIntentData() {
        Intent intent = getIntent();
    }
    private void findView() {
        vp2TabPager2 = findViewById(R.id.tab_pager2_view_pager2);
        tlTabPager2 = findViewById(R.id.tab_pager2_tab_layout);
    }

    private void setListener() {
        vp2TabPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayoutCommon.setTabLayout(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }
}