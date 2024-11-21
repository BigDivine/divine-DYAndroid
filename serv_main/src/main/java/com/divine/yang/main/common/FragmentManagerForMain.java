package com.divine.yang.main.common;

import android.content.Context;

import com.divine.yang.main.fragment.HomeFragment;
import com.divine.yang.main.fragment.UserFragment;
import com.divine.yang.source.SPKeys;
import com.divine.yang.util.sys.SPUtils;
import com.divine.yang.home.shop.ShopHomeFragment;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

/**
 * Author: Divine
 * CreateDate: 2021/8/27
 * Describe:
 */
public class FragmentManagerForMain {
    private Context mContext;
    private ArrayList<ShopHomeFragment> data=new ArrayList<>();

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
                return new ShopHomeFragment(data);
            default:
                return new HomeFragment();
        }
    }

    public void setData(ArrayList<ShopHomeFragment> data) {
        this.data = data;
    }
}
