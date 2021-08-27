package com.divine.dy.module_home;

import android.view.View;

import com.divine.dy.lib_base.base.BaseFragment;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * Author: Divine
 * CreateDate: 2021/8/27
 * Describe:
 */
public class ShopHomeFragment extends BaseFragment {
    private SwipeRefreshLayout srlShopHomeSwipeLayout;

    @Override
    protected void initView(View view) {
        srlShopHomeSwipeLayout = view.findViewById(R.id.shop_home_swipe_layout);
    }

    @Override
    protected void getData() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_shop_home;
    }

    @Override
    public String getTitle() {
        return "购物首页";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.ic_main_home;
    }
}
