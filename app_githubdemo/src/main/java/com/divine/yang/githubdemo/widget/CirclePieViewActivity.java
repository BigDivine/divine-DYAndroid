package com.divine.yang.githubdemo.widget;

import com.divine.base.base.BaseActivity;
import com.divine.widget.dychart.CirclePieBean;
import com.divine.widget.dychart.CirclePieView;
import com.divine.yang.githubdemo.R;

import java.util.ArrayList;

public class CirclePieViewActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_circle_pie_view;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        CirclePieView circlePieView = findViewById(R.id.line_pie);
        ArrayList<CirclePieBean> data2 = new ArrayList<>();
        data2.add(new CirclePieBean("公司业务活动开销", 2));
        data2.add(new CirclePieBean("信息传媒", 1));
        data2.add(new CirclePieBean("交通运输成本", 2));
        data2.add(new CirclePieBean("报销", 1));
        data2.add(new CirclePieBean("员工薪酬", 3));
        data2.add(new CirclePieBean("其他预算", 1));
        circlePieView.startDraw(data2);
    }
}