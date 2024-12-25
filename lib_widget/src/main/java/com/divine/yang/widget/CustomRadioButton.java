package com.divine.yang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.radiobutton.MaterialRadioButton;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.widget
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/12/25
 * Description   :
 */
public class CustomRadioButton extends MaterialRadioButton {
    public CustomRadioButton(@NonNull Context context) {
        super(context);
        initView(context, null, 0);
    }

    public CustomRadioButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public CustomRadioButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public void initView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRadioButton, defStyleAttr, 0);
        int drawableSizeDip = typedArray.getDimensionPixelOffset(R.styleable.CustomRadioButton_drawable_size, 20);
        int drawableColor = typedArray.getColor(R.styleable.CustomRadioButton_drawable_color, -1);
        // int drawableCheckedColor = typedArray.getColor(R.styleable.CustomRadioButton_drawable_checkedColor, -1);
        // int drawableUncheckedColor = typedArray.getColor(R.styleable.CustomRadioButton_drawable_uncheckedColor, -1);
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable drawableLeft = compoundDrawables[0];
        Drawable drawableTop = compoundDrawables[1];
        Drawable drawableRight = compoundDrawables[2];
        Drawable drawableBottom = compoundDrawables[3];

        if (null != drawableLeft) {
            drawableLeft.setBounds(0, 0, drawableSizeDip, drawableSizeDip);
            if (drawableColor != -1) {
                drawableLeft.setTint(drawableColor);
            }
        }
        if (null != drawableTop) {
            drawableTop.setBounds(0, 0, drawableSizeDip, drawableSizeDip);
            if (drawableColor != -1) {
                drawableTop.setTint(drawableColor);
            }
        }
        if (null != drawableRight) {
            drawableRight.setBounds(0, 0, drawableSizeDip, drawableSizeDip);
            if (drawableColor != -1) {
                drawableRight.setTint(drawableColor);
            }
        }
        if (null != drawableBottom) {
            drawableBottom.setBounds(0, 0, drawableSizeDip, drawableSizeDip);
            if (drawableColor != -1) {
                drawableBottom.setTint(drawableColor);
            }
        }

        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        typedArray.recycle();
    }
}
