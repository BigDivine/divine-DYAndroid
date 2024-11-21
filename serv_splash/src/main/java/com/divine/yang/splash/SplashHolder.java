package com.divine.yang.splash;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2021/3/1
 * Describe:
 */
public class SplashHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    //    public TextView text;

    public SplashHolder(@NonNull View itemView) {
        super(itemView);
        // image = itemView.findViewById(R.id.splash_item_image);
    }
}
