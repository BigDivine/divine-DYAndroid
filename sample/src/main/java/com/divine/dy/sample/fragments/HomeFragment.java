package com.divine.dy.sample.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.sample.R;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected void initView(View view) {
        findView(view);
        setListener();
        headerTitle.setText(getTitle());
    }

    @Override
    protected void getData() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public String getTitle() {
        return "首页";
    }

    private TextView headerTitle;
    private ImageButton headerBack;

    private void findView(View view) {
        headerTitle = view.findViewById(R.id.HomeLayoutHeaderTitle);
        headerBack = view.findViewById(R.id.HomeLayoutHeaderBack);
    }

    private void setListener() {
        headerBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.HomeLayoutHeaderBack) {
            getActivity().finish();
        }
    }
}