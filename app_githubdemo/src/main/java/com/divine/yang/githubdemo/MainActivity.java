package com.divine.yang.githubdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.base.SecurityCheck;
import com.divine.base.base.BaseActivity;
import com.divine.widget.DialogUtils;
import com.divine.widget.ItemDecorationLine;
import com.divine.yang.githubdemo.main.MainRecyclerAdapter;
import com.divine.yang.githubdemo.widget.BrokenLineViewActivity;
import com.divine.yang.githubdemo.widget.CirclePieViewActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 手机root权限检查
        if (SecurityCheck.isRoot()) {
            DialogUtils.showConfirmDialog(this, "提示", "您的手机处于Root状态，不允许应用APP，请解除Root状态后应用", (dialog, which) -> {
                dialog.dismiss();
                this.finish();
            });
            return;
        }
        // app应用签名校验,通过SHA1来验证
//        if (!SecurityCheck.signCheck(this)) {
//            DialogUtils.showConfirmDialog(this
//                    , "提示"
//                    , "您的应用签名信息验证失败，不允许使用，请下载官方版本使用"
//                    , (dialog, which) -> {
//                        dialog.dismiss();
//                        this.finish();
//                    }
//            );
//            return;
//        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        RecyclerView rv = findViewById(R.id.view_list);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ItemDecorationLine idl = new ItemDecorationLine(this, android.R.color.black, 1);
        data = new ArrayList<>();

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this, data);
        adapter.setItemClickListener(new RecyclerItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String widget = data.get(position);
                widgetPreview(widget);
            }
        });
        rv.setLayoutManager(lm);
        rv.addItemDecoration(idl);
        rv.setAdapter(adapter);
    }

    @Override
    public void getData() {
        data.add("BrokenLineView");
        data.add("CirclePieView");
        data.add("-------------");
        data.add("CircleRelativeLayout");
        data.add("ColorBarView");
        data.add("ColorRandom");
        data.add("ControlScrollViewPager");
        data.add("CustomViewPager");
        data.add("DividerGridItemDecoration");
        data.add("EllipseColorTextView");
        data.add("EngineerOilRecordBrokenLineBean");
        data.add("FilterTextView");
        data.add("FullyLinearLayoutManager");
        data.add("HistogramView");
        data.add("HomeDecoration");
        data.add("ItemDecorationGM");
        data.add("ItemDecorationHome");
        data.add("LinePieView");
        data.add("MapContainer");
        data.add("PagingScrollHelper");
        data.add("PieChartView");
        data.add("PowerBatteryView");
        data.add("PraiseView");
        data.add("RecycleViewDivider");
        data.add("TriangleView");
        data.add("UnderLineLinearLayout");
        data.add("AppLoadingView");
        data.add("CircleImageView");
        data.add("DialogUtils");
        data.add("EditTextWithLRIcon");
        data.add("ItemDecorationGrid");
        data.add("ItemDecorationLine");
        data.add("RandomVerificationCodeView");
        data.add("SignatureView");
        data.add("ToastUtils");
        data.add("WaterDropView");
        data.add("WaterMarkView");
        data.add("WaveView");
    }

    private void widgetPreview(String widget) {
        switch (widget) {
            case "BrokenLineView":
                startActivity(new Intent(this, BrokenLineViewActivity.class));
                break;
            case "CirclePieView":
                startActivity(new Intent(this, CirclePieViewActivity.class));
                break;
            case "CircleRelativeLayout":
                break;
            case "ColorBarView":
                break;
            case "ColorRandom":
                break;
            case "ControlScrollViewPager":
                break;
            case "CustomViewPager":
                break;
            case "DividerGridItemDecoration":
                break;
            case "EllipseColorTextView":
                break;
            case "EngineerOilRecordBrokenLineBean":
                break;
            case "FilterTextView":
                break;
            case "FullyLinearLayoutManager":
                break;
            case "HistogramView":
                break;
            case "HomeDecoration":
                break;
            case "ItemDecorationGM":
                break;
            case "ItemDecorationHome":
                break;
            case "LinePieView":
                break;
            case "MapContainer":
                break;
            case "PagingScrollHelper":
                break;
            case "PieChartView":
                break;
            case "PowerBatteryView":
                break;
            case "PraiseView":
                break;
            case "RecycleViewDivider":
                break;
            case "TriangleView":
                break;
            case "UnderLineLinearLayout":
                break;
            case "AppLoadingView":
                break;
            case "CircleImageView":
                break;
            case "DialogUtils":
                break;
            case "EditTextWithLRIcon":
                break;
            case "ItemDecorationGrid":
                break;
            case "ItemDecorationLine":
                break;
            case "RandomVerificationCodeView":
                break;
            case "SignatureView":
                break;
            case "ToastUtils":
                break;
            case "WaterDropView":
                break;
            case "WaterMarkView":
                break;
            case "WaveView":
                break;
        }
    }
}