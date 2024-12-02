package com.divine.yang.main.fragment;

import android.view.View;

import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.main.R;

public class UserFragment extends BaseFragment {

    @Override
    protected void initView(View view) {
        findView(view);
        setListener();
    }

    @Override
    protected void getData() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public String getTitle() {
        return "个人中心";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.ic_main_user;
    }


    private void findView(View view) {

    }

    private void setListener() {

    }

}