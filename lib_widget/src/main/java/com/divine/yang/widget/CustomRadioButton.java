package com.divine.yang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.widget
 * Author        : Divine.Yang(Divine)
 * Email         : 
 * Create Date   : 2024/12/25
 * Description   :
 */
public class CustomRadioButton extends AppCompatRadioButton {
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
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        Drawable drawableTop = drawables[1];
        Drawable drawableRight = drawables[2];
        Drawable drawableBottom = drawables[3];
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
        typedArray.recycle();

        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }
}
