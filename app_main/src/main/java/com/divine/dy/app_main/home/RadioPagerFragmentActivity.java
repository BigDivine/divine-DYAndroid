package com.divine.dy.app_main.home;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.app_main.R;
import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class RadioPagerFragmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @Override
    public int getContentViewId() {
        return R.layout.activity_radio_pager_fragment;
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
        RadioPagerViewPager.setAdapter(mViewPagerAdapter);

        RadioPagerViewPager.setCurrentItem(0);

        RadioPagerRadioGroup.setOnCheckedChangeListener(this);
        RadioPagerViewPager.addOnPageChangeListener(this);
    }

    private ViewPager RadioPagerViewPager;
    private RadioGroup RadioPagerRadioGroup;

    private void findView() {
        RadioPagerViewPager = findViewById(R.id.radio_pager_view_pager);
        RadioPagerRadioGroup = findViewById(R.id.radio_pager_radio_group);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_pager_radio_button_home) {
            RadioPagerViewPager.setCurrentItem(0);
        } else if (checkedId == R.id.radio_pager_radio_button_user) {
            RadioPagerViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rbCheck = (RadioButton) RadioPagerRadioGroup.getChildAt(position);
        rbCheck.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}