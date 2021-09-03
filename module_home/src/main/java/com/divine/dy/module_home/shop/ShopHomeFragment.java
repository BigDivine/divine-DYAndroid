package com.divine.dy.module_home.shop;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_widget.widget.EditTextWithLRIcon;
import com.divine.dy.module_home.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

/**
 * Author: Divine
 * CreateDate: 2021/8/27
 * Describe:
 */
public class ShopHomeFragment extends BaseFragment {
    private SwipeRefreshLayout srlShopHomeSwipeLayout;
    private TabLayout tlShopHomeTop;
    private ImageButton ibtShopHomeScan, ibtShopHomeCamera;
    private Button btShopHomeSearch;
    private EditTextWithLRIcon etShopHomeSearch;
    private ViewPager vpShopHomePager;

    private ArrayList<Fragment> fragments;

    @Override
    protected void initView(View view) {
        srlShopHomeSwipeLayout = view.findViewById(R.id.shop_home_swipe_layout);
        tlShopHomeTop = view.findViewById(R.id.shop_home_top_tab);
        ibtShopHomeScan = view.findViewById(R.id.shop_home_scan);
        ibtShopHomeCamera = view.findViewById(R.id.shop_home_camera);
        btShopHomeSearch = view.findViewById(R.id.shop_home_search);
        etShopHomeSearch = view.findViewById(R.id.shop_home_search_text);
        vpShopHomePager = view.findViewById(R.id.shop_home_top_pager);
        tlShopHomeTop.setupWithViewPager(vpShopHomePager);

        fragments = new ArrayList<>();
        fragments.add(new ShopHomeSubscribeFragment());
        fragments.add(new ShopHomeRecommendFragment());
        ShopHomeAdapter adapter = new ShopHomeAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpShopHomePager.setAdapter(adapter);
        vpShopHomePager.setCurrentItem(1);
        for (int i = 0; i < fragments.size(); i++) {
            TabLayout.Tab tab = tlShopHomeTop.getTabAt(i);
            BaseFragment fragment = (BaseFragment) fragments.get(i);
            tab.setText(fragment.getTitle());
        }
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


    private class ShopHomeAdapter extends FragmentPagerAdapter {

        public ShopHomeAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

}
