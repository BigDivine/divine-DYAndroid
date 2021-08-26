package com.divine.dy.sample.widget;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.divine.dy.lib_base.arouter.ARouterManager;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_widget.widget.ToastUtils;
import com.divine.dy.lib_widget.widget.WaterMarkView;
import com.divine.dy.sample.R;

@Route(path = ARouterManager.ROUTER_VIEW_WATERMARK)
public class WaterMarkActivity extends BaseActivity {
    private final String TAG = "DY-WMActivity";

    @Override
    public int getContentViewId() {
        return R.layout.activity_water_mark;
    }

    @Override
    public boolean showToolbar() {
        return true;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        tv=findViewById(R.id.tv);
    }

    TextView tv;
    public void textClick(View view) {
        ToastUtils.showShort(this, "text click");
        Log.e(TAG, "textClick");
     }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float toolHeight = getBaseToolbar().getActionBarHeight();
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        float touchPointY = event.getY() - toolHeight - statusBarHeight;
        Log.e(TAG, "toolbar height:" + toolHeight + "px，statusBarHeight:" + statusBarHeight + "px");
        Log.e(TAG, "MotionEvent：(" + event.getX() + "," + touchPointY + ")");
        ViewGroup parent= (ViewGroup) tv.getParent();
        WaterMarkView wmv=new WaterMarkView(this,null);
        wmv.setTouchPoint(new PointF(event.getX(), touchPointY));
        parent.addView(wmv);
        return super.dispatchTouchEvent(event);
    }

}