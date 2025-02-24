package com.divine.yang.theme;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

/**
 * Author: Divine
 * CreateDate: 2022/4/29
 * Describe:
 */
public class ThemeSource {
    /**
     * 使控件增加一个纯色背景
     *
     * @param view
     * @param color
     */
    public static void setViewBackground(View view, int color) {
        view.setBackgroundColor(color);
    }

    /**
     * 使控件增加一个渐变色背景。自左至右的渐变
     *
     * @param view
     * @param colors
     */
    public static void setViewBackground(View view, int[] colors) {
        GradientDrawable headerBg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        view.setBackground(headerBg);
    }

    /**
     * 带圆角的长方形按钮，颜色为纯色
     *
     * @param mContext
     * @param button
     * @param color
     */
    public static void setButtonTheme(Context mContext, Button button, int color) {
        GradientDrawable ShapeBlueCorner5dp = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.shape_blue_corner_5dp);
        ShapeBlueCorner5dp.setColor(color);
        button.setBackground(ShapeBlueCorner5dp);
    }

    /**
     * 控件CheckBox按钮图片的颜色定制，圆形选中和不选中情况
     *
     * @param mContext
     * @param checkBox
     * @param color
     */
    public static void setCheckBoxButtonDrawable(Context mContext, CheckBox checkBox, int color) {
        Drawable radioButtonNo = mContext.getResources().getDrawable(R.mipmap.no_check);
        radioButtonNo.setTint(color);
        Drawable radioButtonOn = mContext.getResources().getDrawable(R.mipmap.on_check);
        radioButtonOn.setTint(color);
        StateListDrawable RadioButton = new StateListDrawable();
        RadioButton.addState(new int[]{-android.R.attr.state_checked}, radioButtonNo);
        RadioButton.addState(new int[]{android.R.attr.state_checked}, radioButtonOn);
        checkBox.setButtonDrawable(RadioButton);
    }

    /**
     * 设置输入框EditText控件的光标颜色为主题色
     *
     * @param mContext
     * @param editText
     * @param color
     */
    public static void setEditCursorDrawable(Context mContext, EditText editText, int color) {
        GradientDrawable EditTextCursor = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.edit_text_cursor);
        EditTextCursor.setColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.setTextCursorDrawable(EditTextCursor);
        }
    }

    /**
     * 设置输入框EditText控件的聚焦时下划线的颜色
     *
     * @param mContext
     * @param editText
     * @param color
     */
    public static void setEditFocusBottomBorder(Context mContext, EditText editText, int color) {
        LayerDrawable EditTextBorderNoFocus = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.layer_list_edit_bottom_border_grey);
        LayerDrawable EditTextBorderFocus = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.layer_list_edit_bottom_border_blue);
        GradientDrawable drawable = (GradientDrawable) EditTextBorderFocus.getDrawable(1);
        drawable.setStroke(3, color, 0, 0);
        StateListDrawable EditTextBorder = new StateListDrawable();
        EditTextBorder.addState(new int[]{android.R.attr.state_focused}, EditTextBorderFocus);
        EditTextBorder.addState(new int[]{-android.R.attr.state_focused}, EditTextBorderNoFocus);
        editText.setBackground(EditTextBorder);
    }
    //    public static void setProgressCircleRotateAnim(Context mContext, ProgressBar progressBar,int color){}

    /**
     * 可以旋转的原型loading，使用ProgressBar控件，颜色为渐变色
     *
     * @param mContext
     * @param progressBar
     * @param colors
     */
    public static void setProgressCircleRotateAnim(Context mContext, ProgressBar progressBar, int[] colors) {
        RotateDrawable rotateDrawable = (RotateDrawable) mContext.getResources().getDrawable(R.drawable.rotate_progress_dialog);
        GradientDrawable rotateShape = (GradientDrawable) rotateDrawable.getDrawable();
        rotateShape.setColors(colors);
        progressBar.setIndeterminateDrawable(rotateDrawable);
    }

    /**
     * 长条形的ProgressBar控件，进度条颜色，渐变色
     *
     * @param mContext
     * @param progressBar
     * @param colors
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setProgressBarProgressDrawable(Context mContext, ProgressBar progressBar, int[] colors) {
        LayerDrawable progressDrawable = (LayerDrawable) mContext.getResources().getDrawable(R.drawable.progress_bar);
        ClipDrawable progressClip = (ClipDrawable) progressDrawable.getDrawable(1);
        GradientDrawable progressClipGradient = (GradientDrawable) progressClip.getDrawable();
        progressClipGradient.setColors(colors);
        progressBar.setProgressDrawable(progressDrawable);
    }
}
