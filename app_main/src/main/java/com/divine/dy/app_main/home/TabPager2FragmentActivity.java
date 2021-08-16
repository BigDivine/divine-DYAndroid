package com.divine.dy.app_main.home;

import android.util.Log;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.app_main.R;
import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

public class TabPager2FragmentActivity extends BaseActivity {
    private static final String TAG = "DY-TabPager2FragmentAct";

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
        findView();
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Lifecycle mLifeCycle = getLifecycle();
        ArrayList<Fragment> fragments = new ArrayList<>();
        HomeFragment mHomeFragment = new HomeFragment();
        UserFragment mUserFragment = new UserFragment();
        fragments.add(mHomeFragment);
        fragments.add(mUserFragment);
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPager2Adapter mViewPagerAdapter = new ViewPager2Adapter(mFragmentManager, mLifeCycle, fragments);
        TabPager2ViewPager2.setAdapter(mViewPagerAdapter);

        TabLayoutMediator mTabLayoutMediator = new TabLayoutMediator(TabPager2TabLayout, TabPager2ViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                Log.e(TAG, tab.getText() + ":position:" + position);
            }
        });
        mTabLayoutMediator.attach();
        TabPager2ViewPager2.setCurrentItem(0);

        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = TabPager2TabLayout.getTabAt(i);
            BaseFragment fragment = (BaseFragment) fragments.get(i);
            tab.setText(fragment.getTitle());
            tab.setIcon(R.mipmap.ic_launcher);
        }
    }

    private ViewPager2 TabPager2ViewPager2;
    private TabLayout TabPager2TabLayout;

    private void findView() {
        TabPager2ViewPager2 = findViewById(R.id.tab_pager2_view_pager2);
        TabPager2TabLayout = findViewById(R.id.tab_pager2_tab_layout);
    }
}