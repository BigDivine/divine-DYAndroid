package com.divine.yang.main.home;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.main.common.FragmentManagerForMain;
import com.divine.yang.main.common.RadioGroupCommon;

import java.util.ArrayList;

public class RadioPagerFragmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "DY-RPActivity";

    private ArrayList<Fragment> fragments;
    private FragmentManagerForMain fmForMain;
    private RadioGroupCommon rgCommon;

    @Override
    public int getContentViewId() {
        return 0;
        // return R.layout.activity_radio_pager_fragment;
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
        rgCommon = new RadioGroupCommon(this, rgRadioPager);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
        vpRadioPager.setAdapter(mViewPagerAdapter);
        vpRadioPager.setCurrentItem(0);
        // rgCommon.setDrawable(R.id.radio_pager_radio_button_home);
    }

    private ViewPager vpRadioPager;
    private RadioGroup rgRadioPager;

    private void getIntentData() {
        Intent intent = getIntent();
    }

    private void findView() {
        // vpRadioPager = findViewById(R.id.radio_pager_view_pager);
        // rgRadioPager = findViewById(R.id.radio_pager_radio_group);
    }

    private void setListener() {
        rgRadioPager.setOnCheckedChangeListener(this);
        vpRadioPager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        rgCommon.setDrawable(checkedId);
        // if (checkedId == R.id.radio_pager_radio_button_home) {
        //     vpRadioPager.setCurrentItem(0);
        // } else if (checkedId == R.id.radio_pager_radio_button_user) {
        //     vpRadioPager.setCurrentItem(1);
        // }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rbCheck = (RadioButton) rgRadioPager.getChildAt(position);
        rbCheck.setChecked(true);
        rgCommon.setDrawable(rbCheck.getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {

    }
}