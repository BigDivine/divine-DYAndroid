package com.divine.yang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.widget
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2025/01/16
 * Description   :
 */
public class CustomRadioButtonDemo extends LinearLayout {
    public boolean checked;
    Drawable customBackground;
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CustomRadioButtonDemo(@NonNull Context context) {
        super(context);
        initView(context, null, 0);
    }

    public CustomRadioButtonDemo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public CustomRadioButtonDemo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public void initView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRadioButtonDemo, defStyleAttr, 0);
        int drawableSizeDip = typedArray.getDimensionPixelOffset(R.styleable.CustomRadioButtonDemo_drawable_size_demo, 20);
        int drawableColor = typedArray.getColor(R.styleable.CustomRadioButtonDemo_drawable_color_demo, -1);
        customBackground = typedArray.getDrawable(R.styleable.CustomRadioButtonDemo_custom_background_demo);
        if (null != customBackground) {
            customBackground.setBounds(0, 0, drawableSizeDip, drawableSizeDip);
            if (drawableColor != -1) {
                customBackground.setTint(drawableColor);
            }
        }
        typedArray.recycle();
        setBackground(customBackground);
        setOnClickListener(v -> {
            setChecked(!checked);
        });
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable customBackgroundFinal = customBackground;
        if (customBackgroundFinal != null && customBackgroundFinal.isStateful()
                && customBackgroundFinal.setState(getDrawableState())) {
            invalidateDrawable(customBackgroundFinal);
        }
    }

    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
        }
    }
    public boolean isChecked() {
        return checked;
    }
}
