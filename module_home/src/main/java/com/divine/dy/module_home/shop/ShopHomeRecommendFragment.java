package com.divine.dy.module_home.shop;

import android.view.View;

import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_utils.sys.DensityUtils;
import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendEventAdapter;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendFunctionAdapter;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendWaterfallAdapter;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendEventBean;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendWaterfallBean;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public class ShopHomeRecommendFragment extends BaseFragment {
    private RecyclerView rvShopHomeRecommendFunction, rvShopHomeRecommendWaterfall, rvShopHomeRecommendEvent;

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

        int linePx = DensityUtils.dp2px(mContext, 10);
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
        rvShopHomeRecommendWaterfall.setAdapter(new ShopHomeRecommendWaterfallAdapter(getContext(), waterfallData));
        ItemDecorationGrid waterfallDecoration = new ItemDecorationGrid(getContext());
        waterfallDecoration.setWidth(linePx);
        rvShopHomeRecommendWaterfall.addItemDecoration(waterfallDecoration);

        rvShopHomeRecommendEvent = view.findViewById(R.id.shop_home_recommend_event);
        ArrayList<ShopHomeRecommendEventBean> eventData = new ArrayList<>();
        ShopHomeRecommendEventBean eventBean = new ShopHomeRecommendEventBean();
        eventBean.setImg("a");
        eventBean.setName("a");
        for (int i = 0; i < 2; i++) {
            eventData.add(eventBean);
        }
        rvShopHomeRecommendEvent.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvShopHomeRecommendEvent.setAdapter(new ShopHomeRecommendEventAdapter(getContext(), eventData));
        ItemDecorationGrid eventDecoration = new ItemDecorationGrid(getContext());
        eventDecoration.setWidth(linePx);
        rvShopHomeRecommendEvent.addItemDecoration(eventDecoration);
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