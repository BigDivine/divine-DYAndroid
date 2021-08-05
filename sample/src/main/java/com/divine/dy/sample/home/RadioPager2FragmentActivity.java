package com.divine.dy.sample.home;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.sample.R;
import com.divine.dy.sample.fragments.HomeFragment;
import com.divine.dy.sample.fragments.UserFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;

public class RadioPager2FragmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Override
    public int getContentViewId() {
        return R.layout.activity_radio_pager2_fragment;
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
        RadioPager2ViewPager2.setAdapter(mViewPager2Adapter);

        RadioPager2ViewPager2.setCurrentItem(0);

        RadioPager2RadioGroup.setOnCheckedChangeListener(this);
        RadioPager2ViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                RadioButton rbCheck = (RadioButton) RadioPager2RadioGroup.getChildAt(position);
                rbCheck.setChecked(true);
            }
        });
    }

    private ViewPager2 RadioPager2ViewPager2;
    private RadioGroup RadioPager2RadioGroup;

    private void findView() {
        RadioPager2ViewPager2 = findViewById(R.id.radio_pager2_view_pager2);
        RadioPager2RadioGroup = findViewById(R.id.radio_pager2_radio_group);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_pager2_radio_button_home) {
            RadioPager2ViewPager2.setCurrentItem(0);
        } else if (checkedId == R.id.radio_pager2_radio_button_user) {
            RadioPager2ViewPager2.setCurrentItem(1);
        }
    }
}