package com.divine.dy.module_home.shop;

import android.util.Log;
import android.view.View;

import com.divine.dy.lib_base.AppBase;
import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_http.retrofit2.CustomResponse;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;
import com.divine.dy.lib_http.retrofit2.Retrofit2IModel;
import com.divine.dy.lib_http.retrofit2.RetrofitUtils;
import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.module_home.R;
import com.divine.dy.module_home.shop.adapter.ShopHomeRecommendFunctionAdapter;
import com.divine.dy.module_home.shop.bean.ShopHomeRecommendFunctionBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ShopHomeRecommendFragment extends BaseFragment implements Retrofit2Callback<String> {
    private RecyclerView rvShopHomeRecommendFunction, rvShopHomeRecommendWaterfall, rvShopHomeRecommendEvent;
    private ArrayList<ShopHomeRecommendFunctionBean> data;
    private ShopHomeRecommendFunctionAdapter shopHomeRecommendFunctionAdapter;

    @Override
    protected void initView(View view) {
        data = new ArrayList<>();

        //        {"functionId":"536fa34ca3db43de001cf56886cefdbc",
        //                "functionName":"test1",
        //                "functionTitle":"测试1",
        //                "functionImg":"http://10.33.10.42:8668/upload/img/2022315f555c056cbf84308947fa41d4597c33a.png",
        //                "functionShow":1}],
        //        int linePx = DensityUtils.dp2px(mContext, 10);
        //常用功能
        rvShopHomeRecommendFunction = view.findViewById(R.id.shop_home_recommend_function);
        //        ArrayList<ShopHomeRecommendFunctionBean> data = new ArrayList<>();
        //        ShopHomeRecommendFunctionBean bean = new ShopHomeRecommendFunctionBean();
        //        String img = "https://up.enterdesk.com/edpic_source/94/33/45/94334519ac2cd1b80174af6b3c19baa0.jpg";
        //        bean.setImg(img);
        //        bean.setName("a");
        //        bean.setTitle("四字标题");
        //        for (int i = 0; i < 12; i++) {
        //            data.add(bean);
        //        }
        rvShopHomeRecommendFunction.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        shopHomeRecommendFunctionAdapter = new ShopHomeRecommendFunctionAdapter(getContext(), data);
        rvShopHomeRecommendFunction.setAdapter(shopHomeRecommendFunctionAdapter);
        ItemDecorationGrid decoration = new ItemDecorationGrid(getContext(), 60, false);
        rvShopHomeRecommendFunction.addItemDecoration(decoration);
        //                        ShopHomeRecommendFunctionBean bean = new ShopHomeRecommendFunctionBean();
        //                bean.setFunctionId("49a11ffef54ca6b2cf8f6251a69d7fb2");
        //                bean.setFunctionImg("http://10.33.10.42:8668/upload/img/202231530b79ad8d7084975a34d658a9a472e13.png");
        //                bean.setFunctionName("test");
        //                bean.setFunctionShow(1);
        //                bean.setFunctionTitle("测试");
        //                data.add(bean);
        //        //特殊功能
        //        rvShopHomeRecommendEvent = view.findViewById(R.id.shop_home_recommend_event);
        //        ArrayList<ShopHomeRecommendEventBean> eventData = new ArrayList<>();
        //        ShopHomeRecommendEventBean eventBean = new ShopHomeRecommendEventBean();
        //        eventBean.setImg("a");
        //        eventBean.setName("a");
        //        for (int i = 0; i < 2; i++) {
        //            eventData.add(eventBean);
        //        }
        //        rvShopHomeRecommendEvent.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        //        rvShopHomeRecommendEvent.setAdapter(new ShopHomeRecommendEventAdapter(getContext(), eventData));
        //        ItemDecorationGrid eventDecoration = new ItemDecorationGrid(getContext(), linePx, true);
        //        rvShopHomeRecommendEvent.addItemDecoration(eventDecoration);
        //        //瀑布流数据
        //        rvShopHomeRecommendWaterfall = view.findViewById(R.id.shop_home_recommend_waterfall);
        //        ArrayList<ShopHomeRecommendWaterfallBean> waterfallData = new ArrayList<>();
        //        ShopHomeRecommendWaterfallBean waterfallBean = new ShopHomeRecommendWaterfallBean();
        //        waterfallBean.setImg("a");
        //        waterfallBean.setName("a");
        //        for (int i = 0; i < 29; i++) {
        //            waterfallData.add(waterfallBean);
        //        }
        //        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL);
        //        rvShopHomeRecommendWaterfall.setLayoutManager(staggeredGridLayoutManager);
        //        rvShopHomeRecommendWaterfall.setAdapter(new ShopHomeRecommendWaterfallAdapter(getContext(), waterfallData));
        //        ItemDecorationGrid waterfallDecoration = new ItemDecorationGrid(getContext(), linePx, true);
        //        rvShopHomeRecommendWaterfall.addItemDecoration(waterfallDecoration);


    }

    @Override
    protected void getData() {
        Retrofit2IModel iModel = new Retrofit2IModel();
        String serverUrl = AppBase.serverUrl + "/shop/home/function/query";
        iModel.sendRequestPost(serverUrl, RetrofitUtils.String2RequestBody("{}"), this);
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

    @Override
    public void onSuccess(String response) {
        try {
//            Log.e("RadioGetData", response);
            Gson gson = new Gson();
            Type type = new TypeToken<CustomResponse<ArrayList<ShopHomeRecommendFunctionBean>>>() {
            }.getType();
            CustomResponse<ArrayList<ShopHomeRecommendFunctionBean>> functionResponse = gson.fromJson(response, type);
            if (functionResponse.code == 0) {
                ArrayList<ShopHomeRecommendFunctionBean> data = functionResponse.data;
                shopHomeRecommendFunctionAdapter.setData(data);
                //            shopHomeRecommendFunctionAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("RadioGetData", Objects.requireNonNull(e.getMessage()));

        }

    }

    @Override
    public void onFailed(String msg) {
        Log.e("RadioGetData", msg);

    }
}