package com.divine.yang.main.home;

import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.divine.yang.main.common.FragmentManagerForMain;
import com.divine.yang.main.common.RadioGroupCommon;
import com.divine.yang.base.base.BaseActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;

public class RadioPager2FragmentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "DY-RP2Activity";

    private ArrayList<Fragment> fragments;
    private FragmentManagerForMain fmForMain;
    private RadioGroupCommon rgCommon;

    @Override
    public int getContentViewId() {
        return 0;
        // return R.layout.activity_radio_pager2_fragment;
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
        rgCommon = new RadioGroupCommon(this, rgRadioPager2);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        Lifecycle mLifeCycle = getLifecycle();
        // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
        ViewPager2Adapter mViewPager2Adapter = new ViewPager2Adapter(mFragmentManager, mLifeCycle, fragments);
        vp2RadioPager2.setAdapter(mViewPager2Adapter);
        vp2RadioPager2.setCurrentItem(0);
        // rgCommon.setDrawable(R.id.radio_pager2_radio_button_home);
    }

    private ViewPager2 vp2RadioPager2;
    private RadioGroup rgRadioPager2;

    private void getIntentData() {
        Intent intent = getIntent();
    }

    private void findView() {
        // vp2RadioPager2 = findViewById(R.id.radio_pager2_view_pager2);
        // rgRadioPager2 = findViewById(R.id.radio_pager2_radio_group);
    }

    private void setListener() {
        rgRadioPager2.setOnCheckedChangeListener(this);
        vp2RadioPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                RadioButton rbCheck = (RadioButton) rgRadioPager2.getChildAt(position);
                rbCheck.setChecked(true);
                rgCommon.setDrawable(rbCheck.getId());
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        rgCommon.setDrawable(checkedId);
        // if (checkedId == R.id.radio_pager2_radio_button_home) {
        //     vp2RadioPager2.setCurrentItem(0);
        // } else if (checkedId == R.id.radio_pager2_radio_button_user) {
        //     vp2RadioPager2.setCurrentItem(1);
        // }
    }

    @Override
    public void getData() {

    }

    @Override
    public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {

    }
}