package com.divine.dy.app_main.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.divine.dy.app_main.R;

/**
 * Author: Divine
 * CreateDate: 2021/8/27
 * Describe:
 */
public class RadioGroupCommon {
    private Context mContext;
    private RadioGroup mRadioGroup;

    public RadioGroupCommon(Context mContext, RadioGroup mRadioGroup) {
        this.mContext = mContext;
        this.mRadioGroup = mRadioGroup;
    }

    public void setDrawable(int checkedId) {
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
            RadioButton rbCheck = (RadioButton) mRadioGroup.getChildAt(i);
            if (rbCheck.getId() == checkedId) {
                setDrawable(rbCheck, R.color.LoginThemeColor);
            } else {
                setDrawable(rbCheck, R.color.back_black);
            }
        }
    }

    private void setDrawable(RadioButton rb, int tintColor) {
        Drawable[] drawables = rb.getCompoundDrawables();
        Drawable top = drawables[1];
        top.setBounds(0, 0, 50, 50);
        top.setTint(mContext.getResources().getColor(tintColor));
        rb.setCompoundDrawables(drawables[0], top, drawables[2], drawables[3]);
        rb.setTextColor(mContext.getResources().getColor(tintColor));
    }
}
