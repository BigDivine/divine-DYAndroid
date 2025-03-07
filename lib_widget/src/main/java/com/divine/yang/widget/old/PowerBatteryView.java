package com.divine.yang.widget.old;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
 import android.util.AttributeSet;
import android.view.View;

import com.divine.yang.widget.R;

import androidx.annotation.Nullable;


/**
 * 自定义 耗电排行内的 电池图标
 * </p>
 * created by OuyangPeng at 2018/6/10 下午 05:57
 *
 * @author OuyangPeng
 * <p>
 * <p>
 * author: Divine
 * <p>
 * date: 2019/2/22
 */
public class PowerBatteryView extends View {
    /**
     * 电池最大电量
     */
    public static final int MAX_LEVEL = 100;
    /**
     * 电池默认电量
     */
    public static final int DEFAULT_LEVEL = 40;

    /**
     * 自定义View的宽
     */
    private int width;
    /**
     * 自定义View的高
     */
    private int height;
    /**
     * 抗锯齿标志
     */
    private DrawFilter drawFilter;
    /**
     * 电池外壳 厚度
     */
    private int shellStrokeWidth;
    /**
     * 电池外壳 圆角
     */
    private int shellCornerRadius;
    /**
     * 电池外壳 宽度
     */
    private int shellWidth;
    /**
     * 电池外壳 宽度
     */
    private int shellHeight;
    /**
     * 电池头 圆角
     */
    private int shellHeadCornerRadius;
    /**
     * 电池头 宽度
     */
    private int shellHeadWidth;
    /**
     * 电池头 高度
     */
    private int shellHeadHeight;

    /**
     * 电池宽度
     */
    private int levelWidth;
    /**
     * 电池最大高度
     */
    private int levelMaxHeight;
    /**
     * 电池高度
     */
    private int levelHeight = 100;

    /**
     * 电池外壳和电池等级直接的间距
     */
    private int gap;

    /**
     * 电池外壳 画笔
     */
    private Paint shellPaint;
    /**
     * 电池外壳
     */
    private RectF shellRectF;
    /**
     * 电池头
     */
    private RectF shellHeadRect;

    /**
     * 电池电量 画笔
     */
    private Paint levelPaint;
    /**
     * 电池电量
     */
    private RectF levelRect;

    /**
     * 低电颜色
     */
    private int lowerPowerColor;
    /**
     * 在线颜色
     */
    private int onlineColor;
    /**
     * 离线颜色
     */
    private int offlineColor;

    public PowerBatteryView(Context context) {
        super(context);
    }

    public PowerBatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        initTypeArray(context, attrs);

        //设置 电池壳画笔的相关属性
        shellPaint = new Paint();
        shellPaint.setAntiAlias(true);
        shellPaint.setColor(onlineColor);
        shellPaint.setStrokeWidth(shellStrokeWidth);
        shellPaint.setAntiAlias(true);

        //设置 电池画笔的相关属性
        levelPaint = new Paint();
        levelPaint.setColor(onlineColor);
        levelPaint.setStyle(Paint.Style.FILL);
        levelPaint.setStrokeWidth(levelWidth);

