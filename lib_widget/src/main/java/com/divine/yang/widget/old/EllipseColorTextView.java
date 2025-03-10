package com.divine.yang.widget.old;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.divine.yang.widget.R;


/**
 * author: Divine
 * <p>
 * date: 2018/12/13
 * <p>
 * 左右圆弧矩形，可动态改变颜色和弧度
 * <p>
 * 自定义申请状态textView
 */
public class EllipseColorTextView extends View {

    /**
     * 文本内容
     */
    private String ellipseText;
    /**
     * 文本的颜色
     */
    private int ellipseTextColor;
    /**
     * 文本的大小
     */
    private int ellipseTextSize;

    private int ellipseBackgroundColor;

    /**
     * 圆角大小
     */
    private int ellipseCornerSize;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mtitleBound;
    private Paint mtitlePaint;

    public EllipseColorTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EllipseColorTextView(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public EllipseColorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EllipseColorTextView, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EllipseColorTextView_ellipse_text) {
                ellipseText = a.getString(attr);
            } else if (attr == R.styleable.EllipseColorTextView_ellipse_text_color) {
                // 默认颜色设置为黑色
                ellipseTextColor = a.getColor(attr, Color.BLACK);
            } else if (attr == R.styleable.EllipseColorTextView_ellipse_text_size) {
                // 默认设置为16sp，TypeValue也可以把sp转化为px
                ellipseTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.EllipseColorTextView_ellipse_background) {
                //默认为白色
                ellipseBackgroundColor = a.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.EllipseColorTextView_ellipse_corner_size) {
                //默认圆角为0
                ellipseCornerSize = a.getInteger(attr, 0);
            }
        }
        a.recycle();
        mtitlePaint = new Paint();
        mtitlePaint.setTextSize(ellipseTextSize);
        mtitleBound = new Rect();
        mtitlePaint.getTextBounds(ellipseText, 0, ellipseText.length(), mtitleBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mtitlePaint.setTextSize(ellipseTextSize);
            mtitlePaint.getTextBounds(ellipseText, 0, ellipseText.length(), mtitleBound);

            int desired = getPaddingLeft() + mtitleBound.width() + getPaddingRight();
            width = desired <= widthSize ? desired : widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mtitlePaint.setTextSize(ellipseTextSize);
            mtitlePaint.getTextBounds(ellipseText, 0, ellipseText.length(), mtitleBound);
            int desired = getPaddingTop() + mtitleBound.height() + getPaddingBottom();
            height = desired <= heightSize ? desired : heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(ellipseBackgroundColor);
        RectF rec = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawRoundRect(rec, ellipseCornerSize, ellipseCornerSize, paint);

        mtitlePaint.setColor(ellipseTextColor);
        Paint.FontMetricsInt fontMetrics = mtitlePaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(ellipseText, getPaddingLeft(), baseline, mtitlePaint);
    }

    public void setEllipseText(String ellipseText) {
        this.ellipseText = ellipseText;
    }

    public void setEllipseTextColor(int ellipseTextColor) {
        this.ellipseTextColor = ellipseTextColor;
    }

    public void setEllipseTextSize(int ellipseTextSize) {
        this.ellipseTextSize = ellipseTextSize;
    }

    public void setEllipseBackgroundColor(int ellipseBackgroundColor) {
        this.ellipseBackgroundColor = ellipseBackgroundColor;
    }

    public void setEllipseCornerSize(int ellipseCornerSize) {
        this.ellipseCornerSize = ellipseCornerSize;
    }
}
