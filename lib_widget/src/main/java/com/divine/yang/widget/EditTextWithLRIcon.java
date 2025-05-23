package com.divine.yang.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Author: Divine
 * CreateDate: 2021/8/25
 * Describe:
 */
public class EditTextWithLRIcon extends AppCompatEditText {
    private Context mContext;

    public EditTextWithLRIcon(Context context) {
        super(context);
        initView(context);
    }

    public EditTextWithLRIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EditTextWithLRIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    Drawable left, right;

    private void initView(Context mContext) {
        this.mContext = mContext;
        Drawable[] compoundDrawables = getCompoundDrawables();
        left = compoundDrawables[0];
        if (left != null) left.setBounds(0, 0, 60, 60);
        String text = getText().toString();
        if (TextUtils.isEmpty(text)) {
            setCompoundDrawables(left, compoundDrawables[1], null, compoundDrawables[3]);
        } else {
            setRightDrawable(compoundDrawables);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        Drawable[] compoundDrawables = getCompoundDrawables();
        if (focused) {
            setDrawableColor(mContext.getColor(com.divine.yang.theme.R.color.BaseThemeColor));
            String text = getText().toString();
            if (TextUtils.isEmpty(text)) {
                setCompoundDrawables(left, compoundDrawables[1], null, compoundDrawables[3]);
            } else {
                setRightDrawable(compoundDrawables);
            }
        } else {
            setDrawableColor(mContext.getResources().getColor(R.color.back_black));
            setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], null, compoundDrawables[3]);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        Drawable[] compoundDrawables = getCompoundDrawables();
        if (!TextUtils.isEmpty(text) && isFocused()) {
            setRightDrawable(compoundDrawables);
        } else {
            setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], null, compoundDrawables[3]);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {// 判断是否点击到了右边的图标区域
            boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight())) && (event.getX() < (getWidth() - getPaddingRight()));
            if (isClean) {
                // 清除字符
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    protected void setDrawableColor(int color) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        if (left != null) left.setTint(color);
        setCompoundDrawables(left, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
    }

    protected void setRightDrawable(Drawable[] compoundDrawables) {
        right = compoundDrawables[2];
        if (null == right) {
            right = mContext.getDrawable(R.mipmap.ic_edit_clear);
        }
        right.setBounds(0, 0, 40, 40);
        setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3]);
    }
}
