package com.divine.yang.main.viewholder;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.main.R;
import com.divine.yang.main.bean.SettingBean;
import com.divine.yang.main.bean.UserBean;
import com.divine.yang.main.enums.LISTTYPE;
import com.google.android.material.textview.MaterialTextView;

/**
 * Project Name  : DivinePlatform
 * Package       : com.divine.yang.main.viewholder
 * Author        : yangzelong(Divine)
 * Email         : yangzelong@jiuqi.com.cn
 * Create Date   : 2024/12/4
 * Description   :
 */
public class SettingViewHolder extends RecyclerView.ViewHolder {
    AppCompatImageView itemImage;
    MaterialTextView itemTitle;

    public SettingViewHolder(@NonNull View itemView) {
        super(itemView);
            // 列表流形式
            itemImage = itemView.findViewById(R.id.item_image_in_list);
            itemTitle = itemView.findViewById(R.id.item_title_in_list);

    }

    public void update(SettingBean data) {
        itemImage.setImageURI(Uri.parse(data.getImage()));
        itemTitle.setText(data.getTitle());


    }
}
