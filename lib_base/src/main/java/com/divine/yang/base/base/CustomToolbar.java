package com.divine.yang.base.base;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.divine.yang.base.R;
import com.divine.yang.util.sys.DensityUtils;

/**
 * Author: Divine
 * CreateDate: 2020/10/23
 * ModifyDate:2024/11/21
 * Describe: 公共标题栏
 */
public class CustomToolbar {
    private final String TAG = "DY-CustomToolbar";
    private Context mContext;
    private View mToolbar;
    private ToolbarClickListener listener;

    private String leftText, title, rightText;
    private int leftTextResId, titleResId, rightTextResId;
    private int leftTextColor, titleColor, rightTextColor;
    private int leftDrawable, rightDrawable;
    private LinearLayout leftLayout, centerLayout, rightLayout;
    View.OnClickListener leftListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != listener)
                listener.leftClick();
        }
    };
    View.OnClickListener centerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != listener)
                listener.centerClick();
        }
    };
    View.OnClickListener rightListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != listener)
                listener.rightClick();
        }
    };

    public CustomToolbar(Context context, View toolbar) {
        mContext = context;
        mToolbar = toolbar;
        initToolbar();
    }

    public int getStatusBarHeight() {
        int resultPx = 0;
        String statusName = "status_bar_height";
        int resourceId = mContext.getResources().getIdentifier(statusName, "dimen", "android");
        if (resourceId > 0) {
            resultPx = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e(TAG, "status_bar_height:px:" + resultPx);
        // int resultDp = DensityUtils.px2dip(resultPx, mContext);
        // Log.e(TAG, "status_bar_height:dp:" + resultDp);
        return resultPx;
    }

    public void initToolbar() {
        TextView statusBar = mToolbar.findViewById(R.id.status_bar);
        statusBar.setHeight(getStatusBarHeight());

        leftLayout = mToolbar.findViewById(R.id.action_bar_left);
        ImageView backBtn = mToolbar.findViewById(R.id.action_bar_back);
        TextView backText = mToolbar.findViewById(R.id.action_bar_back_text);
        leftLayout.setVisibility(View.VISIBLE);
        leftLayout.setOnClickListener(leftListener);
        backBtn.setOnClickListener(leftListener);
        backText.setOnClickListener(leftListener);

        centerLayout = mToolbar.findViewById(R.id.action_bar_center);
        centerLayout.setVisibility(View.VISIBLE);
        TextView headerTitle = mToolbar.findViewById(R.id.action_bar_title);
        headerTitle.setText(title);
        centerLayout.setOnClickListener(centerListener);
        headerTitle.setOnClickListener(centerListener);

        rightLayout = mToolbar.findViewById(R.id.action_bar_right);
        Button menuText = mToolbar.findViewById(R.id.action_bar_menu_text);
        ImageView menuImg = mToolbar.findViewById(R.id.action_bar_menu);
        rightLayout.setVisibility(View.VISIBLE);
        rightLayout.setOnClickListener(rightListener);
        menuText.setOnClickListener(rightListener);
        menuImg.setOnClickListener(rightListener);
    }

    public void setToolbarClickListener(ToolbarClickListener listener) {
        this.listener = listener;
    }

    public void setLeftVisible(boolean isVisible) {
        if (isVisible) {
            leftLayout.setVisibility(View.VISIBLE);
        } else {
            leftLayout.setVisibility(View.GONE);
        }
    }

    public void setLeftDrawable(int leftDrawable) {
        ImageButton backBtn = leftLayout.findViewById(R.id.action_bar_back);
        backBtn.setImageResource(leftDrawable);
        this.leftDrawable = leftDrawable;
    }

    public void setLeftText(String leftText) {
        TextView backText = leftLayout.findViewById(R.id.action_bar_back_text);
        backText.setText(leftText);
        this.leftText = leftText;
    }

    public void setLeftText(int leftTextResId) {
        TextView backText = leftLayout.findViewById(R.id.action_bar_back_text);
        backText.setText(leftTextResId);
        this.leftTextResId = leftTextResId;
    }

    public void setLeftTextColor(int leftTextColor) {
        TextView backText = leftLayout.findViewById(R.id.action_bar_back_text);
        backText.setTextColor(leftTextColor);
        this.leftTextColor = leftTextColor;
    }

    public void setCenterVisible(boolean isVisible) {
        if (isVisible) {
            centerLayout.setVisibility(View.VISIBLE);
        } else {
            centerLayout.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        TextView headerTitle = centerLayout.findViewById(R.id.action_bar_title);
        headerTitle.setText(title);
        this.title = title;
    }

    public void setTitle(int titleResId) {
        TextView headerTitle = centerLayout.findViewById(R.id.action_bar_title);
        headerTitle.setText(titleResId);
        this.titleResId = titleResId;
    }

    public void setTitleColor(int titleColor) {
        TextView headerTitle = centerLayout.findViewById(R.id.action_bar_title);
        headerTitle.setTextColor(titleColor);
        this.titleColor = titleColor;
    }

    public void setRightVisible(boolean isVisible) {
        if (isVisible) {
            rightLayout.setVisibility(View.VISIBLE);
        } else {
            rightLayout.setVisibility(View.GONE);
        }
    }

    public void setRightText(String rightText) {
        Button menuText = rightLayout.findViewById(R.id.action_bar_menu_text);
        ImageView menuImg = rightLayout.findViewById(R.id.action_bar_menu);
        menuText.setVisibility(View.VISIBLE);
        menuImg.setVisibility(View.GONE);
        menuText.setText(rightText);
        this.rightText = rightText;
    }

    public void setRightText(int rightTextResId) {
        Button menuText = rightLayout.findViewById(R.id.action_bar_menu_text);
        ImageView menuImg = rightLayout.findViewById(R.id.action_bar_menu);
        menuText.setVisibility(View.VISIBLE);
        menuImg.setVisibility(View.GONE);
        this.rightTextResId = rightTextResId;
    }

    public void setRightTextColor(int rightTextColor) {
        Button menuText = rightLayout.findViewById(R.id.action_bar_menu_text);
        menuText.setTextColor(rightTextColor);
        this.rightTextColor = rightTextColor;
    }

    public void setRightDrawable(int rightDrawable) {
        TextView menuText = rightLayout.findViewById(R.id.action_bar_menu_text);
        ImageView menuImg = rightLayout.findViewById(R.id.action_bar_menu);
        menuText.setVisibility(View.GONE);
        menuImg.setVisibility(View.VISIBLE);
        menuImg.setImageResource(rightDrawable);
        this.rightDrawable = rightDrawable;
    }

    public View getToolbar() {
        return mToolbar;
    }

    public ToolbarClickListener getListener() {
        return listener;
    }

    public LinearLayout getLeftLayout() {
        return leftLayout;
    }

    public LinearLayout getCenterLayout() {
        return centerLayout;
    }

    public LinearLayout getRightLayout() {
        return rightLayout;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getTitle() {
        return title;
    }

    public String getRightText() {
        return rightText;
    }

    public int getLeftTextResId() {
        return leftTextResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getRightTextResId() {
        return rightTextResId;
    }

    public int getLeftDrawable() {
        return leftDrawable;
    }

    public int getRightDrawable() {
        return rightDrawable;
    }

    public float getActionBarHeightPx() {
        return (float) DensityUtils.dp2px(mContext, 63);
    }
}
