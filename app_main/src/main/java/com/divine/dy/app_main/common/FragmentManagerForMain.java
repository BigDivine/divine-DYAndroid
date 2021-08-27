package com.divine.dy.app_main.common;

import android.content.Context;

import com.divine.dy.app_main.fragment.HomeFragment;
import com.divine.dy.app_main.fragment.UserFragment;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.module_home.ShopHomeFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

/**
 * Author: Divine
 * CreateDate: 2021/8/27
 * Describe:
 */
public class FragmentManagerForMain {
    private Context mContext;

    public FragmentManagerForMain(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        Fragment homeFragment = getHomeFragment();
        UserFragment mUserFragment = new UserFragment();
        fragments.add(homeFragment);
        fragments.add(mUserFragment);
        return fragments;
    }

    private Fragment getHomeFragment() {
        int mainType = (int) SPUtils.getInstance(mContext).get(SPKeys.SP_KEY_APP_TYPE, 0);
        switch (mainType) {
            case 0:
                return new ShopHomeFragment();
            default:
                return new HomeFragment();
        }
    }
}
