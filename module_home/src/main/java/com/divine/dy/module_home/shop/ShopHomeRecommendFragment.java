package com.divine.dy.module_home.shop;

import android.view.View;

import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendFunctionAdapter;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendWaterfallAdapter;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendWaterfallBean;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class ShopHomeRecommendFragment extends BaseFragment {
    private RecyclerView rvShopHomeRecommendFunction, rvShopHomeRecommendWaterfall;

    @Override
    protected void initView(View view) {
        rvShopHomeRecommendFunction = view.findViewById(R.id.shop_home_recommend_function);
        ArrayList<ShopHomeRecommendFunctionBean> data = new ArrayList<>();
        ShopHomeRecommendFunctionBean bean = new ShopHomeRecommendFunctionBean();
        bean.setImg("a");
        bean.setName("a");
        bean.setTitle("四字标题");
        for (int i = 0; i < 29; i++) {
            data.add(bean);
        }
        rvShopHomeRecommendFunction.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        rvShopHomeRecommendFunction.setAdapter(new ShopHomeRecommendFunctionAdapter(getContext(), data));
        ItemDecorationGrid decoration = new ItemDecorationGrid(getContext());
        decoration.setWidth(60);
        decoration.setShowCellLine(false);
        rvShopHomeRecommendFunction.addItemDecoration(decoration);

        rvShopHomeRecommendWaterfall = view.findViewById(R.id.shop_home_recommend_waterfall);
        ArrayList<ShopHomeRecommendWaterfallBean> waterfallData = new ArrayList<>();
        ShopHomeRecommendWaterfallBean waterfallBean = new ShopHomeRecommendWaterfallBean();
        waterfallBean.setImg("a");
        waterfallBean.setName("a");
        for (int i = 0; i < 29; i++) {
            waterfallData.add(waterfallBean);
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
         rvShopHomeRecommendWaterfall.setLayoutManager(staggeredGridLayoutManager);
//        rvShopHomeRecommendWaterfall.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvShopHomeRecommendWaterfall.setAdapter(new ShopHomeRecommendWaterfallAdapter(getContext(), waterfallData));
        ItemDecorationGrid waterfallDecoration = new ItemDecorationGrid(getContext());
        waterfallDecoration.setWidth(30);
        rvShopHomeRecommendWaterfall.addItemDecoration(waterfallDecoration);
    }

    @Override
    protected void getData() {

    }

    @Override
    public int getContentView() {
        return R.layout.fragment_shop_home_recommend;
    }

    @Override
    public String getTitle() {
        return "推荐";
    }

    @Override
    public int getIconResId() {
        return 0;
    }
}