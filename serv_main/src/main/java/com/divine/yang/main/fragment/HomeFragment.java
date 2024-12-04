package com.divine.yang.main.fragment;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.main.R;
import com.divine.yang.main.adapter.HomeAdapter;
import com.divine.yang.main.bean.HomeBean;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
 private   ArrayList<HomeBean> homeBeans;

    @Override
    protected void initView(View view) {
        findView(view);
        setListener();
        setHomeRecyclerViewData(0);
    }
    @Override
    protected void getData() {
        homeBeans = new ArrayList<>();
        HomeBean homeBean = new HomeBean();
        homeBean.setImage("");
        homeBean.setTitle("title");
        for (int i = 0; i < 10; i++) {
            homeBeans.add(homeBean);
        }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public String getTitle() {
        return "首页";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.ic_main_home;
    }

    private RecyclerView homeRecyclerView;
    private Button listButton, fallButton, cardButton;

    private void findView(View view) {
        homeRecyclerView = view.findViewById(R.id.home_recycler_view);
        listButton = view.findViewById(R.id.button_recycler_list);
        fallButton = view.findViewById(R.id.button_recycler_fall);
        cardButton = view.findViewById(R.id.button_recycler_card);
    }

    private void setListener() {
        listButton.setOnClickListener(this);
        fallButton.setOnClickListener(this);
        cardButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_recycler_list) {
            setHomeRecyclerViewData(0);
        } else if (view.getId() == R.id.button_recycler_fall) {
            setHomeRecyclerViewData(1);
        } else if (view.getId() == R.id.button_recycler_card) {
            setHomeRecyclerViewData(2);
        }
    }

    private void setHomeRecyclerViewData(int listType) {
        HomeAdapter homeAdapter = new HomeAdapter(getActivity(), listType);
        homeAdapter.setData(homeBeans);
        if (listType == 0) {
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        } else if (listType == 1) {
            homeRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        } else if (listType == 2) {
            homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        homeRecyclerView.setAdapter(homeAdapter);
    }
}