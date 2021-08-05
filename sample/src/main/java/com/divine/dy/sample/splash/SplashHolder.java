package com.divine.dy.sample.splash;

import android.view.View;
import android.widget.ImageView;

import com.divine.dy.sample.R;

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
        image = itemView.findViewById(R.id.splash_image);
    }
}