        shellRectF = new RectF();
        shellHeadRect = new RectF();
        levelRect = new RectF();
    }

    /**
     * 初始化自定义属性
     */
    private void initTypeArray(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PowerBatteryView);
        lowerPowerColor = typedArray.getColor(R.styleable.PowerBatteryView_batteryLowerPowerColor,
                getResources().getColor(R.color.lowerPowerColor));
        onlineColor = typedArray.getColor(R.styleable.PowerBatteryView_batteryOnlineColor,
                getResources().getColor(R.color.onlineColor));
        offlineColor = typedArray.getColor(R.styleable.PowerBatteryView_batteryOfflineColor,
                getResources().getColor(R.color.offlineColor));
        //外壳的相关信息
        shellCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellCornerRadius,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_corner));
        shellWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_width));
        shellHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_height));
        shellStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellStrokeWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_stroke_width));

        //电池头的相关信息
        shellHeadCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellHeadCornerRadius,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_corner));
        shellHeadWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellHeadWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_width));
        shellHeadHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryShellHeadHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_height));

        //电池最大高度
        levelMaxHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryLevelMaxHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_level_max_height));
        //电池宽度
        levelWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryLevelWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_level_width));

        //电池外壳和电池等级直接的间距
        gap = typedArray.getDimensionPixelOffset(R.styleable.PowerBatteryView_batteryGap,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_gap));
        //回收typedArray
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //对View上的內容进行测量后得到的View內容占据的宽度
        width = getMeasuredWidth();
        //对View上的內容进行测量后得到的View內容占据的高度
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(drawFilter);
        //垂直电池图标，电池头在上
        //        verticalBattery(canvas);
        //        水平电池图标，电池头在右
        horizontalBattery(canvas);

        //绘制电池壳
        shellPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(shellRectF, shellCornerRadius, shellCornerRadius, shellPaint);

        //绘制电池等级
        canvas.drawRect(levelRect, levelPaint);
    }

    private void verticalBattery(Canvas canvas) {
        //         电池头 矩形的坐标
        //坐标 left：控件整体宽度的一半 减去 电池头宽度的一半
        shellHeadRect.left = width / 2 - shellHeadWidth / 2;
        //坐标 top： 0
        shellHeadRect.top = 0;
        //坐标 right：控件整体宽度的一半 加上 电池头宽度的一半
        shellHeadRect.right = width / 2 + shellHeadWidth / 2;
        //坐标 bottom：电池头的高度
        shellHeadRect.bottom = shellHeadHeight;

        //         电池壳 矩形的坐标
        //坐标 left：电池壳厚度的一半
        shellRectF.left = shellStrokeWidth / 2;
        //坐标 top：电池壳厚度的一半 加上 电池头的高度
        shellRectF.top = shellStrokeWidth / 2 + shellHeadHeight;
        //坐标 right：控件整体宽度 减去 电池壳厚度的一半
        shellRectF.right = width - shellStrokeWidth / 2;
        //坐标 bottom：控件整体高度 减去 电池壳厚度的一半
        shellRectF.bottom = height - shellStrokeWidth / 2;

        //         电池电量 矩形的坐标
        //坐标 left：电池壳厚度的一半 加上 电池外壳和电池等级直接的间距
        levelRect.left = shellStrokeWidth + gap;

        //电池满格时候的最大高度 ：（控件整体高度  减去电池壳厚度 减去电池头高度 减去 电池外壳和电池等级直接的间距的两倍）
        //topOffset: 电池满格时候的最大高度 * （电池满格100 - 当前的电量）/ 电池满格100
        float topOffset = (height - shellHeadHeight - gap * 2 - shellStrokeWidth) * (MAX_LEVEL - levelHeight) / MAX_LEVEL;
        //坐标 top：电池头的高度 + 电池壳的厚度 + 电池外壳和电池等级直接的间距 + topOffset
        levelRect.top = shellHeadHeight + shellStrokeWidth + gap + topOffset;

        //坐标 right：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.right = width - shellStrokeWidth - gap;

        //坐标 bottom：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.bottom = height - shellStrokeWidth - gap;

        //绘制电池头
        shellPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(shellHeadRect, shellHeadCornerRadius, shellHeadCornerRadius, shellPaint);
        //由于电池头的左下角和右下角不是圆角的，因此我们需要画一个矩形 覆盖圆角
        //绘制左下角矩形
        canvas.drawRect(shellHeadRect.left, shellHeadRect.bottom - shellHeadCornerRadius,
                shellHeadRect.left + shellHeadCornerRadius, shellHeadRect.bottom, shellPaint);
        //绘制右下角矩形
        canvas.drawRect(shellHeadRect.right - shellHeadCornerRadius, shellHeadRect.bottom - shellHeadCornerRadius,
                shellHeadRect.right, shellHeadRect.bottom, shellPaint);
    }

    private void horizontalBattery(Canvas canvas) {
        // 电池头 矩形的坐标
        //坐标 left：控件整体宽度的一半 减去 电池头宽度的一半
        shellHeadRect.top = height / 2 - shellHeadWidth / 2;
        //坐标 top： 0
        shellHeadRect.left = width - shellHeadHeight;
        //坐标 right：控件整体宽度的一半 加上 电池头宽度的一半
        shellHeadRect.bottom = height / 2 + shellHeadWidth / 2;
        //坐标 bottom：电池头的高度
        shellHeadRect.right = width;

        // 电池壳 矩形的坐标
        //坐标 left：电池壳厚度的一半
        shellRectF.top = shellStrokeWidth / 2;
        //坐标 top：电池壳厚度的一半 加上 电池头的高度
        shellRectF.left = shellStrokeWidth / 2;
        //坐标 right：控件整体宽度 减去 电池壳厚度的一半
        shellRectF.bottom = height - shellStrokeWidth / 2;
        //坐标 bottom：控件整体高度 减去 电池壳厚度的一半
        shellRectF.right = width - shellStrokeWidth / 2 - shellHeadHeight;

        // 电池电量 矩形的坐标
        //坐标 left：电池壳厚度的一半 加上 电池外壳和电池等级直接的间距
        levelRect.top = shellStrokeWidth + gap;
        levelRect.left = shellStrokeWidth + gap;
        //坐标 right：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.bottom = height - shellStrokeWidth - gap;
        //坐标 bottom：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.right = shellStrokeWidth + gap + (width - (shellStrokeWidth + gap) * 2 - shellHeadHeight) * levelHeight / MAX_LEVEL;

        //绘制电池头
        shellPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(shellHeadRect, shellHeadCornerRadius, shellHeadCornerRadius, shellPaint);
        //由于电池头的左下角和右下角不是圆角的，因此我们需要画一个矩形 覆盖圆角
        //绘制左上角矩形
//        canvas.drawRect(shellHeadRect.left, shellHeadRect.top,
//                shellHeadRect.left + shellHeadCornerRadius, shellHeadRect.top + shellHeadCornerRadius, shellPaint);
//        //绘制左下角矩形
//        canvas.drawRect(shellHeadRect.left, shellHeadRect.bottom - shellHeadCornerRadius,
//                shellHeadRect.left + shellHeadCornerRadius, shellHeadRect.bottom, shellPaint);
    }

    /**
     * 设置电池电量
     *
     * @param level
     */
    public void setLevelHeight(int level) {
        this.levelHeight = level;
        if (this.levelHeight < 0) {
            levelHeight = MAX_LEVEL;
        } else if (this.levelHeight > MAX_LEVEL) {
            levelHeight = MAX_LEVEL;
        }
        postInvalidate();
    }

    /**
     * 设置在线 重绘
     */
    public void setOnline() {
        shellPaint.setColor(onlineColor);
        levelPaint.setColor(onlineColor);
        postInvalidate();
    }

    /**
     * 设置离线 重绘
     */
    public void setOffline() {
        shellPaint.setColor(offlineColor);
        levelPaint.setColor(offlineColor);
        postInvalidate();
    }

    /**
     * 设置低电 重绘
     */
    public void setLowerPower() {
        shellPaint.setColor(lowerPowerColor);
        levelPaint.setColor(lowerPowerColor);
        postInvalidate();
    }
}
