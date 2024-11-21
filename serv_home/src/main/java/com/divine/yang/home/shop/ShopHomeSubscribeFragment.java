package com.divine.yang.home.shop;

import android.util.Log;
import android.view.View;

import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.widget.ItemDecorationGrid;
import com.divine.yang.home.R;
import com.divine.yang.home.shop.adapter.ShopHomeRecommendFunctionAdapter;
import com.divine.yang.home.shop.adapter.ShopHomeSubscribeAdapter;
import com.divine.yang.home.shop.bean.ShopHomeRecommendFunctionBean;
import com.divine.yang.home.shop.bean.ShopHomeSubscribeBean;
import com.divine.yang.home.shop.listener.ShopHomeSubscribeUnfoldListener;

import java.util.ArrayList;
import java.util.Random;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/8/30
 * Describe:
 */
public class ShopHomeSubscribeFragment extends BaseFragment implements ShopHomeSubscribeUnfoldListener {
    @Override
    protected void initView(View view) {
        findView(view);
        //已订阅
        ArrayList<ShopHomeRecommendFunctionBean> data = new ArrayList<>();
        ShopHomeRecommendFunctionBean bean = new ShopHomeRecommendFunctionBean();
//        bean.setImg("a");
//        bean.setName("a");
//        bean.setTitle("四字标题");
//        for (int i = 0; i < 9; i++) {
//            data.add(bean);
//        }
        ShopHomeRecommendFunctionBean beanAll = new ShopHomeRecommendFunctionBean();
//        beanAll.setImg("a");
//        beanAll.setName("a");
//        beanAll.setTitle("全部订阅");
//        data.add(beanAll);

        rvShopHomeSubscribeFunction.setLayoutManager(new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false));
        rvShopHomeSubscribeFunction.setAdapter(new ShopHomeRecommendFunctionAdapter(getContext(), data));
        ItemDecorationGrid decoration = new ItemDecorationGrid(getContext(), 60, false);
        rvShopHomeSubscribeFunction.addItemDecoration(decoration);

        ArrayList<ShopHomeSubscribeBean> data1 = new ArrayList<>();

        for (int j = 1; j < 21; j++) {
            int kMax = new Random().nextInt(9) + 1;
            Log.e("shop-home", kMax + "");
            ShopHomeSubscribeBean subBean = new ShopHomeSubscribeBean();
            subBean.setContent("这是商家发布的信息");
            subBean.setImg("");
            subBean.setSubTitle1("副标题1");
            subBean.setSubTitle2("副标题2");
            subBean.setTitle("商家名称");
            ArrayList<String> contentImg = new ArrayList<>();
            for (int k = 0; k < kMax; k++) {
                contentImg.add("");
            }
            subBean.setContentImg(contentImg);
            data1.add(subBean);
        }

        rvShopHomeSubscribeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mShopHomeSubscribeAdapter = new ShopHomeSubscribeAdapter(getContext(), data1);
        mShopHomeSubscribeAdapter.setItemClickListener(this);
        rvShopHomeSubscribeList.setAdapter(mShopHomeSubscribeAdapter);
        rvShopHomeSubscribeList.addItemDecoration(decoration);
    }

    ShopHomeSubscribeAdapter mShopHomeSubscribeAdapter;

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

    @Override
    public void unfoldClick(int position) {
        mShopHomeSubscribeAdapter.setUnfoldPosition(position);
    }

    private RecyclerView rvShopHomeSubscribeFunction, rvShopHomeSubscribeList;

    private void findView(View view) {
        rvShopHomeSubscribeFunction = view.findViewById(R.id.shop_home_subscribe_function);
        rvShopHomeSubscribeList = view.findViewById(R.id.shop_home_subscribe_list);
    }

}
