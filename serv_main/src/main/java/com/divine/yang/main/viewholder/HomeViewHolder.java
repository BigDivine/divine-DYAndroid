package com.divine.yang.main.viewholder;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.main.R;
import com.divine.yang.main.bean.HomeBean;
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
public class HomeViewHolder extends RecyclerView.ViewHolder {
    private int listType;
    AppCompatImageView itemImage;
    MaterialTextView itemTitle;

    public HomeViewHolder(@NonNull View itemView, int type) {
        super(itemView);
        this.listType = type;
        if (this.listType == LISTTYPE.LIST.ordinal()) {
            // 列表流形式
            itemImage = itemView.findViewById(R.id.item_image_in_list);
            itemTitle = itemView.findViewById(R.id.item_title_in_list);
        } else if (this.listType == LISTTYPE.FALL.ordinal()) {
            // 瀑布流形式
            itemImage = itemView.findViewById(R.id.item_image_in_fall);
            itemTitle = itemView.findViewById(R.id.item_title_in_fall);
        } else if (this.listType == LISTTYPE.CARD.ordinal()) {
            // 卡片流形式
            itemImage = itemView.findViewById(R.id.item_image_in_card);
            itemTitle = itemView.findViewById(R.id.item_title_in_card);
        }
    }

    public void update(HomeBean data) {
        itemImage.setImageURI(Uri.parse(data.getImage()));
        itemTitle.setText(data.getTitle());

        // if (this.listType == LISTTYPE.LIST.ordinal()) {
        //     // 列表流形式
        // } else if (this.listType == LISTTYPE.FALL.ordinal()) {
        //     // 瀑布流形式
        // } else if (this.listType == LISTTYPE.CARD.ordinal()) {
        //     // 卡片流形式
        // }
    }
}
