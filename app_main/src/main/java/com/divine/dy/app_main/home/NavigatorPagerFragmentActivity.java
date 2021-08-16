package com.divine.dy.app_main.home;

import android.view.MenuItem;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.app_main.R;
import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class NavigatorPagerFragmentActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    public int getContentViewId() {
        return R.layout.activity_navigator_pager_fragment;
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
        NavigatorPagerViewPager.setAdapter(mViewPagerAdapter);

        NavigatorPagerViewPager.setCurrentItem(0);

        NavigatorPagerBottomNavigationView.setOnNavigationItemSelectedListener(this);
        NavigatorPagerViewPager.addOnPageChangeListener(this);
    }

    private ViewPager NavigatorPagerViewPager;
    private BottomNavigationView NavigatorPagerBottomNavigationView;

    private void findView() {
        NavigatorPagerViewPager = findViewById(R.id.navigator_pager_view_pager);
        NavigatorPagerBottomNavigationView = findViewById(R.id.navigator_pager_bottom_navigator);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem miCheck = NavigatorPagerBottomNavigationView.getMenu().getItem(position);
        miCheck.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int viewId = item.getItemId();
        if (viewId == R.id.bottom_menu_home) {
            NavigatorPagerViewPager.setCurrentItem(0);
        } else if (viewId == R.id.bottom_menu_user) {
            NavigatorPagerViewPager.setCurrentItem(1);
        }

        return true;
    }
}