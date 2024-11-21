package com.divine.yang.githubdemo.widget;

import com.divine.base.base.BaseActivity;
import com.divine.widget.dychart.BrokenLineBean;
import com.divine.widget.dychart.BrokenLineView;
import com.divine.yang.githubdemo.R;

import java.util.ArrayList;
import java.util.List;

public class BrokenLineViewActivity extends BaseActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_broken_line_view;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        BrokenLineView mBrokenLineView = findViewById(R.id.broken_line);
        List<BrokenLineBean> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(new BrokenLineBean(i, (float) (Math.random() * 1.0f)));
        }
        String[] xSplit = new String[]{"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
        String[] ySplit = new String[]{"0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
        mBrokenLineView.setSplit(xSplit, ySplit);
        mBrokenLineView.setAxisTitle("时期", "利润(万)");
        mBrokenLineView.showXYDummyLine(true);
        mBrokenLineView.startDraw(data, 100);
    }
}