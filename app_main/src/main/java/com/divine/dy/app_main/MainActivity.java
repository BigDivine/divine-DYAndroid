package com.divine.dy.app_main;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.divine.dy.app_main.home.NavigatorPager2FragmentActivity;
import com.divine.dy.app_main.home.NavigatorPagerFragmentActivity;
import com.divine.dy.app_main.home.RadioPager2FragmentActivity;
import com.divine.dy.app_main.home.RadioPagerFragmentActivity;
import com.divine.dy.app_main.home.TabPager2FragmentActivity;
import com.divine.dy.app_main.home.TabPagerFragmentActivity;
import com.divine.dy.lib_base.arouter.ARouterManager;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseToolbar;
import com.divine.dy.lib_base.getpermission.PermissionList;
import com.divine.dy.lib_log.LocalLogcat;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;

@Route(path = ARouterManager.ROUTER_MAIN_MAIN)
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DY-Main";
    private LocalLogcat mLogcat;
    private int mainViewType, mainMainType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean showToolbar() {
        BaseToolbar toolbar = getBaseToolbar();
        toolbar.setTitle("选择首页类型");
        toolbar.setLeftVisible(false);
        toolbar.setRightVisible(false);
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

    private Spinner sSelectViewType, sSelectMainType;
    private Button btMainSubmit;

    private void findView() {
        ARouter.getInstance().inject(this);
        sSelectViewType = findViewById(R.id.main_select_view_type);
        sSelectMainType = findViewById(R.id.main_select_main_type);
        btMainSubmit = findViewById(R.id.main_submit);
    }

    private void setListener() {
        btMainSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.main_submit) {
            mainViewType = sSelectViewType.getSelectedItemPosition();
            mainMainType = sSelectMainType.getSelectedItemPosition();
            toMainPage();
        }
    }

    private void toMainPage() {
        Intent intent = new Intent();
        SPUtils.getInstance(this).put(SPKeys.SP_KEY_APP_TYPE, mainMainType);
        switch (mainViewType) {
            case 0:
                intent.setClass(this, TabPagerFragmentActivity.class);
                break;
            case 1:
                intent.setClass(this, TabPager2FragmentActivity.class);
                break;
            case 2:
                intent.setClass(this, RadioPagerFragmentActivity.class);
                break;
            case 3:
                intent.setClass(this, RadioPager2FragmentActivity.class);
                break;
            case 4:
                intent.setClass(this, NavigatorPagerFragmentActivity.class);
                break;
            case 5:
                intent.setClass(this, NavigatorPager2FragmentActivity.class);
                break;
        }
        startActivity(intent);

    }
}