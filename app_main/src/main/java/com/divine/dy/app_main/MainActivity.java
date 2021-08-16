package com.divine.dy.app_main;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.divine.dy.app_main.home.NavigatorPager2FragmentActivity;
import com.divine.dy.app_main.home.NavigatorPagerFragmentActivity;
import com.divine.dy.app_main.home.RadioPager2FragmentActivity;
import com.divine.dy.app_main.home.RadioPagerFragmentActivity;
import com.divine.dy.app_main.home.TabPager2FragmentActivity;
import com.divine.dy.app_main.home.TabPagerFragmentActivity;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseToolbar;
import com.divine.dy.lib_base.base.ToolbarClickListener;
import com.divine.dy.lib_base.getpermission.PermissionList;
import com.divine.dy.lib_log.LocalLogcat;

public abstract class MainActivity extends BaseActivity implements View.OnClickListener, ToolbarClickListener {
    private static final String TAG = "DY-Main";
    private LocalLogcat mLogcat;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean showToolbar() {
        BaseToolbar toolbar = getBaseToolbar();
        toolbar.setLeftVisible(false);
        toolbar.setRightVisible(true);
        toolbar.setTitle("DYAndroid");
        toolbar.setRightText("退出登录");
        toolbar.setToolbarClickListener(this);
        return true;
    }

    @Override
    public void initView() {
        mLogcat = LocalLogcat.getInstance(this);
        mLogcat.start();
        findView();
        setListener();
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{
                PermissionList.WRITE_EXTERNAL_STORAGE,
                PermissionList.READ_EXTERNAL_STORAGE
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLogcat.stop();
    }

    private TextView tvTabPagerFragment, tvTabPager2Fragment, tvRadioPagerFragment, tvRadioPager2Fragment, tvNavigatorPagerFragment, tvNavigatorPager2Fragment, tvWaterMarkTextView;

    private void findView() {
        tvTabPagerFragment = findViewById(R.id.TabPagerFragment);
        tvTabPager2Fragment = findViewById(R.id.TabPager2Fragment);
        tvRadioPagerFragment = findViewById(R.id.RadioPagerFragment);
        tvRadioPager2Fragment = findViewById(R.id.RadioPager2Fragment);
        tvNavigatorPagerFragment = findViewById(R.id.NavigatorPagerFragment);
        tvNavigatorPager2Fragment = findViewById(R.id.NavigatorPager2Fragment);
        tvWaterMarkTextView = findViewById(R.id.WaterMarkTextView);
    }

    private void setListener() {
        tvTabPagerFragment.setOnClickListener(this);
        tvTabPager2Fragment.setOnClickListener(this);
        tvRadioPagerFragment.setOnClickListener(this);
        tvRadioPager2Fragment.setOnClickListener(this);
        tvNavigatorPagerFragment.setOnClickListener(this);
        tvNavigatorPager2Fragment.setOnClickListener(this);
        tvWaterMarkTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.TabPagerFragment) {
            startActivity(new Intent(this, TabPagerFragmentActivity.class));
        } else if (viewId == R.id.TabPager2Fragment) {
            startActivity(new Intent(this, TabPager2FragmentActivity.class));
        } else if (viewId == R.id.RadioPagerFragment) {
            startActivity(new Intent(this, RadioPagerFragmentActivity.class));
        } else if (viewId == R.id.RadioPager2Fragment) {
            startActivity(new Intent(this, RadioPager2FragmentActivity.class));
        } else if (viewId == R.id.NavigatorPagerFragment) {
            startActivity(new Intent(this, NavigatorPagerFragmentActivity.class));
        } else if (viewId == R.id.NavigatorPager2Fragment) {
            startActivity(new Intent(this, NavigatorPager2FragmentActivity.class));
        } else if (viewId == R.id.WaterMarkTextView) {
            toWaterMarkTextView();
        }

    }

    public abstract void toWaterMarkTextView();

    @Override
    public void leftClick() {

    }

    @Override
    public void centerClick() {

    }

    @Override
    public void rightClick() {
        //        toLogin();
    }
}