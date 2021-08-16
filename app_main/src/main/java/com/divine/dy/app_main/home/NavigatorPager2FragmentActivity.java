package com.divine.dy.app_main.home;

import android.view.MenuItem;

import com.divine.dy.app_main.R;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

public class NavigatorPager2FragmentActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    public int getContentViewId() {
        return R.layout.activity_navigator_pager2_fragment;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        findView();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Lifecycle mLifeCycle = getLifecycle();
        ArrayList<Fragment> fragments = new ArrayList<>();
        HomeFragment mHomeFragment = new HomeFragment();
        UserFragment mUserFragment = new UserFragment();
        fragments.add(mHomeFragment);
        fragments.add(mUserFragment);
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPager2Adapter mViewPager2Adapter = new ViewPager2Adapter(mFragmentManager, mLifeCycle, fragments);
        NavigatorPager2ViewPager2.setAdapter(mViewPager2Adapter);

        NavigatorPager2ViewPager2.setCurrentItem(0);
        NavigatorPager2BottomNavigationView.setOnNavigationItemSelectedListener(this);
        NavigatorPager2ViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                MenuItem miCheck = NavigatorPager2BottomNavigationView.getMenu().getItem(position);
                miCheck.setChecked(true);
            }
        });
    }

    private ViewPager2 NavigatorPager2ViewPager2;
    private BottomNavigationView NavigatorPager2BottomNavigationView;

    private void findView() {
        NavigatorPager2ViewPager2 = findViewById(R.id.navigator_pager2_view_pager2);
        NavigatorPager2BottomNavigationView = findViewById(R.id.navigator_pager2_bottom_navigator);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int viewId = item.getItemId();
        if (viewId == R.id.bottom_menu_home) {
            NavigatorPager2ViewPager2.setCurrentItem(0);
        } else if (viewId == R.id.bottom_menu_user) {
            NavigatorPager2ViewPager2.setCurrentItem(1);
        }

        return true;
    }
}