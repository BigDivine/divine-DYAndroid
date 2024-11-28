// package com.divine.yang.main.old.common;
//
// import android.content.Context;
// import android.view.View;
//
// import androidx.fragment.app.Fragment;
//
// import com.google.android.material.tabs.TabLayout;
//
// import java.util.ArrayList;
//
// /**
//  * Author: Divine
//  * CreateDate: 2021/8/27
//  * Describe:
//  */
// public class TabLayoutCommon {
//     private Context mContext;
//     private TabLayout tl;
//     private ArrayList<Fragment> fragments;
//
//     public TabLayoutCommon(Context mContext, TabLayout tl, ArrayList<Fragment> fragments) {
//         this.mContext = mContext;
//         this.tl = tl;
//         this.fragments = fragments;
//     }
//
//     public void setTabLayout(int selectPosition) {
//         for (int i = 0; i < fragments.size(); i++) {
//             TabLayout.Tab tab = tl.getTabAt(i);
//             View customView = tab.getCustomView();
//             // if (customView == null) {
//             //     customView = LayoutInflater.from(mContext).inflate(R.old.item_img_text_v, null);
//             // }
//             // BaseFragment fragment = (BaseFragment) fragments.get(i);
//             // int tabColor;
//             // if (i == selectPosition) {
//             //     tabColor = mContext.getResources().getColor(R.color.LoginThemeColor);
//             // } else {
//             //     tabColor = mContext.getResources().getColor(R.color.back_black);
//             // }
//             // tab.setCustomView(customView);
//             // ImageView ivItemImg = customView.findViewById(R.id.item_img_text_v_img);
//             // TextView tvItemText = customView.findViewById(R.id.item_img_text_v_text);
//             // tvItemText.setText(fragment.getTitle());
//             // ivItemImg.setImageResource(fragment.getIconResId());
//             // tvItemText.setTextColor(tabColor);
//             // ivItemImg.setColorFilter(tabColor);
//         }
//     }
// }
