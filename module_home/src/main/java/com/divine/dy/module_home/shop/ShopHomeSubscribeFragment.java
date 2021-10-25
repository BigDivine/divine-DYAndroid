package com.divine.dy.module_home.shop;

import android.view.View;

import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendFunctionAdapter;
import com.divine.dy.module_home.shop.adapter.ShopHomeSubscribeAdapter;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeSubscribeFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        findView(view);
        //已订阅
        ArrayList<ShopHomeRecommendFunctionBean> data = new ArrayList<>();
        ShopHomeRecommendFunctionBean bean = new ShopHomeRecommendFunctionBean();
        bean.setImg("a");
        bean.setName("a");
        bean.setTitle("四字标题");
        for (int i = 0; i < 9; i++) {
            data.add(bean);
        }
        ShopHomeRecommendFunctionBean beanAll = new ShopHomeRecommendFunctionBean();
        beanAll.setImg("a");
        beanAll.setName("a");
        beanAll.setTitle("全部订阅");
        data.add(beanAll);

        rvShopHomeSubscribeFunction.setLayoutManager(new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false));
        rvShopHomeSubscribeFunction.setAdapter(new ShopHomeRecommendFunctionAdapter(getContext(), data));
        ItemDecorationGrid decoration = new ItemDecorationGrid(getContext(), 60, false);
        rvShopHomeSubscribeFunction.addItemDecoration(decoration);

        rvShopHomeSubscribeList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rvShopHomeSubscribeList.setAdapter(new ShopHomeSubscribeAdapter(getContext(), data));
        rvShopHomeSubscribeList.addItemDecoration(decoration);
    }

    @Override
    protected void getData() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_shop_home_subscribe;
    }

    @Override
    public String getTitle() {
        return "订阅";
    }

    @Override
    public int getIconResId() {
        return 0;
    }

    private RecyclerView rvShopHomeSubscribeFunction, rvShopHomeSubscribeList;

    private void findView(View view) {
        rvShopHomeSubscribeFunction = view.findViewById(R.id.shop_home_subscribe_function);
        rvShopHomeSubscribeList = view.findViewById(R.id.shop_home_subscribe_list);
    }
}
