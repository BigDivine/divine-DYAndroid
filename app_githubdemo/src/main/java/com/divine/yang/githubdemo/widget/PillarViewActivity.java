package com.divine.yang.githubdemo.widget;

import com.divine.base.base.BaseActivity;
import com.divine.widget.dychart.PillarBean;
import com.divine.widget.dychart.PillarView;
import com.divine.yang.githubdemo.R;

import java.util.ArrayList;
import java.util.List;

public class PillarViewActivity extends BaseActivity {

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
        PillarView histogramView = findViewById(R.id.pillar);
        List<PillarBean> data3 = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data3.add(new PillarBean(i, (float) (Math.random() * 1.0f)));
        }
        String[] xSplit1 = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        String[] ySplit1 = new String[]{"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        histogramView.setSplit(xSplit1, ySplit1);
        histogramView.setAxisTitle("时期", "利润(万)");
        histogramView.showXYDummyLine(true);
        histogramView.startDraw(data3, 100);
    }
}