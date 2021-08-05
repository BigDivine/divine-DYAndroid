package com.divine.dy.sample.home;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.sample.R;
import com.divine.dy.sample.fragments.HomeFragment;
import com.divine.dy.sample.fragments.UserFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TabPagerFragmentActivity extends BaseActivity {
    private static final String TAG = "DY-TabPagerFragmentActivity";

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
        findView();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        ArrayList<Fragment> fragments = new ArrayList<>();
        HomeFragment mHomeFragment = new HomeFragment();
        UserFragment mUserFragment = new UserFragment();
        fragments.add(mHomeFragment);
        fragments.add(mUserFragment);
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        TabPagerViewPager.setAdapter(mViewPagerAdapter);

        TabPagerTabLayout.setupWithViewPager(TabPagerViewPager);
        TabPagerViewPager.setCurrentItem(0);

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = TabPagerTabLayout.getTabAt(i);
            BaseFragment fragment = (BaseFragment) fragments.get(i);
            tab.setText(fragment.getTitle());
            tab.setIcon(R.mipmap.ic_launcher);
        }
    }

    private ViewPager TabPagerViewPager;
    private TabLayout TabPagerTabLayout;

    private void findView() {
        TabPagerViewPager = findViewById(R.id.tab_pager_view_pager);
        TabPagerTabLayout = findViewById(R.id.tab_pager_tab_layout);
    }
}